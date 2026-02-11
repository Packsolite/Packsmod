package eu.packsolite.radio;

import eu.packsolite.radio.player.MusicPlayer;
import lombok.Getter;

public enum MusicFeature {
	INSTANCE;

	@Getter
	private final MusicPlayer musicPlayer = new MusicPlayer();

	public void init() {
		musicPlayer.init();
	}
}
