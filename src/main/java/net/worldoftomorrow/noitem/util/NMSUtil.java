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
