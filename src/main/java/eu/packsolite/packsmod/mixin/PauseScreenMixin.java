package eu.packsolite.packsmod.mixin;

import eu.packsolite.packsmod.feature.radio.MusicFeature;
import eu.packsolite.packsmod.feature.radio.player.MusicPlayer;
import eu.packsolite.packsmod.feature.radio.ui.RadioButtons;
import eu.packsolite.packsmod.feature.radio.ui.RadioSliderWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static eu.packsolite.packsmod.feature.radio.ui.RadioButtons.*;

@Mixin({PauseScreen.class})
class PauseScreenMixin extends net.minecraft.client.gui.screens.Screen {

	protected PauseScreenMixin(Component title) {
		super(title);
	}

	@Inject(at = {@At("HEAD")}, method = {"createPauseMenu()V"})
	void initWidgets(CallbackInfo info) {
		int sliderX = this.width / 2 - VOLUME_SLIDER_WIDTH / 2;
		MusicPlayer player = MusicFeature.INSTANCE.getMusicPlayer();

		RadioSliderWidget slider = new RadioSliderWidget(sliderX, BUTTON_PADDING, VOLUME_SLIDER_WIDTH, VOLUME_SLIDER_HEIGHT, player);
		Button previousButton = RadioButtons.previousStationButton(slider, sliderX - STATION_SELECT_BUTTON_WIDTH - BUTTON_PADDING, BUTTON_PADDING);
		Button nextButton = RadioButtons.nextStationButton(slider, sliderX + VOLUME_SLIDER_WIDTH + BUTTON_PADDING, BUTTON_PADDING);

		addRenderableWidget(previousButton);
		addRenderableWidget(nextButton);
		addRenderableWidget(slider);
	}
}