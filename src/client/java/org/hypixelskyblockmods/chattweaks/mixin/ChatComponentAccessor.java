package org.hypixelskyblockmods.chattweaks.mixin;

import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.util.ArrayListDeque;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ChatComponent.class)
public interface ChatComponentAccessor {
	@Accessor("allMessages")
	List<GuiMessage> chattweaks$getAllMessages();

	@Accessor("trimmedMessages")
	List<GuiMessage.Line> chattweaks$getTrimmedMessages();

	@Accessor("recentChat")
	ArrayListDeque<String> chattweaks$getRecentChat();
}
