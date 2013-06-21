
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
