package eu.packsolite.radio.ui;


import eu.packsolite.radio.player.MusicPlayer;
import net.minecraft.client.gui.components.AbstractSliderButton;

import static java.lang.String.valueOf;
import static net.minecraft.network.chat.Component.literal;

public class RadioSliderWidget extends AbstractSliderButton {
	private final MusicPlayer player;

	public RadioSliderWidget(int x, int y, int width, int height, MusicPlayer player) {
		super(x, y, width, height, literal(player.toString()), player.getVolume() / 100.0D);
		this.player = player;
	}

	@Override
	public void updateMessage() {
		setMessage(literal(valueOf(player.toString())));
	}

	@Override
	protected void applyValue() {
		player.setVolume((int) (this.value * 100D));
	}
}