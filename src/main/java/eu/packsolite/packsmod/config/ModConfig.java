package eu.packsolite.packsmod.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "packsmod")
public class ModConfig implements ConfigData {

	/*
		Documentation: https://shedaniel.gitbook.io/cloth-config/auto-config/annotations
	 */
	@ConfigEntry.Gui.CollapsibleObject
	public RadioConfig radioConfig = new RadioConfig();

	public static class RadioConfig {
		@ConfigEntry.Gui.Tooltip
		public boolean resumeOnClientLaunch = true;
		@ConfigEntry.Gui.Excluded
		public int selectedStream;
		@ConfigEntry.Gui.Excluded
		public int volume;
		@ConfigEntry.Gui.Excluded
		public boolean playing;
	}
}