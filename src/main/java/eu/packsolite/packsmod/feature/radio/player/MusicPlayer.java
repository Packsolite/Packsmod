package eu.packsolite.packsmod.feature.radio.player;

import eu.packsolite.packsmod.Packsmod;
import eu.packsolite.packsmod.config.ConfigProvider;
import jaco.mp3.player.MP3Player;
import lombok.Getter;
import lombok.Synchronized;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Arrays.asList;

public class MusicPlayer {
	private final List<MusicStream> streams = new ArrayList<>();
	private final ExecutorService radioDispatcher = Executors.newSingleThreadExecutor();

	@Getter
	private int selectedStreamIndex;
	@Getter
	private boolean playing;
	@Getter
	private int volume;

	private int playingStreamIndex;
	private MP3Player player;

	public MusicPlayer() {
		this.streams.addAll(asList(MusicStream.values()));
	}

	public void init() {
		var config = ConfigProvider.getConfig();
		this.playing = config.radioConfig.resumeOnClientLaunch && config.radioConfig.playing;
		this.volume = config.radioConfig.volume;
		this.selectedStreamIndex = config.radioConfig.selectedStream;
		this.updatePlayerState();
	}

	@Synchronized
	public void nextStream() {
		this.selectedStreamIndex = (selectedStreamIndex + 1) % streams.size();
		updatePlayerState();
	}

	@Synchronized
	public void prevStream() {
		this.selectedStreamIndex = selectedStreamIndex - 1;
		if (selectedStreamIndex < 0) {
			selectedStreamIndex = streams.size() - 1;
		}
		updatePlayerState();
	}

	public MusicStream getSelectedStream() {
		return this.streams.get(this.selectedStreamIndex % this.streams.size());
	}

	public void setVolume(int volume) {
		if (volume == 0) {
			this.playing = false;
			updatePlayerState();
			return;
		}

		if (volume != this.volume) {
			this.volume = volume;
			this.playing = true;
			updatePlayerState();
		}
	}

	/**
	 * Updates the ui state to the underlying actual mp3 player on a separate thread to avoid ui freezes.
	 */
	private void updatePlayerState() {
		radioDispatcher.execute(() -> {
			if (playingStreamIndex != selectedStreamIndex) {
				if (player != null) {
					stopPlaying(); // force a restart
				}
			}

			if (playing) {
				if (player == null) {
					startPlaying();
				}
				if (player.getVolume() != volume) {
					player.setVolume(volume);
				}
			} else if (player != null) {
				stopPlaying();
			}
		});
	}

	private void startPlaying() {
		MusicStream stream = getSelectedStream();
		try {
			this.player = new MP3Player(URI.create(stream.getStream()).toURL());
			this.player.setVolume(this.volume);
			this.player.play();
			this.playingStreamIndex = this.selectedStreamIndex;
			var configHolder = ConfigProvider.getConfigHolder();
			configHolder.getConfig().radioConfig.playing = true;
			configHolder.getConfig().radioConfig.volume = volume;
			configHolder.getConfig().radioConfig.selectedStream = playingStreamIndex;
			configHolder.save();
			Packsmod.LOGGER.info("Radio is now playing stream #{}", playingStreamIndex);
		} catch (Exception e) {
			this.player = null;
			Packsmod.LOGGER.error("Error starting radio with stream {}", stream, e);
		}
	}

	private void stopPlaying() {
		var disposedPlayer = this.player;
		this.player = null;
		var configHolder = ConfigProvider.getConfigHolder();
		configHolder.getConfig().radioConfig.playing = false;
		configHolder.save();
		Thread.ofVirtual().start(disposedPlayer::stop); // no need to wait after disposal
	}

	public String toString() {
		return playing ? (getSelectedStream().getName() + " (" + volume + "%)") : "Radio off";
	}
}