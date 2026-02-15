package eu.packsolite.packsmod.command;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;

public class BulkCommandExecutor {
	static boolean stop;

	public static void stop() {
		stop = true;
	}

	public static void executeCommands(long delayMs, File file) {
		stop = false;
		new Thread(() -> {
			try {
				BufferedReader br = Files.newBufferedReader(file.toPath());
				String line;
				while ((line = br.readLine()) != null && !stop) {
					if (!line.isBlank()) {
						eu.packsolite.packsmod.command.SkidIrc.getInstance().sendMinecraftChatMessage(line);
					}
					Thread.sleep(delayMs);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}
}
