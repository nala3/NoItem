package net.worldoftomorrow.noitem.util;

import net.minecraft.server.v1_5_R2.Item;
import net.minecraft.server.v1_5_R2.ItemStack;
import net.minecraft.server.v1_5_R2.PotionBrewer;

import org.bukkit.craftbukkit.v1_5_R2.inventory.CraftItemStack;

public class NMSUtil {
	public static int getPotionResult(int origdata, org.bukkit.inventory.ItemStack ingredient) {
		return getPotionResult(origdata, CraftItemStack.asNMSCopy(ingredient));
	}

	private static int getPotionResult(int origdata, ItemStack ingredient) {

		int newdata = getBrewResult(origdata, ingredient);

		if ((origdata <= 0 || origdata != newdata)) {
			return origdata != newdata ? newdata : origdata;
		} else {
			return origdata;
		}
	}

	// TODO: BROKEN
	private static int getBrewResult(int i, ItemStack itemstack) {
		return itemstack == null ? i : (Item.byId[itemstack.id].w() ? PotionBrewer.a(i, Item.byId[itemstack.id].u())
				: i);
	}
}
