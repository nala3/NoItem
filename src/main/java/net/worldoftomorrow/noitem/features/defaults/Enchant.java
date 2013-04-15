package net.worldoftomorrow.noitem.features.defaults;

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

public class Enchant extends NIFeature {

	public Enchant() {
		super("Enchant", "You are not allowed to enchant %i!", true);
		// TODO Test feature
	}

	@EventHandler
	public void handleEnchantItem(EnchantItemEvent event) {
		if (event.isCancelled()) return;
		Player p = event.getEnchanter();
		ItemStack item = event.getItem();
		if (NoItem.getPM().has(p, this, item)) {
			this.doNotify(p, item);
			event.setCancelled(true);
		}
	}
}
