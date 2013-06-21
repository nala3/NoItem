
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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import net.worldoftomorrow.noitem.NoItem;

import org.bukkit.configuration.file.YamlConfiguration;

public abstract class YamlFile {

	private final File				file;
	private final YamlConfiguration	resource;
	private final YamlConfiguration	yamlfile;
	private final NoItem			plugin;

	public YamlFile(String dir, String name) {
		this.plugin = NoItem.getInstance();
		this.file = new File(plugin.getDataFolder() + File.separator + dir, name);
		this.resource = YamlConfiguration.loadConfiguration(plugin.getResource(name));
		this.yamlfile = load();
	}

	private YamlConfiguration load() {
		try {
			// If the file does not exist, create it. Return null if it fails
			if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
				plugin.getLogger().severe("Could not create parent directory.");
			}
			if (!file.exists() && !file.createNewFile()) {
				plugin.getLogger().severe("Could not create config file: " + file.getName());
				return null;
			} else {
				// Get the configuration file from the plugin folder
				// This will be assigned to yamlfile
				YamlConfiguration confile = YamlConfiguration.loadConfiguration(file);
				// Get the values of the resource file
				Map<String, Object> vals = resource.getValues(true);
				String key;
				// If the destination file does not have the entry
				// That the resource file has, add it. Otherwise
				// Leave its value the same
				for (Entry<String, Object> entry : vals.entrySet()) {
					key = entry.getKey();
					if (!confile.isSet(key)) {
						confile.set(key, entry.getValue());
					}
				}
				// Write the file to the disk
				PrintWriter o = new PrintWriter(file, "UTF-8");
				o.write(confile.saveToString());
				o.close();
				return confile;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public YamlConfiguration getConfig() {
		return this.yamlfile;
	}

	public YamlConfiguration getDefaultConfig() {
		return this.resource;
	}

	/**
	 * Get an object from the config file The boolean is whether to return the
	 * default value if the real value is not set; defaults to true.
	 * 
	 * @param key
	 * @param def
	 * @return Object from the config
	 */
	public Object getObject(String key, boolean def) {
		if (!this.getConfig().isSet(key) && def) {
			return this.getDefaultConfig().get(key);
		} else {
			return this.getConfig().get(key);
		}
	}

	public Object getObject(String key) {
		return getObject(key, true);
	}
	
	public void setValue(String key, Object val) {
		this.yamlfile.set(key, val);
	}
}
