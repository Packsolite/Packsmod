package eu.packsolite.packsmod.command;

import lombok.Getter;
import lombok.Setter;

import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

@Getter
public abstract class DotCommand {
	private final String name;
	private String[] aliases = new String[0];

	protected eu.packsolite.packsmod.command.SkidIrc mod = eu.packsolite.packsmod.command.SkidIrc.getInstance();

	@Setter
	private CommandHandler commandHandler;

	public DotCommand(String name) {
		this.name = name;
	}

	public void setAliases(String... aliases) {
		this.aliases = aliases;
	}

	public abstract void execute(String[] args);

	public boolean matchesCommand(String command) {
		return concat(of(name), of(aliases)).anyMatch(command::equalsIgnoreCase);
	}
}
