
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

import net.minecraft.server.v1_5_R2.RecipesFurnace;
import net.minecraft.server.v1_5_R2.TileEntityFurnace;
import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_5_R2.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class Cook extends NIFeature {

	public Cook() {
		super("Cook", "You are not allowed to cook %i!", true);
		// TODO: Test feature
	}

	@EventHandler
	public void handleNoCookInvClick(InventoryClickEvent event) {
		if (event.isCancelled()) return;
		InventoryView view = event.getView();
		int slot = event.getRawSlot();
		ItemStack fuel = view.getItem(1);
		ItemStack cookable = view.getItem(0);
		if (view.getType() == InventoryType.FURNACE) {
			SlotType st = event.getSlotType();
			ItemStack cursor = event.getCursor();
			ItemStack current = event.getCurrentItem();
			Player p = getPlayerFromEntity(event.getWhoClicked());
			if (slot == 1 && cursor.getTypeId() != 0 && cookable.getTypeId() != 0 && isFuel(cursor)) {
				if (NoItem.getPM().has(p, this, cookable)) {
					event.setCancelled(true);
					this.doNotify(p, cookable);
					// Check if the current item is also a fuel, just because.
					if (current.getTypeId() != 0 && isFuel(current)) {
						// If the inventory is full
						if (p.getInventory().firstEmpty() == -1) {
							// Drop a copy of the item by the player
							p.getWorld().dropItem(p.getLocation(), new ItemStack(current));
						} else {
							// Give a copy of the item to the player
							p.getInventory().addItem(new ItemStack(current));
						}
						// Remove the original item
						view.setItem(slot, null);
						p.sendMessage(ChatColor.BLUE + "Well darn, the old item is fuel too! Let me just fix that..");
					}
				}
				// Uncooked Item slot
			} else if (slot == 0) {
				if (fuel.getTypeId() != 0 && cursor.getTypeId() != 0 && isCookable(cursor)) {
					if (NoItem.getPM().has(p, this, cursor)) {
						event.setCancelled(true);
						this.doNotify(p, cursor);
					}
				}
				// Shift clicking anywhere else in the inventory
			} else if (slot > 3 && st != SlotType.OUTSIDE && event.isShiftClick()) {
				if (current.getTypeId() != 0) {
					if (fuel.getTypeId() != 0 && isCookable(current)) {
						if (NoItem.getPM().has(p, this, current)) {
							event.setCancelled(true);
							this.doNotify(p, current);
						}
					} else if (cookable.getTypeId() != 0 && isFuel(current)) {
						if (NoItem.getPM().has(p, this, cookable)) {
							event.setCancelled(true);
							this.doNotify(p, cookable);
						}
					}
				}
			}
		}
	}

	private boolean isFuel(ItemStack item) {
		// Create an NMS item stack
		net.minecraft.server.v1_5_R2.ItemStack nmss = CraftItemStack.asNMSCopy(item);
		// Use the NMS TileEntityFurnace to check if the item being clicked is a
		// fuel
		return TileEntityFurnace.isFuel(nmss);
	}

	private boolean isCookable(ItemStack item) {
		net.minecraft.server.v1_5_R2.ItemStack nmss = CraftItemStack.asNMSCopy(item);
		// If the result of that item being cooked is null, it is not cookable
		return RecipesFurnace.getInstance().getResult(nmss.getItem().id) != null;
	}

	private Player getPlayerFromEntity(HumanEntity ent) {
		return Bukkit.getPlayer(ent.getName());
	}
}
