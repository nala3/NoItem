package net.worldoftomorrow.noitem.features.defaults;

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interact extends NIFeature {

	public Interact() {
		super("Interact", "You can not interact with %i!", true);
		// TODO: Test feature
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled()) return;
		Player p = event.getPlayer();
		// If the event is NOT a block place event and was not in air
		Block clicked = event.getClickedBlock();
		if (!event.isBlockInHand() && clicked != null) {
			if (NoItem.getPM().has(p, this, clicked)) {
				event.setCancelled(true);
				this.doNotify(p, clicked);
			}
		}
	}

	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent event) {
		if (event.isCancelled()) return;
		Player p = event.getPlayer();
		Entity clicked = event.getRightClicked();
		if (NoItem.getPM().has(p, this, clicked)) {
			event.setCancelled(true);
			this.doNotify(p, clicked);
		}
	}
}
