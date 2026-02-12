package eu.packsolite.packsmod.command.impl;


import eu.packsolite.packsmod.command.DotCommand;

public class MsgCommand extends DotCommand {

	public MsgCommand() {
		super("msg");
		this.setAliases("whisper");
	}

	@Override
	public void execute(String[] args) {
		if (args.length < 2) {
			mod.print("§c.msg <player> [message]");
			return;
		}
		mod.getClient().sendWhisperMessage(args[0], String.join(" ", args).substring(args[0].length() + 1));
	}
}
