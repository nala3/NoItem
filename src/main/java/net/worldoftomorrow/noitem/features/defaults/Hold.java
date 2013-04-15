package net.worldoftomorrow.noitem.features.defaults;

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Hold extends NIFeature {

	public Hold() {
		super("Hold", "You are not allowed to hold %i!", true);
		// TODO Auto-generated constructor stub
	}

	@EventHandler
	public void handleNoHoldInvClick(InventoryClickEvent event) {
		if (event.isCancelled()) return;
		ItemStack cursor = event.getCursor();
		Player p = getPlayerFromEntity(event.getWhoClicked());
		PlayerInventory inv = p.getInventory();
		if (cursor.getTypeId() != 0 && event.getSlotType() == SlotType.QUICKBAR
				&& event.getSlot() == inv.getHeldItemSlot()) {
			if (NoItem.getPM().has(p, this, cursor)) {
				event.setCancelled(true);
				this.doNotify(p, cursor);
			}
		}
	}

	@EventHandler
	public void handleNoHoldPickup(PlayerPickupItemEvent event) {
		if (event.isCancelled()) return;
		ItemStack item = event.getItem().getItemStack();
		Player p = event.getPlayer();
		PlayerInventory inv = p.getInventory();
		if (inv.firstEmpty() == inv.getHeldItemSlot() && NoItem.getPM().has(p, this, item)) {
			event.getItem().setPickupDelay(200);
			event.setCancelled(true);
			this.doNotify(p, item);
		}
	}

	private Player getPlayerFromEntity(HumanEntity ent) {
		return Bukkit.getPlayer(ent.getName());
	}
}