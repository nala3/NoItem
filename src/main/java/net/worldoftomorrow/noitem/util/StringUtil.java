package net.worldoftomorrow.noitem.util;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StringUtil {

	public static String parseMsg(Player offender, String msg, Object o) {
		msg = msg.replace("%n", offender.getName());
		msg = msg.replace("%w", offender.getWorld().getName());
		msg = msg.replace("%x", String.valueOf(offender.getLocation().getBlockX()));
		msg = msg.replace("%y", String.valueOf(offender.getLocation().getBlockY()));
		msg = msg.replace("%z", String.valueOf(offender.getLocation().getBlockZ()));
		if(o instanceof ItemStack) {
			msg = msg.replace("%i", getStackName((ItemStack) o));
		} else if (o instanceof Block) {
			msg = msg.replace("%i", getBlockName((Block) o));
		} else if (o instanceof Entity) {
			msg = msg.replace("%i", getEntityName((Entity) o));
		} else if (o instanceof String) {
			msg = msg.replace("%i", (String) o);
		} else if (o instanceof Integer) {
			msg = msg.replace("%i", o.toString());
		}else {
			throw new UnsupportedOperationException("Invalid object given to parseMsg(): " + o.getClass().getSimpleName());
		}
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}
	
	public static String getStackName(ItemStack stack) {
		return stack.getType().name().replace("_", "").toLowerCase();
	}
	
	public static String getBlockName(Block b) {
		return b.getType().name().replace("_", "").toLowerCase();
	}
	
	public static String getEntityName(Entity e) {
		return e.getType().name().replace("_", "").toLowerCase();
	}
}
