package eu.packsolite.packsmod.command.impl;

import eu.packsolite.packsmod.command.DotCommand;

public class ChatCommand extends DotCommand {
	public ChatCommand() {
		super("chat");
		this.setAliases("c");
	}

	@Override
	public void execute(String[] args) {
		mod.getClient().sendChatMessage(String.join(" ", args));
	}
}
