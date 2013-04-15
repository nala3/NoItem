package net.worldoftomorrow.noitem.features.defaults;

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class Place extends NIFeature {

	public Place() {
		super("Place", "You are not allowed to place %i!", true);
		// TODO Test feature
	}

	@EventHandler
	public void handleBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled()) return;
		Player p = event.getPlayer();
		Block b = event.getBlock();
		if (NoItem.getPM().has(p, this, b)) {
			event.setCancelled(true);
			this.doNotify(p, b);
		}
	}
}
