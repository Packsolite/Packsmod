package eu.packsolite.packsmod.command.impl;


import de.liquiddev.ircclient.client.IrcPlayer;
import eu.packsolite.packsmod.command.DotCommand;

import java.util.Collection;

public class ListCommand extends DotCommand {

	public ListCommand() {
		super("list");
	}

	@Override
	public void execute(String[] args) {
		for (IrcPlayer player : (Collection<IrcPlayer>) IrcPlayer.listPlayers()) {
			mod.print(player.getIrcNick() + " - " + player.getClientName() + " - " + player.getExtra());
		}
		mod.print("count: " + IrcPlayer.listPlayers().size());
	}
}
