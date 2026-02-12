package eu.packsolite.packsmod.command.impl;


import eu.packsolite.packsmod.command.DotCommand;

public class IrcCommand extends DotCommand {

	public IrcCommand() {
		super("irc");
	}

	@Override
	public void execute(String[] args) {
		mod.getClient().executeCommand(String.join(" ", args));
	}
}
