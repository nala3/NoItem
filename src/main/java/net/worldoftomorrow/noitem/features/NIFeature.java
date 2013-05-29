package net.worldoftomorrow.noitem.features;

import net.worldoftomorrow.noitem.Config;
import net.worldoftomorrow.noitem.util.StringUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class NIFeature implements Listener {

	private final String	featurename;
	private final String	message;
	private final boolean	notify;
	private final boolean	enabled;

	public NIFeature(String name, String message, boolean notify) {
		this.featurename = name;
		if (Config.isSet("Features." + name)) {
			this.message = Config.getString("Features." + name + ".message");
			this.notify = Config.getBoolean("Features." + name + ".notify");
			this.enabled = Config.getBoolean("Features." + name + ".enabled");
		} else { // Create configuration section if it does not exist
			Config.set("Features." + name + ".enabled", true);
			Config.set("Features." + name + ".message", message);
			Config.set("Features." + name + ".notify", notify);
			this.message = message;
			this.notify = notify;
			this.enabled = true; // Enabled on first load by default
		}
	}

	public final String getName() {
		return this.featurename;
	}

	public final boolean isEnabled() {
		return this.enabled;
	}

	public final boolean shouldNotify() {
		return notify;
	}

	public final String getMessage() {
		return message;
	}

	public void doNotify(Player p, Object o) {
		if (this.shouldNotify()) {
			p.sendMessage(StringUtil.parseMsg(p, this.message, o));
		}
	}
	
	public boolean isCheckable() {
		return this.getClass().isAssignableFrom(CheckableNIFeature.class);
	}
}
