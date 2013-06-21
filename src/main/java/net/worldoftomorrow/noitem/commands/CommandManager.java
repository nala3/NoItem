
/* Copyright (c) 2013, Worldoftomorrow.net
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies, 
 * either expressed or implied, of the FreeBSD Project.
 */

package net.worldoftomorrow.noitem.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import net.worldoftomorrow.noitem.Logging;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {
	
	private static final HashMap<Pattern, Entry<String[], Method>> commands = new HashMap<Pattern, Entry<String[], Method>>();
	
	public CommandManager() {
		this.registerCommandClass(CommandList.class);
	}
	
	public void registerCommandClass(Class<? extends CommandClass> cc) {
		for(Method m : cc.getDeclaredMethods()) {
			if(m.isAnnotationPresent(Command.class)) {
				Command anot = m.getAnnotation(Command.class);
				Pattern pat = Pattern.compile(anot.syntax(), Pattern.CASE_INSENSITIVE);
				commands.put(pat, new AbstractMap.SimpleImmutableEntry<String[], Method>(anot.perms(), m));
			}
		}
	}
	
	public void exectuteCommand(CommandSender sender, String[] args) {
		Entry<String[], Method> cmd = getCommandEntry(args);
		if(cmd == null) {
			sender.sendMessage("Invalid command syntax!");
			return;
		}
		if(hasCommandPermission(sender, cmd)) {
			try {
				cmd.getValue().invoke(null, sender, args);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				Logging.severe("A command method is not properly written! Please report this!");
				e.printStackTrace();
			}
		} else {
			sender.sendMessage("You do not have permission to run this command!");
		}
	}
	
	private Entry<String[], Method> getCommandEntry(String[] args) {
		StringBuilder builder = new StringBuilder();
		for(String arg : args) {
			builder.append(arg + " ");
		}
		for(Pattern pat : commands.keySet()) {
			if(pat.matcher(builder.toString().trim()).matches()) {
				return commands.get(pat);
			}
		}
		return null;
	}
	
	private boolean hasCommandPermission(CommandSender sender, Entry<String[], Method> entry) {
		for(String perm : entry.getKey()) {
			if(sender.hasPermission(perm)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd,
			String label, String[] args) {
		this.exectuteCommand(sender, args);
		return true;
	}
}
