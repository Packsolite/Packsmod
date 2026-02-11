package eu.packsolite.packsmod.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.AutoConfigClient;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.client.gui.screens.Screen;

public class ConfigProvider {

	public static ConfigHolder<ModConfig> getConfigHolder() {
		return AutoConfig.getConfigHolder(ModConfig.class);
	}

	public static ModConfig getConfig() {
		return getConfigHolder().getConfig();
	}

	public static Screen getConfigScreen(Screen parent) {
		return AutoConfigClient.getConfigScreen(ModConfig.class, parent).get();
	}
}
