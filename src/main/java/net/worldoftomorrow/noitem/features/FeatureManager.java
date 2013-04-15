package net.worldoftomorrow.noitem.features;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

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

	private final ArrayList<NIFeature>			defaults	= new ArrayList<NIFeature>();
	private final ArrayList<CustomNIFeature>	custom		= new ArrayList<CustomNIFeature>();
	private int									defLoaded	= 0;
	private int									custLoaded	= 0;

	private static File							featureFolder;

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
	 * 
	 * @return URLs of custom features to be loaded.
	 * @throws MalformedURLException
	 */
	private static URL[] setupFolder() {
		// Set the static feature folder.
		setFeatureFolder(new File(NoItem.getInstance().getDataFolder() + File.separator + "features"));
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
		for (CustomNIFeature f : custom) {
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
}
