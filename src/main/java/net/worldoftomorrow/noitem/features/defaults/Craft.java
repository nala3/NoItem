
/* Copyright (c) 2013, Worldoftomorrow.net
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies, 
 * either expressed or implied, of the FreeBSD Project.
 */

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
