package net.worldoftomorrow.noitem.commands.frame;

import org.bukkit.command.CommandSender;

public abstract class NICommand {
	
	public final String command;
	public final String[] args;
	public final CommandSender sender;
	
	public NICommand(CommandSender sender, String command, String[] args) {
		this.command = command;
		this.args = args;
		this.sender = sender;
		if(checkCommand()) doCommand();
	}
	
	/**
	 * Executes the command
	 * @return Result of command
	 */
	public abstract boolean doCommand();
	
	/**
	 * Checks the validity of the commands arguments (if any)
	 * @return Validity of arguments
	 */
	public abstract boolean checkCommand();
}
