package net.worldoftomorrow.noitem.features.configs;

import java.io.File;
import java.io.IOException;

import net.worldoftomorrow.noitem.Logging;
import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.configuration.file.YamlConfiguration;

public abstract class FeatureConfig {
	private final File file;
	private final YamlConfiguration config;

	public FeatureConfig(NIFeature feature) {
		this.file = new File(NoItem.getInstance().getDataFolder()
				+ File.separator + "configs" + File.separator
				+ feature.getName().toLowerCase());
		if(!initialize()) {
			Logging.severe("Could not initialize feature config!");
		}
		this.config = YamlConfiguration.loadConfiguration(this.file);
	}

	private boolean initialize() {
		if (file.exists()) {
			return true;
		} else {
			try {
				if (!file.getParentFile().exists()
						&& !file.getParentFile().mkdirs()) {
					Logging.severe("Could not create feature config folder!");
					return false;
				}
				if (!file.createNewFile()) {
					Logging.severe("Could not create feature config!");
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public YamlConfiguration getYamlConfig() {
		return this.config;
	}
	
	public void setValue(String key, Object o) {
		this.config.set(key, o);
	}
	
	public String getString(String key) {
		return this.config.getString(key);
	}
	
	public boolean getBoolean(String key) {
		return this.config.getBoolean(key);
	}
	
	
}
