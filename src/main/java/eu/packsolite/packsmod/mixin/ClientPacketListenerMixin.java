package eu.packsolite.packsmod.mixin;

import eu.packsolite.packsmod.command.SkidIrc;
import eu.packsolite.packsmod.config.ConfigProvider;
import eu.packsolite.packsmod.feature.smashmc.SmashMcFeature;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
class ClientPacketListenerMixin {

	@Inject(method = "handleSystemChat", at = @At("HEAD"))
	void handleSystemChat(ClientboundSystemChatPacket clientboundSystemChatPacket, CallbackInfo ci) {
		SmashMcFeature.INSTANCE.handleSystemChat(clientboundSystemChatPacket.content());
	}

	@Inject(method = "sendChat", at = @At("HEAD"), cancellable = true)
	void onSendChatMessage(String message, CallbackInfo ci) {
		if (message.startsWith(".") && ConfigProvider.getConfig().irc.enabled) {
			ci.cancel();
			String command = message.substring(1, message.split(" ")[0].length());
			String[] arguments = message.length() > command.length() + 1
					? message.substring(message.split(" ")[0].length() + 1).split(" ")
					: new String[0];
			SkidIrc.getInstance().getCommandHandler().handleCommand(command, arguments);
		}
	}
}