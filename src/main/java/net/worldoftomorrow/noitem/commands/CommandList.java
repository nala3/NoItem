 
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

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.FeatureManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandList extends CommandClass {
	
	/*
	 * TODO: These methods are poorly implemented. They should be able to add and remove
	 * a single value from the configurations.
	 */
	
	//list <list> set any.path.you.want ("some string value"|value)
	@Command(syntax = "list \\w+ set \\S+\\s(\".*\"|\\S+)", perms = { "noitem.command.list.*", "noitem.command.list.set", "noitem.command.list" })
	public static void listSet(CommandSender sender, String[] args) {
		if(args[1].equalsIgnoreCase("armor")) {
			NoItem.getLists().getArmorList().setValue(args[3], args[4]);
		} else if (args[1].equalsIgnoreCase("tools")) {
			NoItem.getLists().getToolList().setValue(args[3], args[4]);
		} else {
			sender.sendMessage("Invalid list name. The options are Armor or Tools.");
		}
	}
	
	@Command(syntax = "list \\w+ remove \\S+", perms = { "noitem.command.list.*", "noitem.command.list.remove", "noitem.command.list" })
	public static void listRemove(CommandSender sender, String[] args) {
		if(args[1].equalsIgnoreCase("armor")) {
			NoItem.getLists().getArmorList().setValue(args[3], "");
		} else if (args[1].equalsIgnoreCase("tools")) {
			NoItem.getLists().getToolList().setValue(args[3], "");
		} else {
			sender.sendMessage("Invalid list name. The options are Armor or Tools.");
		}
	}
	
	@Command(syntax="check \\w+", perms = {"noitem.command.check.*", "noitem.command.check.player", "noitem.command.check"})
	public static void checkSingle(CommandSender sender, String[] args) {
		Player p = NoItem.getInstance().getServer().getPlayer(args[1]);
		if(p == null) {
			sender.sendMessage("Could not find a player with that name!");
		} else {
			FeatureManager.getInstance().check(p);
		}
	}
	
	@Command(syntax="check all", perms = {"noitem.command.check.*", "noitem.command.check.all", "noitem.command.check"})
	public static void checkAll(CommandSender sender, String[] args) {
		for(Player p : NoItem.getInstance().getServer().getOnlinePlayers()) {
			FeatureManager.getInstance().check(p);
		}
	}
}
