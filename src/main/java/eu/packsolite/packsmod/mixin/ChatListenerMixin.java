package eu.packsolite.packsmod.mixin;

import eu.packsolite.packsmod.feature.smashmc.SmashMcFeature;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
class ChatListenerMixin {

	@Inject(
			method = "handleSystemChat",
			at = @At("HEAD")
	)
	void handleSystemChat(ClientboundSystemChatPacket clientboundSystemChatPacket, CallbackInfo ci) {
		SmashMcFeature.INSTANCE.handleSystemChat(clientboundSystemChatPacket.content());
	}
}