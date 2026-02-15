package eu.packsolite.packsmod.command.impl;

import java.io.File;

import static eu.packsolite.packsmod.command.BulkCommandExecutor.executeCommands;
import static eu.packsolite.packsmod.command.BulkCommandExecutor.stop;

public class BulkCommandsCommand extends eu.packsolite.packsmod.command.DotCommand {
	public BulkCommandsCommand() {
		super("commands");
	}

	@Override
	public void execute(String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("stop")) {
			stop();
			mod.print("§7Stopped execution");
			return;
		}
		if (args.length < 2) {
			mod.print("§c.commands <path-to-commands.txt> <delayms>");
			mod.print("§cor");
			mod.print("§c.commands stop");
			return;
		}

		File file = discoverFile(args[0]);
		if (!file.exists()) {
			mod.print("§cFile not found.");
			return;
		}
		long ms = Long.parseLong(args[1]);
		executeCommands(ms, file);
	}

	private File discoverFile(String name) {
		// First, try raw path
		File file = new File(name);
		// Then try on desktop
		if (!file.exists()) {
			String desktopPath = System.getProperty("user.home") + "\\" + "Desktop";
			file = new File(desktopPath + "\\" + name);
		}
		// Then try on desktop with .txt extension
		if (!file.exists()) {
			String desktopPath = System.getProperty("user.home") + "\\" + "Desktop";
			file = new File(desktopPath + "\\" + name + ".txt");
		}
		return file;
	}
}
