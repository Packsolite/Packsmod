package eu.packsolite.mixin;

import eu.packsolite.radio.MusicFeature;
import eu.packsolite.radio.player.MusicPlayer;
import eu.packsolite.radio.ui.RadioButtons;
import eu.packsolite.radio.ui.RadioSliderWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static eu.packsolite.radio.ui.RadioButtons.STATION_SELECT_BUTTON_WIDTH;

@Mixin({PauseScreen.class})
public class PauseScreenMixin extends net.minecraft.client.gui.screens.Screen {

	private static final int VOLUME_SLIDER_WIDTH = 120;
	private static final int VOLUME_SLIDER_HEIGHT = 20;
	private static final int BUTTON_PADDING = 2;

	protected PauseScreenMixin(Component title) {
		super(title);
	}

	@Inject(at = {@At("HEAD")}, method = {"createPauseMenu()V"})
	private void initWidgets(CallbackInfo info) {
		final int sliderX = this.width / 2 - VOLUME_SLIDER_WIDTH / 2;
		final MusicPlayer player = MusicFeature.INSTANCE.getMusicPlayer();

		RadioSliderWidget slider = new RadioSliderWidget(sliderX, BUTTON_PADDING, VOLUME_SLIDER_WIDTH, VOLUME_SLIDER_HEIGHT, player);
		Button previousButton = RadioButtons.previousStationButton(slider, sliderX - STATION_SELECT_BUTTON_WIDTH - BUTTON_PADDING, BUTTON_PADDING);
		Button nextButton = RadioButtons.nextStationButton(slider, sliderX + VOLUME_SLIDER_WIDTH + BUTTON_PADDING, BUTTON_PADDING);

		addRenderableWidget(previousButton);
		addRenderableWidget(nextButton);
		addRenderableWidget(slider);
	}
}