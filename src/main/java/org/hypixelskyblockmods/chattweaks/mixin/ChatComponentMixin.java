package org.hypixelskyblockmods.chattweaks.mixin;

import net.minecraft.client.gui.components.ChatComponent;
import org.hypixelskyblockmods.chattweaks.ChatTweaksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin {
	@Inject(method = "isChatFocused", at = @At("RETURN"), cancellable = true)
	private void chattweaks$treatPeekAsFocused(CallbackInfoReturnable<Boolean> cir) {
		if (ChatTweaksClient.isPeeking()) {
			cir.setReturnValue(true);
		}
	}

	@ModifyConstant(method = "addMessageToDisplayQueue", constant = @Constant(intValue = 100))
	private int chattweaks$extendRenderedHistory(int original) {
		return ChatTweaksClient.historyLimit();
	}

	@ModifyConstant(method = "addMessageToQueue", constant = @Constant(intValue = 100))
	private int chattweaks$extendMessageHistory(int original) {
		return ChatTweaksClient.historyLimit();
	}

	@ModifyConstant(method = "addRecentChat", constant = @Constant(intValue = 100))
	private int chattweaks$extendSentHistory(int original) {
		return ChatTweaksClient.historyLimit();
	}
}
