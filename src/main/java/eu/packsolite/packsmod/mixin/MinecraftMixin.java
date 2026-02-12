package eu.packsolite.packsmod.mixin;

import eu.packsolite.packsmod.command.SkidIrc;
import eu.packsolite.packsmod.config.ConfigProvider;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
class MinecraftMixin {

	@Inject(method = "<init>*", at = @At("RETURN"))
	void onConstructed(CallbackInfo ci) {
		if (ConfigProvider.getConfig().irc.enabled) {
			SkidIrc.getInstance().enable();
		}
	}
}
