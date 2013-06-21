
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

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Have extends CheckableNIFeature {

	public Have() {
		super("Have", "You are not allowed to have %i!", true);
		// TODO Test feature
	}

	@EventHandler
	public void handleNoHaveItemHeld(PlayerItemHeldEvent event) {
		Player p = event.getPlayer();
		PlayerInventory inv = p.getInventory();
		ItemStack item = inv.getItem(event.getNewSlot());
		if (item != null && item.getTypeId() != 0 && NoItem.getPM().has(p, this, item)) {
			this.doNotify(p, item);
			inv.remove(item);
		}
	}

	@EventHandler
	public void handleNoHavePickup(PlayerPickupItemEvent event) {
		if (event.isCancelled()) return;
		ItemStack item = event.getItem().getItemStack();
		Player p = event.getPlayer();
		if (NoItem.getPM().has(p, this, item)) {
			event.getItem().setPickupDelay(200);
			event.setCancelled(true);
			this.doNotify(p, item);
		}
	}

	@Override
	public void check(Player p) {
		for(ItemStack i : p.getInventory().getContents()) {
			if(NoItem.getPM().has(p, this, i)) {
				this.doNotify(p, i);
				i.setTypeId(0);
			}
		}
	}
}
