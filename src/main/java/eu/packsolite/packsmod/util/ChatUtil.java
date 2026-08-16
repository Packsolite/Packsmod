package eu.packsolite.packsmod.util;

import net.minecraft.client.Minecraft;

import static net.minecraft.network.chat.Component.literal;

public class ChatUtil {

	private static final String FORMAT = "[Packsmod] %s";

	public static void display(String message) {
		displayRaw(FORMAT.formatted(message));
	}

	public static void displayRaw(String message) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		Minecraft.getInstance().execute(() -> player.sendSystemMessage(literal(message)));
	}
}
