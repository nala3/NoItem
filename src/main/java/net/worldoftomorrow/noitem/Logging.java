package net.worldoftomorrow.noitem;

public class Logging {
	public static void info(String msg) {
		NoItem.getInstance().getLogger().info(msg);
	}
	
	public static void warn(String msg) {
		NoItem.getInstance().getLogger().warning(msg);
	}
	
	public static void severe(String msg) {
		NoItem.getInstance().getLogger().severe(msg);
	}
	
	public static void debug(String msg) {
		if(Config.getBoolean("Debugging")) NoItem.getInstance().getLogger().info("[DBG] ".concat(msg));
	}
}
