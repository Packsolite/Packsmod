package eu.packsolite.packsmod.command;

import eu.packsolite.packsmod.command.impl.*;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

public class CommandHandler {

	@Getter
	private final Collection<DotCommand> commands = List.of(
			new ChatCommand(),
			new IrcCommand(),
			new ListCommand(),
			new LocalChatCommand(),
			new MsgCommand(),
			new RespondCommand(),
			new HelpCommand()
	);

	public CommandHandler() {
		commands.forEach(cmd -> cmd.setCommandHandler(this));
	}

	/**
	 * Handles a command and returns true, if the message should be intercepted.
	 */
	public boolean handleCommand(String command, String[] args) {
		var optional = commands
				.stream()
				.filter(cmd -> cmd.matchesCommand(command))
				.findAny();

		optional.ifPresent(cmd -> cmd.execute(args));

		return optional.isPresent();
	}
}
