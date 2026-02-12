package eu.packsolite.packsmod.util;

import net.minecraft.world.entity.player.Player;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GlowUtil {

	private static final Set<String> GLOWING_PLAYERS = ConcurrentHashMap.newKeySet();

	public static void glowPlayer(String playerName) {
		GLOWING_PLAYERS.add(playerName);
	}

	public static void removeGlow(String playerName) {
		GLOWING_PLAYERS.remove(playerName);
	}

	public static boolean isGlowing(Player player) {
		return GLOWING_PLAYERS.contains(player.getGameProfile().name());
	}
}
