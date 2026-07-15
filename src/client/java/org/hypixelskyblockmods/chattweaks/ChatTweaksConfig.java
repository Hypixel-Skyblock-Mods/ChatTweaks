package org.hypixelskyblockmods.chattweaks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ChatTweaksConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("chattweaks.json");

	public boolean extendedChatHistory = true;

	public static ChatTweaksConfig load() {
		if (Files.isRegularFile(PATH)) {
			try (Reader reader = Files.newBufferedReader(PATH)) {
				ChatTweaksConfig config = GSON.fromJson(reader, ChatTweaksConfig.class);
				if (config != null) {
					return config;
				}
			} catch (IOException | RuntimeException exception) {
				ChatTweaksClient.LOGGER.warn("Could not read {}; using defaults", PATH, exception);
			}
		}

		ChatTweaksConfig config = new ChatTweaksConfig();
		config.save();
		return config;
	}

	public void save() {
		try {
			Files.createDirectories(PATH.getParent());
			try (Writer writer = Files.newBufferedWriter(PATH)) {
				GSON.toJson(this, writer);
			}
		} catch (IOException exception) {
			ChatTweaksClient.LOGGER.error("Could not save {}", PATH, exception);
		}
	}
}
