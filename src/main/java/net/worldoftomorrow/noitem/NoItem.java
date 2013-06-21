
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

package net.worldoftomorrow.noitem;

import java.io.IOException;

import net.worldoftomorrow.noitem.commands.CommandManager;
import net.worldoftomorrow.noitem.features.FeatureManager;
import net.worldoftomorrow.noitem.features.NIFeature;
import net.worldoftomorrow.noitem.features.configs.Lists;
import net.worldoftomorrow.noitem.permissions.PermManager;
import net.worldoftomorrow.noitem.util.Metrics;
import net.worldoftomorrow.noitem.util.Updater;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class NoItem extends JavaPlugin {
	
	// Seperate log file for errors to be printed

	private static NoItem	instance;
	private FeatureManager	fm;
	private PermManager		pm;
	@SuppressWarnings("unused")
	private Config			config;
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
		this.getCommand("noitem").setExecutor((CommandExecutor) new CommandManager());
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
	
	public static Lists getLists() {
		return instance.lists;
	}
	
	public FeatureManager getFeatureManager() {
		return fm;
	}
}
