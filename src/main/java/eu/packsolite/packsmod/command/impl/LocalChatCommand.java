package eu.packsolite.packsmod.command.impl;


import eu.packsolite.packsmod.command.DotCommand;

public class LocalChatCommand extends DotCommand {

	public LocalChatCommand() {
		super("local");
		this.setAliases("l", "lc");
	}

	@Override
	public void execute(String[] args) {
		mod.getClient().sendLocalChatMessage(String.join(" ", args));
	}
}
