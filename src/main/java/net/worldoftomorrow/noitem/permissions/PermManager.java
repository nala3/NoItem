
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

package net.worldoftomorrow.noitem.permissions;

import net.worldoftomorrow.noitem.Config;
import net.worldoftomorrow.noitem.features.NIFeature;
import net.worldoftomorrow.noitem.util.StringUtil;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PermManager {
	public boolean	whitelist	= Config.getBoolean("PermsAsWhiteList");

	public boolean has(Player p, String perm) {
		if (perm == null) return whitelist;
		return p.hasPermission(perm);
	}

	public boolean has(Player p, NIFeature feature, Object o) {
		final boolean has = check(p, construct(feature, o));
		return whitelist ? !has : has; 
	}

	private String[] construct(NIFeature feature, Object o) {
		String perm = "noitem." + feature.getName().toLowerCase() + ".";
		final int id;
		final short data;
		final String name;

		if (o instanceof ItemStack) {
			ItemStack stack = (ItemStack) o;
			id = stack.getTypeId();
			name = StringUtil.getStackName(stack);
			data = stack.getDurability();
		} else if (o instanceof Block) {
			Block b = (Block) o;
			id = b.getTypeId();
			name = StringUtil.getBlockName(b);
			data = b.getData();
		} else if (o instanceof Entity) {
			Entity e = (Entity) o;
			name = StringUtil.getEntityName(e);
			id = -1;
			data = -1;
		} else if (o instanceof Integer) {
			name = o.toString();
			id = -1;
			data = -1;
		} else {
			throw new UnsupportedOperationException("Unknown object type: " + o.getClass().getSimpleName());
		}

		String[] perms = new String[7];
		if (id != -1 && data != -1) {
			perms[0] = perm + name + '.' + data; // Standard name
			perms[1] = perm + id + '.' + data; // Standard number
			if (data == 0) {
				perms[2] = perm + id; // ID, no data
				perms[3] = perm + name; // Name, no data
			}
			perms[4] = perm + name + ".*"; // Name, all data
			perms[5] = perm + id + ".*"; // ID, all data
			perms[6] = perm + "*"; // Entire feature

		} else {
			perms[0] = perm + name; // Standard permission
		}

		return perms;
	}

	private boolean check(Player p, String[] perms) {
		if (p.hasPermission("noitem.*")) return whitelist;
		// First check if any of the normal permissions are set false.
		for (int i = 0; i <= 3; i++) {
			if (perms[i] == null) continue;
			// Return the opposite as whitelist.
			if (permSetFalse(p, perms[i])) return !whitelist;
		}
		// Then just check every permission
		for (String perm : perms) {
			if (perm == null) continue;
			if (p.hasPermission(perm)) return true;
		}
		// TODO Auto-generated method stub
		return false;
	}

	private boolean permSetFalse(Player p, String perm) {
		return p.isPermissionSet(perm) && !p.hasPermission(perm);
	}
}
