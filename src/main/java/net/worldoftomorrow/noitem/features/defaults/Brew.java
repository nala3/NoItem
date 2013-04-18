package net.worldoftomorrow.noitem.features.defaults;

import net.minecraft.server.v1_5_R2.Item;
import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;
import net.worldoftomorrow.noitem.util.NMSUtil;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class Brew extends NIFeature {

	public Brew() {
		super("Brew", "You are not allowed to brew that potion! (%i)", true);
		// TODO Test feature (possibly fixed)
	}

	@EventHandler
	public void handleNoBrewInvClick(InventoryClickEvent event) {
		if (event.isCancelled()) return;

		InventoryView view = event.getView();
		if (view.getType() == InventoryType.BREWING) {
			ItemStack cursor = event.getCursor();
			Player p = getPlayerFromEntity(event.getWhoClicked());
			int slot = event.getRawSlot();
			ItemStack item;
			// Ing. Slot
			if (slot == 3 && cursor.getTypeId() != 0) {
				int result;
				for (int i = 0; i < 3; i++) {
					item = view.getItem(i);
					result = NMSUtil.getPotionResult(item.getDurability(), cursor);
					// If the item is air, or the items durability is the same
					// as the results, continue
					if (item.getTypeId() == 0 || item.getDurability() == result) continue;

					if (NoItem.getPM().has(p, this, result)) {
						event.setCancelled(true);
						this.doNotify(p, result);
						return; // Be sure to break the loop to avoid sending
								// multiple messages
					}
				}
				// Potion slot
				// If it is not the ing. slot, the cursor is a potion, and the
				// ingredient slot is not empty
			} else if (slot < 3 && slot >= 0 && cursor.getTypeId() == Item.POTION.id
					&& view.getItem(3).getTypeId() != 0) {
				item = view.getItem(3); // ingredient
				int result = NMSUtil.getPotionResult(cursor.getDurability(), item);
				if (result == cursor.getDurability()) return;
				if (item.getTypeId() != 0 && NoItem.getPM().has(p, this, result)) {
					event.setCancelled(true);
					this.doNotify(p, result);
				}
				// Shift click
			} else if (slot > 3 && event.isShiftClick()) {
				item = view.getItem(slot); // Clicked
				// If the item clicked is a ptoion
				if (item.getTypeId() == Item.POTION.id) {
					ItemStack ingredient = view.getItem(3);
					// If the ingredient is empty, return
					if (ingredient.getTypeId() == 0) return;

					int result = NMSUtil.getPotionResult(item.getDurability(), ingredient);
					if (NoItem.getPM().has(p, this, result)) {
						event.setCancelled(true);
						this.doNotify(p, result);
					}
					// Else, treat it as an ingredient
				} else {
					ItemStack potion;
					int result;
					for (int i = 0; i < 3; i++) {
						potion = view.getItem(i);
						result = NMSUtil.getPotionResult(potion.getDurability(), item);
						if (NoItem.getPM().has(p, this, result)) {
							event.setCancelled(true);
							this.doNotify(p, result);
							return;
						}
					}
				}
			}
		}
	}

	private Player getPlayerFromEntity(HumanEntity ent) {
		return Bukkit.getPlayer(ent.getName());
	}

}
