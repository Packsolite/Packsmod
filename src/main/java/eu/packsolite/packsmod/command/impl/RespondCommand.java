package eu.packsolite.packsmod.command.impl;

import eu.packsolite.packsmod.command.DotCommand;

public class RespondCommand extends DotCommand {

	public RespondCommand() {
		super("respond");
		this.setAliases("r");
	}

	@Override
	public void execute(String[] args) {
		if (args.length < 1) {
			mod.print("§c.r [message]");
			return;
		}
		mod.getClient().sendWhisperMessage(mod.getLastWhisperTarget(), String.join(" ", args));
	}
}
