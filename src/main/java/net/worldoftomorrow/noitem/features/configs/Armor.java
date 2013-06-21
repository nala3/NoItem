
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

package net.worldoftomorrow.noitem.features.configs;

import org.bukkit.inventory.ItemStack;

public class Armor extends YamlFile {

	public Armor() {
		super("lists", "armor.yml");
	}
	
	public boolean isArmor(ItemStack item) {
		return isArmor(item.getTypeId());
	}
	
	public boolean isArmor(int id) {
		return isHelmet(id) || isChestplate(id) || isLeggings(id) || isBoots(id);
	}
	
	public boolean isHelmet(ItemStack item) {
		return isHelmet(item.getTypeId());
	}
	
	public boolean isHelmet(int id) {
		return this.getConfig().getList("Helmets").contains(id);
	}
	
	public boolean isChestplate(ItemStack item) {
		return isChestplate(item.getTypeId());
	}
	
	public boolean isChestplate(int id) {
		return this.getConfig().getList("Chestplates").contains(id);
	}
	
	public boolean isLeggings(ItemStack item) {
		return isLeggings(item.getTypeId());
	}
	
	public boolean isLeggings(int id) {
		return this.getConfig().getList("Leggings").contains(id);
	}
	
	public boolean isBoots(ItemStack item) {
		return this.getConfig().getList("Boots").contains(item.getTypeId());
	}
	
	public boolean isBoots(int id) {
		return this.getConfig().getList("Boots").contains(id);
	}

}
