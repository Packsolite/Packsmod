package eu.packsolite.packsmod.command.impl;

import eu.packsolite.packsmod.command.DotCommand;
import eu.packsolite.packsmod.command.SkidIrc;
import eu.packsolite.packsmod.feature.smashmc.StatusCustomPayload;
import io.github.killergerbah.jsubtitle.srt.SrtSubtitle;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StatusCommand extends DotCommand {

	Timer timer;

	public StatusCommand() {
		super("status");
		this.setAliases("s");
	}

	@Override
	public void execute(String[] args) {
		if (args.length < 1) {
			mod.print("§c.status <subtitle-file>");
			return;
		}

		if (timer != null) {
			timer.cancel();
			timer.purge();
		}

		if (args[0].equalsIgnoreCase("stop")) {
			reset();
			return;
		}

		timer = new Timer();

		File file = new File(String.join(" ", args));

		if (!file.exists()) {
			SkidIrc.getInstance().addChat("§cFile not found: " + file.getAbsolutePath());
			return;
		}

		playSubtitleFile(file);
	}

	private void playSubtitleFile(File file) {
		try {
			List<SrtSubtitle> subtitles = SrtSubtitle.read(file);

			long lastEnd = 0;
			for (var subtitle : subtitles) {
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						updateSubtitle(subtitle.getText());
					}
				}, subtitle.getStart());
				if (subtitle.getEnd() > lastEnd) {
					lastEnd = subtitle.getEnd();
				}
			}

			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					reset();
				}
			}, lastEnd);


		} catch (IOException e) {
			e.printStackTrace();
			SkidIrc.getInstance().addChat("§cError parsing subtitle file");
		}
	}

	private void updateSubtitle(String text) {
		StatusCustomPayload payload = new StatusCustomPayload(0, "§d" + text);
		ClientPlayNetworking.send(payload);
	}

	private void reset() {
		StatusCustomPayload payload = new StatusCustomPayload(1, null);
		ClientPlayNetworking.send(payload);
	}
}
