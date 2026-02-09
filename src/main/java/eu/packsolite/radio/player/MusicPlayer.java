package eu.packsolite.radio.player;

import eu.packsolite.Packsmod;
import jaco.mp3.player.MP3Player;
import lombok.Getter;
import lombok.Synchronized;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Arrays.asList;

public class MusicPlayer {
	private MP3Player player;
	private final List<MusicStream> streams = new ArrayList<>();
	private final ExecutorService radioDispatcher = Executors.newSingleThreadExecutor();

	@Getter
	private int selectedStreamIndex;
	private int playingStreamIndex;
	@Getter
	private boolean playing;
	@Getter
	private int volume;

	public MusicPlayer() {
		this.streams.addAll(asList(MusicStream.values()));
		this.playing = false;
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
			this.player = new MP3Player(new URL(stream.getStream()));
			this.player.setVolume(this.volume);
			this.player.play();
			this.playingStreamIndex = this.selectedStreamIndex;
			Packsmod.LOGGER.info("Radio is now playing stream #{}", playingStreamIndex);
		} catch (Exception e) {
			this.player = null;
			e.printStackTrace();
		}
	}

	private void stopPlaying() {
		var disposedPlayer = this.player;
		this.player = null;
		Thread.ofVirtual().start(disposedPlayer::stop); // no need to wait after disposal
	}

	public String toString() {
		return isPlaying() ? (getSelectedStream().getName() + " (" + volume + "%)") : "Radio off";
	}
}