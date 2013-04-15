package net.worldoftomorrow.noitem.features.defaults;

import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class Pickup extends NIFeature {

	public Pickup() {
		super("Pickup", "You are not allowed to pick up %i!", true);
		// TODO: Test feature
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent event) {
		if (event.isCancelled()) return;
		ItemStack item = event.getItem().getItemStack();
		Player p = event.getPlayer();
		event.getItem().setPickupDelay(200);
		event.setCancelled(true);
		this.doNotify(p, item);
	}
}
