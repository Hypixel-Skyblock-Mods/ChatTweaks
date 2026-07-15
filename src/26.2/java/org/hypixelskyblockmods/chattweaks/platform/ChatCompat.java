package org.hypixelskyblockmods.chattweaks.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;

public final class ChatCompat {
	private ChatCompat() {
	}

	public static ChatComponent chat(Minecraft minecraft) {
		return minecraft.gui.hud.getChat();
	}

	public static boolean hasNoScreenOrOverlay(Minecraft minecraft) {
		return minecraft.gui.screen() == null && minecraft.gui.overlay() == null;
	}
}
