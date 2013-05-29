package net.worldoftomorrow.noitem;

import java.io.IOException;

import net.worldoftomorrow.noitem.features.FeatureManager;
import net.worldoftomorrow.noitem.features.NIFeature;
import net.worldoftomorrow.noitem.features.lists.Lists;
import net.worldoftomorrow.noitem.permissions.PermManager;
import net.worldoftomorrow.noitem.util.Metrics;
import net.worldoftomorrow.noitem.util.Updater;

import org.bukkit.plugin.java.JavaPlugin;

public class NoItem extends JavaPlugin {

	private static NoItem	instance;
	private FeatureManager	fm;
	private PermManager		pm;
	@SuppressWarnings("unused")
	private Config			config;
	@SuppressWarnings("unused")
	private Lists 			lists;

	public NoItem() {
		setupStatic(this);
	}

	@Override
	public void onEnable() {
		this.config = new Config();
		this.fm = new FeatureManager(Thread.currentThread().getContextClassLoader());
		Logging.info(fm.reloadFeatures() + " features loaded.");
		this.pm = new PermManager();
		this.lists = new Lists();
		if(Config.getBoolean("AutoDownloadUpdates")) {
			new Updater(this, this.getFile(), Updater.UpdateType.DEFAULT, true);
		} else if(Config.getBoolean("CheckForUpdates")) {
			new Updater(this, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, true);
		}
		
		try {
			if(!new Metrics(this).start()) {
				this.getLogger().warning("Could not start metrics!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
