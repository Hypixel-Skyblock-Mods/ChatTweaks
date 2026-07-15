package org.hypixelskyblockmods.chattweaks;

import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor;
import io.github.notenoughupdates.moulconfig.processor.ConfigProcessorDriver;
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor;

public final class ChatTweaksConfigScreen {
	private ChatTweaksConfigScreen() {
	}

	public static void open() {
		ChatTweaksMoulConfig config = new ChatTweaksMoulConfig();
		MoulConfigProcessor<ChatTweaksMoulConfig> processor = MoulConfigProcessor.withDefaults(config);
		ConfigProcessorDriver driver = new ConfigProcessorDriver(processor);
		driver.checkExpose = false;
		driver.processConfig(config);

		MoulConfigEditor<ChatTweaksMoulConfig> editor = new MoulConfigEditor<>(processor);
		IMinecraft.INSTANCE.openWrappedScreen(editor);
	}

	public static final class ChatTweaksMoulConfig extends Config {
		@Category(name = "Chat", desc = "ChatTweaks chat settings")
		public final ChatCategory chat = new ChatCategory();

		public ChatTweaksMoulConfig() {
			this.chat.extendedChatHistory = ChatTweaksClient.extendedHistoryEnabled();
			this.saveRunnables.add(() ->
				ChatTweaksClient.setExtendedHistoryEnabled(this.chat.extendedChatHistory)
			);
		}

		@Override
		public StructuredText getTitle() {
			return StructuredText.of("ChatTweaks");
		}

		@Override
		public boolean isValidRunnable(int runnableId) {
			return false;
		}
	}

	public static final class ChatCategory {
		@ConfigOption(
			name = "Extended Chat History",
			desc = "Keep up to 1,000 chat messages instead of Minecraft's default 100."
		)
		@ConfigEditorBoolean
		public boolean extendedChatHistory = true;
	}
}
