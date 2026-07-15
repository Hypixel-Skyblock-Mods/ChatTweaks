package org.hypixelskyblockmods.chattweaks;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.hypixelskyblockmods.chattweaks.mixin.ChatComponentAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public final class ChatTweaksClient implements ClientModInitializer {
	public static final String MOD_ID = "chattweaks";
	public static final Logger LOGGER = LoggerFactory.getLogger("ChatTweaks");
	public static final int VANILLA_HISTORY_LIMIT = 100;
	public static final int EXTENDED_HISTORY_LIMIT = 1_000;

	private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"));
	public static final KeyMapping PEEK_CHAT = new KeyMapping(
		"key.chattweaks.peek_chat",
		InputConstants.Type.KEYSYM,
		90,
		CATEGORY
	);

	private static ChatTweaksConfig config;
	private static boolean wasPeeking;

	@Override
	public void onInitializeClient() {
		config = ChatTweaksConfig.load();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
			literal("chattweaks").executes(context -> {
				Minecraft minecraft = Minecraft.getInstance();
				minecraft.setScreen(new ChatTweaksConfigScreen(minecraft.screen));
				return 1;
			})
		));

		ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
			boolean peeking = isPeeking();
			if (wasPeeking && !peeking && minecraft.gui != null) {
				minecraft.gui.getChat().resetChatScroll();
			}
			wasPeeking = peeking;
		});
	}

	public static boolean isPeeking() {
		Minecraft minecraft = Minecraft.getInstance();
		return PEEK_CHAT.isDown() && minecraft.screen == null && minecraft.getOverlay() == null;
	}

	public static int historyLimit() {
		return config != null && config.extendedChatHistory ? EXTENDED_HISTORY_LIMIT : VANILLA_HISTORY_LIMIT;
	}

	public static boolean extendedHistoryEnabled() {
		return config == null || config.extendedChatHistory;
	}

	public static void setExtendedHistoryEnabled(boolean enabled) {
		config.extendedChatHistory = enabled;
		config.save();

		if (!enabled) {
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.gui != null) {
				ChatComponentAccessor chat = (ChatComponentAccessor) minecraft.gui.getChat();
				trimToLimit(chat.chattweaks$getAllMessages(), VANILLA_HISTORY_LIMIT);
				trimToLimit(chat.chattweaks$getTrimmedMessages(), VANILLA_HISTORY_LIMIT);
				while (chat.chattweaks$getRecentChat().size() > VANILLA_HISTORY_LIMIT) {
					chat.chattweaks$getRecentChat().removeFirst();
				}
				minecraft.gui.getChat().rescaleChat();
			}
		}
	}

	private static void trimToLimit(List<?> messages, int limit) {
		while (messages.size() > limit) {
			messages.removeLast();
		}
	}
}
