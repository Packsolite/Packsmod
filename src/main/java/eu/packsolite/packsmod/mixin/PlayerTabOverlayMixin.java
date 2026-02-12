package eu.packsolite.packsmod.mixin;

import eu.packsolite.packsmod.config.ConfigProvider;
import eu.packsolite.packsmod.config.ModConfig;
import eu.packsolite.packsmod.feature.smashmc.SmashApi;
import eu.packsolite.packsmod.feature.smashmc.SmashMcFeature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerTabOverlay.class)
class PlayerTabOverlayMixin {

	@Unique
	private static final ModConfig CONFIG = ConfigProvider.getConfig();

	@Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true)
	void getName(PlayerInfo playerInfo, CallbackInfoReturnable<Component> info) {
		// is known on smashmc?
		if (CONFIG.smashmc.playerPrefixes && !isOnSmashMC()) {
			var type = SmashMcFeature.INSTANCE.getApi().isCurrentlyKnownId(playerInfo.getProfile().id());
			if (type == SmashApi.IdType.KNOWN) {
				overridePrefix(info, playerInfo, "§8[§5SmashMC§8]");
			} else if (type == SmashApi.IdType.PENDING) {
				overridePrefix(info, playerInfo, "§8[§e...§8]");
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
			PlayerInfo playerInfo,
			String prefixIn
	) {
		StringBuilder prefix = new StringBuilder(prefixIn);

		// name color
		if (playerInfo.getTeam() != null) {
			prefix.append(playerInfo.getTeam().getColor());
			prefix.append(" ");
		} else {
			prefix.append("§f ");
		}

		// name
		if (playerInfo.getTabListDisplayName() != null) {
			info.setReturnValue(Component.literal(prefix.toString()).append(playerInfo.getTabListDisplayName()));
		} else {
			Component originalTabName = info.getReturnValue();
			info.setReturnValue(Component.literal(prefix.toString()).append(originalTabName));
		}
	}
}