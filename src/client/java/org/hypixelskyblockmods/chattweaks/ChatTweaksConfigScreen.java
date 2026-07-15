package org.hypixelskyblockmods.chattweaks;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public final class ChatTweaksConfigScreen extends Screen {
	private final @Nullable Screen parent;
	private final LinearLayout layout = LinearLayout.vertical().spacing(8);

	public ChatTweaksConfigScreen(@Nullable Screen parent) {
		super(Component.translatable("chattweaks.config.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();
		this.layout.defaultCellSetting().alignHorizontallyCenter();
		this.layout.addChild(new StringWidget(this.title, this.font));
		this.layout.addChild(
			new MultiLineTextWidget(Component.translatable("chattweaks.config.extended_history.description"), this.font)
				.setMaxWidth(Math.min(320, this.width - 40))
				.setCentered(true)
		);
		this.layout.addChild(Button.builder(historyButtonText(), button -> {
			ChatTweaksClient.setExtendedHistoryEnabled(!ChatTweaksClient.extendedHistoryEnabled());
			button.setMessage(historyButtonText());
		}).width(220).build());
		this.layout.addChild(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(220).build());
		this.layout.visitWidgets(this::addRenderableWidget);
		this.repositionElements();
	}

	private Component historyButtonText() {
		Component state = Component.translatable(
			ChatTweaksClient.extendedHistoryEnabled() ? "chattweaks.config.on" : "chattweaks.config.off"
		);
		return Component.translatable("chattweaks.config.extended_history", state);
	}

	@Override
	protected void repositionElements() {
		this.layout.arrangeElements();
		FrameLayout.centerInRectangle(this.layout, this.getRectangle());
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(this.parent);
	}
}
