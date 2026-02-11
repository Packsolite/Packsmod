package eu.packsolite.packsmod.feature.radio;

import eu.packsolite.packsmod.feature.radio.player.MusicPlayer;
import lombok.Getter;

public enum MusicFeature {
	INSTANCE;

	@Getter
	private final MusicPlayer musicPlayer = new MusicPlayer();

	public void init() {
		musicPlayer.init();
	}
}
