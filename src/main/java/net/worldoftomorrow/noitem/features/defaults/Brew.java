
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
