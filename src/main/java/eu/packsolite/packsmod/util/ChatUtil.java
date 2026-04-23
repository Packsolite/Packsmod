package eu.packsolite.packsmod.util;

import net.minecraft.client.Minecraft;

import static net.minecraft.network.chat.Component.literal;

public class ChatUtil {

	public static void display(String message) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		Minecraft.getInstance().execute(() -> player.sendSystemMessage(literal(message)));
	}
}
