package eu.packsolite.packsmod.command.impl;

import eu.packsolite.packsmod.command.DotCommand;

public class HelpCommand extends DotCommand {
	public HelpCommand() {
		super("help");
		setAliases("h");
	}

	@Override
	public void execute(String[] args) {
		for (DotCommand command : getCommandHandler().getCommands()) {
			mod.print("." + command.getName());
		}
	}
}
