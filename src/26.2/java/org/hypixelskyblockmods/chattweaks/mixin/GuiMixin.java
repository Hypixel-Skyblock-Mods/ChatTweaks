package org.hypixelskyblockmods.chattweaks.mixin;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.Mth;
import org.hypixelskyblockmods.chattweaks.ChatTweaksClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class GuiMixin {
	@Shadow @Final private Minecraft minecraft;
	@Shadow @Final private ChatComponent chat;
	@Shadow private int tickCount;

	@Inject(method = "extractChat", at = @At("HEAD"), cancellable = true)
	private void chattweaks$renderPeekedChat(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
		if (!ChatTweaksClient.isPeeking() || this.minecraft.player == null) {
			return;
		}

		Window window = this.minecraft.getWindow();
		int mouseX = Mth.floor(this.minecraft.mouseHandler.getScaledXPos(window));
		int mouseY = Mth.floor(this.minecraft.mouseHandler.getScaledYPos(window));
		graphics.nextStratum();
		this.chat.extractRenderState(
			graphics,
			this.minecraft.font,
			this.tickCount,
			mouseX,
			mouseY,
			ChatComponent.DisplayMode.FOREGROUND,
			false
		);
		ci.cancel();
	}
}
