package eu.packsolite.packsmod.mixin;

import eu.packsolite.packsmod.feature.radio.ui.RadioButtons;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TitleScreen.class})
class TitleScreenMixin extends Screen {

	protected TitleScreenMixin(Component component) {
		super(component);
	}

	@Inject(at = {@At("TAIL")}, method = {"init()V"})
	void init(CallbackInfo info) {
		int buttonHeight = 15;
		int buttonWidth = 40;
		int x = this.width / 2 - buttonWidth / 2;
		int y = this.height - buttonHeight - 10;
		Button musicButton = RadioButtons.mainMenuToggleButton(buttonHeight, buttonWidth, x, y);
		addRenderableWidget(musicButton);
	}
}
