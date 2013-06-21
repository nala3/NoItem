
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

public class Tools extends YamlFile {

	public Tools() {
		super("lists", "tools.yml");
	}
	
	public boolean isTool(ItemStack item) {
		return isTool(item.getTypeId());
	}
	
	public boolean isTool(int id) {
		return isPickaxe(id) || isAxe(id) || isSword(id) || isShovel(id) || isHoe(id) || isShear(id) || isOther(id);
	}
	
	public boolean isPickaxe(ItemStack item) {
		return isPickaxe(item.getTypeId());
	}
	
	public boolean isPickaxe(int id) {
		return this.getConfig().getList("Pickaxes").contains(id);
	}
	
	public boolean isAxe(ItemStack item) {
		return isAxe(item.getTypeId());
	}
	
	public boolean isAxe(int id) {
		return this.getConfig().getList("Axes").contains(id);
	}
	
	public boolean isSword(ItemStack item) {
		return isSword(item.getTypeId());
	}
	
	public boolean isSword(int id) {
		return this.getConfig().getList("Swords").contains(id);
	}
	
	public boolean isShovel(ItemStack item) {
		return isShovel(item.getTypeId());
	}
	
	public boolean isShovel(int id) {
		return this.getConfig().getList("Shovels").contains(id);
	}
	
	public boolean isHoe(ItemStack item) {
		return isHoe(item.getTypeId());
	}
	
	public boolean isHoe(int id) {
		return this.getConfig().getList("Hoes").contains(id);
	}
	
	public boolean isShear(ItemStack item) {
		return isShear(item.getTypeId());
	}
	
	public boolean isShear(int id) {
		return this.getConfig().getList("Shears").contains(id);
	}
	
	public boolean isOther(ItemStack item) {
		return isOther(item.getTypeId());
	}
	
	public boolean isOther(int id) {
		return this.getConfig().getList("Other").contains(id);
	}
}
