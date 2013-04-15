package net.worldoftomorrow.noitem.features.defaults;

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class Drop extends NIFeature {

	public Drop() {
		super("Drop", "You are not allowed to drop %i!", true);
		// TODO Test feature
	}

	@EventHandler
	public void handleItemDrop(PlayerDropItemEvent event) {
		if (event.isCancelled()) return;
		ItemStack drop = event.getItemDrop().getItemStack();
		Player p = event.getPlayer();
		if (NoItem.getPM().has(p, this, drop)) {
			event.setCancelled(true);
			this.doNotify(p, drop);
		}
	}
}
