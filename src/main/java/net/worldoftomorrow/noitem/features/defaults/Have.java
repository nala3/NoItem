package net.worldoftomorrow.noitem.features.defaults;

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Have extends NIFeature {

	public Have() {
		super("Have", "You are not allowed to have %i!", true);
		// TODO Test feature
	}

	@EventHandler
	public void handleNoHaveItemHeld(PlayerItemHeldEvent event) {
		Player p = event.getPlayer();
		PlayerInventory inv = p.getInventory();
		ItemStack item = inv.getItem(event.getNewSlot());
		if (item.getTypeId() != 0 && NoItem.getPM().has(p, this, item)) {
			this.doNotify(p, item);
			inv.remove(item);
		}
	}

	@EventHandler
	public void handleNoHavePickup(PlayerPickupItemEvent event) {
		if (event.isCancelled()) return;
		ItemStack item = event.getItem().getItemStack();
		Player p = event.getPlayer();
		if (NoItem.getPM().has(p, this, item)) {
			event.getItem().setPickupDelay(200);
			event.setCancelled(true);
			this.doNotify(p, item);
		}
	}
}
