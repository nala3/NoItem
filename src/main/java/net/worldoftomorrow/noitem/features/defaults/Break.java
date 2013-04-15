package net.worldoftomorrow.noitem.features.defaults;

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class Break extends NIFeature {

	public Break() {
		super("Break", "You are not allowed to break %i!", true);
		// TODO Test feature
	}

	@EventHandler
	public void handleBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled()) return;
		Player p = event.getPlayer();
		Block b = event.getBlock();
		if (NoItem.getPM().has(p, this, b)) {
			event.setCancelled(true);
			this.doNotify(p, b);
		}
	}
}
