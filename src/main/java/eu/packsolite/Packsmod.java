package eu.packsolite;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Packsmod implements ModInitializer {
	public static final String MOD_ID = "packsmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("(っ◕‿◕)っ Packs mod says hello!");
	}
}