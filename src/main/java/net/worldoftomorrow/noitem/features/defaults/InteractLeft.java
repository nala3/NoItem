package net.worldoftomorrow.noitem.features.defaults;

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractLeft extends NIFeature {

	public InteractLeft() {
		super("InteractLeft", "You are not allowed to left-interact with %i!", true);
		// TODO: Test feature
	}

	@EventHandler
	public void onInteractLeft(PlayerInteractEvent event) {
		if (event.isCancelled()) return;
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (NoItem.getPM().has(event.getPlayer(), this, event.getClickedBlock())) {
				event.setCancelled(true);
				this.doNotify(event.getPlayer(), event.getClickedBlock());
			}
		}
	}

}
