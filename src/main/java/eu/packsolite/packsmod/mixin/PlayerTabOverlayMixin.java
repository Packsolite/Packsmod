package eu.packsolite.packsmod.mixin;

import eu.packsolite.packsmod.config.ConfigProvider;
import eu.packsolite.packsmod.config.ModConfig;
import eu.packsolite.packsmod.feature.smashmc.SmashApi;
import eu.packsolite.packsmod.feature.smashmc.SmashMcFeature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerTabOverlay.class)
class PlayerTabOverlayMixin {

	@Unique
	private static final ModConfig CONFIG = ConfigProvider.getConfig();


	/**
	 * private Component decorateName(final PlayerInfo info, final MutableComponent name) {
	 * return info.getGameMode() == GameType.SPECTATOR ? name.withStyle(ChatFormatting.ITALIC) : name;
	 * }
	 *
	 * @param playerInfo
	 * @param info
	 */

	@Inject(method = "decorateName", at = @At("RETURN"), cancellable = true)
	void decorateName(PlayerInfo playerInfo, MutableComponent name, CallbackInfoReturnable<Component> info) {
		// is known on smashmc?
		if (CONFIG.smashmc.playerPrefixes && !isOnSmashMC()) {
			var type = SmashMcFeature.INSTANCE.getApi().isCurrentlyKnownId(playerInfo.getProfile().id());
			if (type == SmashApi.IdType.KNOWN) {
				overridePrefix(info, name, "§8[§5SmashMC§8]");
			} else if (type == SmashApi.IdType.PENDING) {
				overridePrefix(info, name, "§8[§e...§8]");
			}
		}
	}

	@Unique
	private boolean isOnSmashMC() {
		var server = Minecraft.getInstance().getCurrentServer();
		if (server != null) {
			return server.ip.toLowerCase().contains("smashmc.eu") || server.name.contains("SmashMC.eu");
		}
		return false;
	}

	@Unique
	private void overridePrefix(
			CallbackInfoReturnable<Component> info,
			MutableComponent name,
			String prefixIn
	) {
		info.setReturnValue(Component.literal(prefixIn + " ").append(name));
	}
}