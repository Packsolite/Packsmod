package eu.packsolite.packsmod.config;

import com.google.common.base.Suppliers;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.AutoConfigClient;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Supplier;

public class ConfigProvider {

	private static final Supplier<ConfigHolder<ModConfig>> CONFIG_HOLDER_SUPPLIER = Suppliers.memoize(() -> AutoConfig.getConfigHolder(ModConfig.class));

	public static ConfigHolder<ModConfig> getConfigHolder() {
		return CONFIG_HOLDER_SUPPLIER.get();
	}

	public static ModConfig getConfig() {
		return getConfigHolder().getConfig();
	}

	public static Screen getConfigScreen(Screen parent) {
		return AutoConfigClient.getConfigScreen(ModConfig.class, parent).get();
	}
}
