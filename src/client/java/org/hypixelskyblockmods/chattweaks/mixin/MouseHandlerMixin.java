package org.hypixelskyblockmods.chattweaks.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.Mth;
import org.hypixelskyblockmods.chattweaks.ChatTweaksClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
	@Shadow @Final private Minecraft minecraft;

	@Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
	private void chattweaks$scrollPeekedChat(long handle, double xOffset, double yOffset, CallbackInfo ci) {
		if (handle != this.minecraft.getWindow().handle()
			|| !ChatTweaksClient.isPeeking()
			|| !ChatTweaksClient.peekScrollingEnabled()) {
			return;
		}

		double scroll = Mth.clamp(yOffset * this.minecraft.options.mouseWheelSensitivity().get(), -1.0, 1.0);
		if (!this.minecraft.hasShiftDown()) {
			scroll *= 7.0;
		}
		this.minecraft.gui.getChat().scrollChat((int) scroll);
		ci.cancel();
	}
}
