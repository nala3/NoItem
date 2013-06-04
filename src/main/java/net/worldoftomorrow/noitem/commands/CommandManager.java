package net.worldoftomorrow.noitem.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import net.worldoftomorrow.noitem.commands.frame.NICommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {

	private final HashMap<String, Class<NICommand>> commands = new HashMap<String, Class<NICommand>>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command,
			String[] args) {
		// If there are arguments and it is a registered NoItem command
		if (args.length > 0 && commands.containsKey(args[0])) {
			try {
				commands.get(args[0])
						.getConstructor(CommandSender.class, String.class, String.class)
						.newInstance(sender, command, removeArg(args));
			} catch (NoSuchMethodException | SecurityException
					| InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	// Add commands; version; reload; check; add/remove list item; set config
	// options;

	/**
	 * Removes the first item from an array
	 * @param orig
	 * @return A copy of the original array with the first item removed	
	 */
	public static String[] removeArg(String[] orig) {
		String[] nu = new String[orig.length - 1];
		System.arraycopy(orig, 1, nu, 0, orig.length - 1);
		return nu;
	}
}
