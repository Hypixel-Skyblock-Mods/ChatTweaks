package org.hypixelskyblockmods.chattweaks;

import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.Version;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public final class ChatTweaksUpdateChecker {
	private static final String RELEASE_URL = "https://api.github.com/repos/Hypixel-Skyblock-Mods/ChatTweaks/releases/latest";
	private static final long CACHE_MILLIS = 15 * 60 * 1000L;
	private static final Object LOCK = new Object();
	private static final HttpClient CLIENT = HttpClient.newBuilder()
		.connectTimeout(Duration.ofSeconds(5))
		.followRedirects(HttpClient.Redirect.NORMAL)
		.build();
	private static final String INSTALLED_VERSION = FabricLoader.getInstance()
		.getModContainer(ChatTweaksClient.MOD_ID)
		.orElseThrow()
		.getMetadata()
		.getVersion()
		.getFriendlyString()
		.split("\\+", 2)[0];

	private static volatile long lastCompletedAt;
	private static volatile Snapshot snapshot = new Snapshot(INSTALLED_VERSION, State.NOT_CHECKED, null);

	private ChatTweaksUpdateChecker() {
	}

	public static Snapshot snapshot() {
		return snapshot;
	}

	public static void refresh(boolean force) {
		synchronized (LOCK) {
			if (snapshot.state() == State.CHECKING) {
				return;
			}
			if (!force && lastCompletedAt != 0L && System.currentTimeMillis() - lastCompletedAt < CACHE_MILLIS) {
				return;
			}
			snapshot = new Snapshot(INSTALLED_VERSION, State.CHECKING, null);
		}

		CompletableFuture.runAsync(() -> {
			Snapshot result;
			try {
				result = fetchLatestVersion();
			} catch (Exception exception) {
				ChatTweaksClient.LOGGER.warn("Could not check the latest ChatTweaks release", exception);
				result = new Snapshot(INSTALLED_VERSION, State.FAILED, null);
			}
			synchronized (LOCK) {
				snapshot = result;
				lastCompletedAt = System.currentTimeMillis();
			}
		});
	}

	private static Snapshot fetchLatestVersion() throws Exception {
		HttpRequest request = HttpRequest.newBuilder(URI.create(RELEASE_URL))
			.timeout(Duration.ofSeconds(8))
			.header("Accept", "application/vnd.github+json")
			.header("User-Agent", "ChatTweaks/" + INSTALLED_VERSION)
			.header("X-GitHub-Api-Version", "2022-11-28")
			.GET()
			.build();
		HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() != 200) {
			throw new IllegalStateException("GitHub returned HTTP " + response.statusCode());
		}

		String latestVersion = JsonParser.parseString(response.body())
			.getAsJsonObject()
			.get("tag_name")
			.getAsString();
		if (latestVersion.startsWith("v")) {
			latestVersion = latestVersion.substring(1);
		}
		if (latestVersion.isBlank()) {
			throw new IllegalStateException("GitHub's latest release did not include tag_name");
		}

		Version current = SemanticVersion.parse(INSTALLED_VERSION);
		Version latest = SemanticVersion.parse(latestVersion);
		State state = current.compareTo(latest) >= 0 ? State.UP_TO_DATE : State.UPDATE_AVAILABLE;
		return new Snapshot(INSTALLED_VERSION, state, latestVersion);
	}

	public record Snapshot(String currentVersion, State state, String latestVersion) {
		public String message() {
			return switch (state) {
				case NOT_CHECKED -> "Update status not checked";
				case CHECKING -> "Checking for updates...";
				case UP_TO_DATE -> "Up to date";
				case UPDATE_AVAILABLE -> "Update available: " + latestVersion;
				case FAILED -> "Could not check for updates";
			};
		}

		public int color() {
			return switch (state) {
				case NOT_CHECKED, FAILED -> 0xA0A0A0;
				case CHECKING -> 0xFFD966;
				case UP_TO_DATE -> 0x55FF55;
				case UPDATE_AVAILABLE -> 0xFF5555;
			};
		}
	}

	public enum State {
		NOT_CHECKED,
		CHECKING,
		UP_TO_DATE,
		UPDATE_AVAILABLE,
		FAILED
	}
}
