package net.worldoftomorrow.noitem.commands;

import org.bukkit.command.CommandSender;

import net.worldoftomorrow.noitem.commands.frame.NICommand;

public class List extends NICommand {

	public List(CommandSender sender, String command, String[] args) {
		super(sender, command, args);
	}

	@Override
	public boolean doCommand() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkCommand() {
		if(args.length == 4 || args.length == 3) {
			
		}
		// TODO Auto-generated method stub
		return false;
	}

}
