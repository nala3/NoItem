package net.worldoftomorrow.noitem.commands.frame;

import org.bukkit.command.ConsoleCommandSender;

public abstract class ConsoleCommand extends NICommand {

	private final ConsoleCommandSender ccs;
	
	public ConsoleCommand(ConsoleCommandSender ccs, String command, String[] args) {
		super(ccs, command, args);
		this.ccs = ccs;
	}
	
	public ConsoleCommandSender getConsoleSender() {
		return ccs;
	}
}
