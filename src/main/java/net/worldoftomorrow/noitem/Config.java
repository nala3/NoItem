package net.worldoftomorrow.noitem;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	private final NoItem			plugin;
	private final YamlConfiguration	header;
	private final YamlConfiguration	misc;
	private final File				config;

	public Config() {
		this.plugin = NoItem.getInstance();
		this.config = new File(plugin.getDataFolder(), "config.yml");
		this.header = YamlConfiguration.loadConfiguration(plugin.getResource("header.yml"));
		this.misc = YamlConfiguration.loadConfiguration(plugin.getResource("misc.yml"));
		this.load();
	}

	private void load() {
		try {
			if (config.exists()) {
				this.setUserValues();
				this.writeConfig();
				NoItem.getInstance().reloadConfig();
			} else {
				this.createConfig();
				this.writeConfig();
				NoItem.getInstance().reloadConfig();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeConfig() throws IOException {
		if (!config.delete() && !config.createNewFile()) {
			plugin.getLogger().severe("Could not create fresh configuration file! - 002");
		}
		PrintWriter o = new PrintWriter(config, "UTF-8");
		o.write(header.saveToString());
		o.write(misc.saveToString());
		o.close();
		NoItem.getInstance().reloadConfig();
	}

	private void setUserValues() {
		// Load the original configuration
		YamlConfiguration orig = YamlConfiguration.loadConfiguration(config);
		// Get its current values
		Map<String, Object> values = orig.getValues(true);
		String key;
		// Loop through each old value to "clean" the configuration
		// by removing old, extra, or out of place values.
		for (Entry<String, Object> entry : values.entrySet()) {
			key = entry.getKey();
			if (key.startsWith("Features.")) {
				Logging.debug("Set usr cfg val: " + key + ": " + entry.getValue().toString());
				header.set(key, entry.getValue());
			} else if (misc.isSet(key)) {
				misc.set(key, entry.getValue());
			}
		}
	}

	private void createConfig() throws IOException {
		if (!config.getParentFile().exists() && !config.getParentFile().mkdir()) {
			plugin.getLogger().severe("Could not create config directory!");
		}
		if (!config.exists() && !config.createNewFile()) {
			plugin.getLogger().severe("Could not create new config file! - 001");
		}
	}

	public static Object getValue(String key) {
		return NoItem.getInstance().getConfig().get(key);
	}

	public static boolean getBoolean(String key) {
		return NoItem.getInstance().getConfig().getBoolean(key);
	}

	public static String getString(String key) {
		return NoItem.getInstance().getConfig().getString(key);
	}

	public static Map<String, Object> getValues() {
		return NoItem.getInstance().getConfig().getValues(true);
	}

	public static boolean isSet(String key) {
		return NoItem.getInstance().getConfig().isSet(key);
	}

	public static void set(String key, Object value) {
		Logging.debug("Set cfg val: " + key + ": " + value);
		NoItem.getInstance().getConfig().set(key, value);
		NoItem.getInstance().saveConfig();
	}
}
