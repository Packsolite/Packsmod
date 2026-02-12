package eu.packsolite.packsmod.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import eu.packsolite.packsmod.config.ConfigProvider;
import eu.packsolite.packsmod.util.GlowUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
class LivingEntityMixin {

	@ModifyReturnValue(
			method = "isCurrentlyGlowing",
			at = @At("RETURN")
	)
	private boolean onIsCurrentlyGlowing(boolean original) {
		if ((Object) this instanceof Player player) {
			return original || GlowUtil.isGlowing(player) && ConfigProvider.getConfig().smashmc.glowReportedPlayers;
		}
		return original;
	}
}
