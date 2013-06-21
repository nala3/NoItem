
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

package net.worldoftomorrow.noitem.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.server.v1_5_R2.ItemStack;
import net.minecraft.server.v1_5_R2.TileEntityBrewingStand;

import org.bukkit.craftbukkit.v1_5_R2.inventory.CraftItemStack;

public class NMSUtil {
	
	private static Method getPotionResult;
	
	static {
		try {
			getPotionResult = TileEntityBrewingStand.class.getDeclaredMethod("c", Integer.class, ItemStack.class);
			getPotionResult.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public static int getPotionResult(int origdata, org.bukkit.inventory.ItemStack ingredient) {
		return getPotionResult(origdata, CraftItemStack.asNMSCopy(ingredient));
	}

	private static int getPotionResult(int origdata, ItemStack ingredient) {
		int newdata;
		try {
			newdata = (int) getPotionResult.invoke(origdata, ingredient);
			if ((origdata <= 0 || origdata != newdata)) {
				return origdata != newdata ? newdata : origdata;
			} else {
				return origdata;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return origdata;
		}
	}
}
