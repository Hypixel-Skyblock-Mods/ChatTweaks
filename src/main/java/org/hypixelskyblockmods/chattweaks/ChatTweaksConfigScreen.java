package org.hypixelskyblockmods.chattweaks;

import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorKeybind;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOrder;
import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor;
import io.github.notenoughupdates.moulconfig.processor.ConfigProcessorDriver;
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor;
import org.lwjgl.glfw.GLFW;

public final class ChatTweaksConfigScreen {
	private ChatTweaksConfigScreen() {
	}

	public static void open() {
		ChatTweaksUpdateChecker.refresh(false);
		ChatTweaksMoulConfig config = new ChatTweaksMoulConfig();
		MoulConfigProcessor<ChatTweaksMoulConfig> processor = MoulConfigProcessor.withDefaults(config);
		processor.registerConfigEditor(ConfigVersionStatus.class, (option, ignored) -> new VersionStatusEditor(option));
		ConfigProcessorDriver driver = new ConfigProcessorDriver(processor);
		driver.checkExpose = false;
		driver.processConfig(config);

		MoulConfigEditor<ChatTweaksMoulConfig> editor = new MoulConfigEditor<>(processor);
		IMinecraft.INSTANCE.openWrappedScreen(editor);
	}

	public static final class ChatTweaksMoulConfig extends Config {
		@Category(name = "Dashboard", desc = "ChatTweaks version and update status")
		public final DashboardCategory dashboard = new DashboardCategory();

		@Category(name = "Chat", desc = "ChatTweaks chat settings")
		public final ChatCategory chat = new ChatCategory();

		public ChatTweaksMoulConfig() {
			this.chat.chatPeek = ChatTweaksClient.chatPeekEnabled();
			this.chat.peekKey = ChatTweaksClient.peekKeyCode();
			this.chat.scrollChatPeek = ChatTweaksClient.peekScrollingEnabled();
			this.chat.extendedChatHistory = ChatTweaksClient.extendedHistoryEnabled();
			this.saveRunnables.add(() -> {
				ChatTweaksClient.setChatPeekEnabled(this.chat.chatPeek);
				ChatTweaksClient.setPeekKeyCode(this.chat.peekKey);
				ChatTweaksClient.setPeekScrollingEnabled(this.chat.scrollChatPeek);
				ChatTweaksClient.setExtendedHistoryEnabled(this.chat.extendedChatHistory);
			});
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

	public static final class DashboardCategory {
		@ConfigOrder(-20)
		@ConfigOption(
			name = "Version",
			desc = "Shows the installed ChatTweaks version and compares it with the latest GitHub release."
		)
		@ConfigVersionStatus
		public transient String versionStatus = "";

		@ConfigOrder(-10)
		@ConfigOption(
			name = "Update Check",
			desc = "Check GitHub Releases again for a newer stable ChatTweaks version."
		)
		@ConfigEditorButton(buttonText = "Check again")
		public transient Runnable checkForUpdates = () -> ChatTweaksUpdateChecker.refresh(true);
	}

	public static final class ChatCategory {
		@ConfigOrder(-30)
		@ConfigOption(
			name = "Chat Peek",
			desc = "Enable holding the Peek Chat key to view chat without opening it."
		)
		@ConfigEditorBoolean
		public boolean chatPeek = true;

		@ConfigOrder(-20)
		@ConfigOption(
			name = "Peek Chat Key",
			desc = "The same binding shown in Minecraft's Controls menu."
		)
		@ConfigEditorKeybind(defaultKey = GLFW.GLFW_KEY_Z)
		public int peekKey = GLFW.GLFW_KEY_Z;

		@ConfigOrder(-10)
		@ConfigOption(
			name = "Scroll While Peeking",
			desc = "Allow the mouse wheel to scroll chat while the Peek Chat key is held."
		)
		@ConfigEditorBoolean
		public boolean scrollChatPeek = true;

		@ConfigOption(
			name = "Extended Chat History",
			desc = "Keep up to 1,000 chat messages instead of Minecraft's default 100."
		)
		@ConfigEditorBoolean
		public boolean extendedChatHistory = true;
	}
}
