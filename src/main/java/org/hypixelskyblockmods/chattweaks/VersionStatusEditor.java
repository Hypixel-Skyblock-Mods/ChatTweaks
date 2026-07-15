package org.hypixelskyblockmods.chattweaks;

import io.github.notenoughupdates.moulconfig.common.RenderContext;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.gui.GuiOptionEditor;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;

public final class VersionStatusEditor extends GuiOptionEditor {
	public VersionStatusEditor(ProcessedOption option) {
		super(option);
	}

	@Override
	public void render(RenderContext context, int x, int y, int width) {
		ChatTweaksUpdateChecker.Snapshot status = ChatTweaksUpdateChecker.snapshot();
		var font = context.getMinecraft().getDefaultFontRenderer();

		context.drawDarkRect(x, y, width, getHeight(), true);
		context.drawStringCenteredScaledMaxWidth(
			StructuredText.of("Current version: " + status.currentVersion()),
			font,
			x + width / 2F,
			y + 16F,
			true,
			width - 12,
			0xE0E0E0
		);
		context.drawStringCenteredScaledMaxWidth(
			StructuredText.of(status.message()),
			font,
			x + width / 2F,
			y + 36F,
			true,
			width - 12,
			status.color()
		);
	}

	@Override
	public int getHeight() {
		return 52;
	}

	@Override
	public boolean fulfillsSearch(String word) {
		return super.fulfillsSearch(word) || "version update latest github".contains(word);
	}
}
