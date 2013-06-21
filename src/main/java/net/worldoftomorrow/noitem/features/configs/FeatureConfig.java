
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

import net.worldoftomorrow.noitem.Logging;
import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.NIFeature;

import org.bukkit.configuration.file.YamlConfiguration;

public abstract class FeatureConfig {
	private final File file;
	private final YamlConfiguration config;

	public FeatureConfig(NIFeature feature) {
		this.file = new File(NoItem.getInstance().getDataFolder() + File.separator
				+ "configs" + File.separator + feature.getName().toLowerCase());
		if (!initialize()) {
			Logging.severe("Could not initialize feature config!");
		}
		this.config = YamlConfiguration.loadConfiguration(this.file);
	}

	private boolean initialize() {
		if (file.exists()) {
			return true;
		} else {
			try {
				if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
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
