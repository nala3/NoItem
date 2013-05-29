package net.worldoftomorrow.noitem.features;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class CheckableNIFeature extends NIFeature {

	public CheckableNIFeature(String name, String message, boolean notify) {
		super(name, message, notify);
	}
	
	public void checkAll() {
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			this.check(p);
		}
	}
	
	public void checkWorld(World w) {
		for(Player p : w.getPlayers()) {
			this.check(p);
		}
	}
	
	/**
	 * This method is used to check every player for compliance to this feature.
	 */
	public abstract void check(Player p);

}
