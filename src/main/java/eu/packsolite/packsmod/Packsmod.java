package eu.packsolite.packsmod;

import com.mojang.blaze3d.platform.InputConstants;
import eu.packsolite.packsmod.config.ConfigProvider;
import eu.packsolite.packsmod.config.ModConfig;
import eu.packsolite.packsmod.feature.radio.MusicFeature;
import eu.packsolite.packsmod.feature.smashmc.SmashMcFeature;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Packsmod implements ModInitializer {
	public static final String MOD_ID = "packsmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private KeyMapping configScreenKeyBinding;

	@Override
	public void onInitialize() {
		this.configScreenKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping("Packsmod options", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_P, KeyMapping.Category.MISC));
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (configScreenKeyBinding.consumeClick()) {
				openConfigScreen();
			}
			SmashMcFeature.INSTANCE.update();
		});

		MusicFeature.INSTANCE.init();
		LOGGER.info("(っ◕‿◕)っ Packs mod initialized!");
	}

	public void openConfigScreen() {
		Minecraft.getInstance().setScreen(ConfigProvider.getConfigScreen(null));
	}
}