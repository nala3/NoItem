
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class Wear extends CheckableNIFeature {

	public Wear() {
		super("Wear", "You are not allowed to wear %i!", true);
		// TODO Test feature + implement armor list again
	}

	public void handleWearInvClick(InventoryClickEvent event) {
		if (event.isCancelled())
			return;
		InventoryView view = event.getView();
		SlotType st = event.getSlotType();
		int slot = event.getRawSlot();
		if (view.getType() == InventoryType.CRAFTING && slot != -999) {
			ItemStack cursor = event.getCursor();
			ItemStack current = event.getCurrentItem();
			Player p = getPlayerFromEntity(event.getWhoClicked());
			// Handle direct clicking
			if (st == SlotType.ARMOR && cursor.getTypeId() != 0) {
				if (NoItem.getLists().isArmor(cursor.getTypeId())
						&& NoItem.getPM().has(p, this, cursor)) {
					event.setCancelled(true);
					this.doNotify(p, cursor);
				}
				// Handle shift clicking
			} else if (st != SlotType.ARMOR && current.getTypeId() != 0
					&& event.isShiftClick()) {
				if (NoItem.getLists().isArmor(current.getTypeId())
						&& NoItem.getPM().has(p, this, current)) {
					event.setCancelled(true);
					this.doNotify(p, current);
				}
			}
		}
	}

	@Override
	public void check(Player p) {
		for(ItemStack i : p.getInventory().getArmorContents()) {
			// No need for list checks here, we know it is being worn
			if(NoItem.getPM().has(p, this, i)) {
				this.doNotify(p, i);
				i.setTypeId(0);
			}
		}
	}

	private Player getPlayerFromEntity(HumanEntity ent) {
		return Bukkit.getPlayer(ent.getName());
	}
}
