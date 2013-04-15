package net.worldoftomorrow.noitem;

import net.worldoftomorrow.noitem.features.FeatureManager;
import net.worldoftomorrow.noitem.features.NIFeature;
import net.worldoftomorrow.noitem.permissions.PermManager;

import org.bukkit.plugin.java.JavaPlugin;

public class NoItem extends JavaPlugin {

	private static NoItem	instance;
	private FeatureManager	fm;
	private PermManager		pm;
	@SuppressWarnings("unused")
	private Config			config;

	public NoItem() {
		setupStatic(this);
	}

	@Override
	public void onEnable() {
		this.config = new Config();
		// Load the FeatureManager AFTER static resources have been set up since
		// they are used in the feature managers constructor.
		this.fm = new FeatureManager(Thread.currentThread().getContextClassLoader());
		Logging.info(fm.reloadFeatures() + " features loaded.");
		this.pm = new PermManager();
	}

	private static void setupStatic(NoItem inst) {
		NoItem.instance = inst;
	}

	public static NoItem getInstance() {
		return instance;
	}

	public static void registerListener(NIFeature clazz) {
		instance.getServer().getPluginManager().registerEvents(clazz, instance);
	}

	public static PermManager getPM() {
		return instance.pm;
	}
}
