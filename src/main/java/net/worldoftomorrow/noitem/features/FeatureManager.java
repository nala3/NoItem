
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

package net.worldoftomorrow.noitem.features;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import org.bukkit.entity.Player;

import net.worldoftomorrow.noitem.Logging;
import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.features.defaults.Break;
import net.worldoftomorrow.noitem.features.defaults.Brew;
import net.worldoftomorrow.noitem.features.defaults.Cook;
import net.worldoftomorrow.noitem.features.defaults.Craft;
import net.worldoftomorrow.noitem.features.defaults.Drink;
import net.worldoftomorrow.noitem.features.defaults.Drop;
import net.worldoftomorrow.noitem.features.defaults.Enchant;
import net.worldoftomorrow.noitem.features.defaults.Have;
import net.worldoftomorrow.noitem.features.defaults.Hold;
import net.worldoftomorrow.noitem.features.defaults.Interact;
import net.worldoftomorrow.noitem.features.defaults.InteractLeft;
import net.worldoftomorrow.noitem.features.defaults.InteractRight;
import net.worldoftomorrow.noitem.features.defaults.Open;
import net.worldoftomorrow.noitem.features.defaults.Pickup;
import net.worldoftomorrow.noitem.features.defaults.Place;
import net.worldoftomorrow.noitem.features.defaults.Use;
import net.worldoftomorrow.noitem.features.defaults.Wear;

public class FeatureManager extends URLClassLoader {

	private final ArrayList<NIFeature> defaults = new ArrayList<NIFeature>();
	private final ArrayList<NIFeature> custom = new ArrayList<NIFeature>();
	private int defLoaded = 0;
	private int custLoaded = 0;

	private static File featureFolder;

	// Needs the context class loader. (
	// Thread.currentThread().getContextClassLoader() )
	// TODO: If a MalformedURLException is thrown, tell them to remove special
	// characters from custom class names.
	public FeatureManager(ClassLoader parent) {
		super(setupFolder(), parent);
		// Add the default classes to the list.
		defaults.add(new Break());
		defaults.add(new Brew());
		defaults.add(new Cook());
		defaults.add(new Craft());
		defaults.add(new Drink());
		defaults.add(new Drop());
		defaults.add(new Enchant());
		defaults.add(new Have());
		defaults.add(new Hold());
		defaults.add(new Interact());
		defaults.add(new InteractLeft());
		defaults.add(new InteractRight());
		defaults.add(new Open());
		defaults.add(new Pickup());
		defaults.add(new Place());
		defaults.add(new Use());
		defaults.add(new Wear());
	}
	
	/**
	 * @return An ArrayList of all loaded default and custom features
	 */
	public ArrayList<NIFeature> getLoadedFeatures() {
		ArrayList<NIFeature> all = new ArrayList<NIFeature>();
		all.addAll(defaults);
		all.addAll(custom);
		return all;
	}
	
	public static FeatureManager getInstance() {
		return NoItem.getInstance().getFeatureManager();
	}

	/**
	 * 
	 * @return URLs of custom features to be loaded.
	 * @throws MalformedURLException
	 */
	private static URL[] setupFolder() {
		// Set the static feature folder.
		setFeatureFolder(new File(NoItem.getInstance().getDataFolder()
				+ File.separator + "features"));
		// Make sure the feature folder exists.
		if (!featureFolder.exists()) {
			featureFolder.mkdirs();
			return new URL[0];
		} else {
			// This obviously does not need to be run if the feature folder was
			// just made!
			return getCustomFeatures();
		}
	}

	/**
	 * Gets the URLs of all classes from the custom feature folder
	 * 
	 * @throws MalformedURLException
	 * 
	 * @returns URLs of custom features
	 */
	private static URL[] getCustomFeatures() {
		ArrayList<URL> urls = new ArrayList<URL>();
		for (File f : featureFolder.listFiles()) {
			if (f.isFile() && f.getName().matches(".+\\.class")) {
				try {
					urls.add(f.toURI().toURL());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		return urls.toArray(new URL[urls.size()]);
	}

	private static void setFeatureFolder(File f) {
		featureFolder = f;
	}

	/**
	 * Loads all custom features.
	 * 
	 * @param urls
	 * @returns Number of loaded custom features
	 */
	private int loadCustomFeatures() {
		try {
			URL[] urls;
			urls = getCustomFeatures();
			custom.clear(); // Clear the custom classes.
			for (URL url : urls) {
				Class<?> clazz = this.loadClass(url.getPath(), true);
				// Check if the class extends at least CustomNIFeature
				if (clazz.isAssignableFrom(CustomNIFeature.class)) {
					custom.add((CustomNIFeature) clazz.newInstance());
				} else if (clazz.isAssignableFrom(CustomCheckableNIFeature.class)) {
					custom.add((CustomCheckableNIFeature) clazz.newInstance());
				}
				NoItem.getInstance().reloadConfig();
			}
		} catch (ClassNotFoundException e) {
			// Could not find class
		} catch (InstantiationException e) {
			// Class constructor must have no arguments.
		} catch (IllegalAccessException e) {
			// Class is not public
		}
		int loaded = 0;
		for (NIFeature f : custom) {
			if (f.isEnabled()) {
				NoItem.registerListener(f);
				loaded++;
				Logging.debug("Loaded custom feature: " + f.getName());
			}
		}
		return loaded;
	}

	/**
	 * Loads all default features that are enabled in the configuration
	 * 
	 * @returns Number of default features loaded
	 */
	private int loadDefaultFeatures() {
		int loaded = 0;
		for (NIFeature f : defaults) {
			if (f.isEnabled()) {
				NoItem.registerListener(f);
				loaded++;
			}
		}
		NoItem.getInstance().reloadConfig();
		return loaded;
	}

	/**
	 * Reloads all custom and default features
	 * 
	 * @return Total number of reloaded features.
	 * @throws MalformedURLException
	 */
	public int reloadFeatures() {
		int defNewLoaded = loadDefaultFeatures();
		int custNewLoaded = loadCustomFeatures();
		if (defNewLoaded < defLoaded || custNewLoaded < custLoaded)
			Logging.warn("To disable a feature, the server must be restarted.");
		defLoaded = defNewLoaded; // Update the counts
		custLoaded = custNewLoaded;
		return defNewLoaded + custNewLoaded;
	}
	
	public void check(Player p) {
		for(NIFeature f : this.getLoadedFeatures()) {
			if(f.isCheckable()) {
				((CheckableNIFeature) f).check(p);
			}
		}
	}
}
