package eu.packsolite.packsmod.feature.radio.ui;

import eu.packsolite.packsmod.feature.radio.MusicFeature;
import eu.packsolite.packsmod.feature.radio.player.MusicPlayer;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

import static net.minecraft.network.chat.Component.literal;

public class RadioButtons {

	public static final int STATION_SELECT_BUTTON_WIDTH = 20;
	public static final int STATION_SELECT_BUTTON_HEIGHT = 20;
	public static final int VOLUME_SLIDER_WIDTH = 120;
	public static final int VOLUME_SLIDER_HEIGHT = 20;
	public static final int BUTTON_PADDING = 2;

	private RadioButtons() {
	}

	public static Button nextStationButton(RadioSliderWidget slider, int x, int y) {
		return createButton(literal("»"), x, y, button -> {
			MusicFeature.INSTANCE.getMusicPlayer().nextStream();
			slider.updateMessage();
		});
	}

	public static Button previousStationButton(RadioSliderWidget slider, int x, int y) {
		return createButton(literal("«"), x, y, button -> {
			MusicFeature.INSTANCE.getMusicPlayer().prevStream();
			slider.updateMessage();
		});
	}

	public static Button mainMenuToggleButton(int buttonHeight, int buttonWidth, int x, int y) {
		MusicPlayer player = MusicFeature.INSTANCE.getMusicPlayer();
		Supplier<Component> getButtonText = () -> literal(player.isPlaying() ? "♬ On" : "♬ Off");
		return Button.builder(getButtonText.get(), button -> {
			player.toggle();
			button.setMessage(getButtonText.get());
		}).bounds(x, y, buttonWidth, buttonHeight).build();
	}

	private static Button createButton(Component text, int x, int y, Button.OnPress onPress) {
		return Button.builder(text, onPress).bounds(x, y, STATION_SELECT_BUTTON_WIDTH, STATION_SELECT_BUTTON_HEIGHT).build();
	}
}
