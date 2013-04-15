package net.worldoftomorrow.noitem.features.defaults;

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.CheckableNIFeature;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class Wear extends CheckableNIFeature {

	public Wear() {
		super("Wear", "You are not allowed to wear %i!", true);
		// TODO Test feature + implement armor list again
	}

	public void handleWearInvClick(InventoryClickEvent event) {
		if (event.isCancelled()) return;
		InventoryView view = event.getView();
		SlotType st = event.getSlotType();
		int slot = event.getRawSlot();
		if (view.getType() == InventoryType.CRAFTING && slot != -999) {
			ItemStack cursor = event.getCursor();
			ItemStack current = event.getCurrentItem();
			Player p = getPlayerFromEntity(event.getWhoClicked());
			// Handle direct clicking
			if (st == SlotType.ARMOR && cursor.getTypeId() != 0) {
				if (/* NoItem.getLists().isArmor(cursor.getTypeId()) && */NoItem.getPM().has(p, this, cursor)) {
					event.setCancelled(true);
					this.doNotify(p, cursor);
				}
				// Handle shift clicking
			} else if (st != SlotType.ARMOR && current.getTypeId() != 0 && event.isShiftClick()) {
				if (/* NoItem.getLists().isArmor(current.getTypeId()) && */NoItem.getPM().has(p, this, current)) {
					event.setCancelled(true);
					this.doNotify(p, current);
				}
			}
		}
	}

	@Override
	public void check() {
		// TODO Implement Wear check

	}

	private Player getPlayerFromEntity(HumanEntity ent) {
		return Bukkit.getPlayer(ent.getName());
	}
}
