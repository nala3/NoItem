
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

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.CheckableNIFeature;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Hold extends CheckableNIFeature {

	public Hold() {
		super("Hold", "You are not allowed to hold %i!", true);
		// TODO Test
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
	
	@EventHandler
	public void handleNoHoldItemHeld(PlayerItemHeldEvent event) {
		Player p = event.getPlayer();
		PlayerInventory inv = p.getInventory();
		ItemStack item = inv.getItem(event.getNewSlot());
		if (item != null && item.getTypeId() != 0 && NoItem.getPM().has(p, this, item)) {
			this.switchItems(event.getNewSlot(), event.getPreviousSlot(), inv);
			this.doNotify(p, item);
		}
	}

	private Player getPlayerFromEntity(HumanEntity ent) {
		return Bukkit.getPlayer(ent.getName());
	}
	
	private void switchItems(int s1, int s2, Inventory inv) {
		ItemStack i1 = inv.getItem(s1);
		ItemStack i2 = inv.getItem(s2);
		inv.setItem(s1, i2);
		inv.setItem(s2, i1);
	}

	@Override
	public void check(Player p) {
		ItemStack held = p.getItemInHand();
		if(held.getTypeId() != 0 && NoItem.getPM().has(p, this, held)) {
			p.getWorld().dropItem(p.getLocation(), held.clone());
			p.getInventory().setItemInHand(null);
		}
	}
}
