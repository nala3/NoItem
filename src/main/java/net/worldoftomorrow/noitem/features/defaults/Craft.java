package net.worldoftomorrow.noitem.features.defaults;

import net.minecraft.server.v1_5_R2.CraftingManager;
import net.minecraft.server.v1_5_R2.EntityPlayer;
import net.minecraft.server.v1_5_R2.InventoryCrafting;
import net.minecraft.server.v1_5_R2.MinecraftServer;
import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_5_R2.inventory.CraftItemStack;
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
		
		// Shouldn't all this only be done for known custom crafting tables?
		Player p = getPlayerFromEntity(event.getWhoClicked());
		EntityPlayer ep = MinecraftServer.getServer().getPlayerList().f(p.getName());
		InventoryCrafting ic = new InventoryCrafting(ep.activeContainer, 3, 3);
		net.minecraft.server.v1_5_R2.ItemStack result = CraftingManager.getInstance().craft(ic, ep.world);
		if(result != null) {
			ItemStack stack = CraftItemStack.asBukkitCopy(result);
			if(NoItem.getPM().has(p, this, stack)) {
				event.setCancelled(true);
				this.doNotify(p, stack);
			}
		}
	}

	private Player getPlayerFromEntity(HumanEntity ent) {
		return Bukkit.getPlayer(ent.getName());
	}
}
