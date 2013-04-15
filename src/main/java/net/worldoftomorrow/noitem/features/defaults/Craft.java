package net.worldoftomorrow.noitem.features.defaults;

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class Craft extends NIFeature {

	public Craft() {
		super("Craft", "You are not allowed to craft %i!", true);
		// TODO Test + move getPlayerFromEntity to static Util class
	}

	public void handleItemCraft(CraftItemEvent event) {
		if (event.isCancelled()) return;
		ItemStack result = event.getCurrentItem();
		Player p = getPlayerFromEntity(event.getWhoClicked());
		if (result.getTypeId() != 0 && NoItem.getPM().has(p, this, result)) {
			event.setCancelled(true);
			this.doNotify(p, result);
		}
	}

	private Player getPlayerFromEntity(HumanEntity ent) {
		return Bukkit.getPlayer(ent.getName());
	}
}
