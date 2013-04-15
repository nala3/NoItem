package net.worldoftomorrow.noitem.features.defaults;

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Use extends NIFeature {

	public Use() {
		super("Use", "You are not allowed to use %i!", true);
		// TODO Test feature
	}

	@EventHandler
	public void handlePlayerDamageEntity(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) return;
		Entity e = event.getDamager();
		if (e instanceof Player) {
			Player p = (Player) e;
			ItemStack inHand = p.getItemInHand();
			if (NoItem.getPM().has(p, this, inHand)) {
				event.setCancelled(true);
				this.doNotify(p, inHand);
			}
		}
	}

	@EventHandler
	public void handlePlayerShearEntity(PlayerShearEntityEvent event) {
		if (event.isCancelled()) return;
		Player p = event.getPlayer();
		ItemStack inHand = p.getItemInHand();
		if (NoItem.getPM().has(p, this, inHand)) {
			event.setCancelled(true);
			this.doNotify(p, inHand);
		}
	}

	@EventHandler
	public void handleUseInteract(PlayerInteractEvent event) {
		if (event.isCancelled()) return;
		Player p = event.getPlayer();
		ItemStack inHand = p.getItemInHand();
		if (inHand != null && NoItem.getPM().has(p, this, inHand)) {
			event.setCancelled(true);
			this.doNotify(p, inHand);
		}
	}
}
