package net.worldoftomorrow.noitem.commands.frame;

import org.bukkit.entity.Player;

public abstract class PlayerCommand extends NICommand {
	
	private final Player p;
	
	public PlayerCommand(Player p, String command, String[] args) {
		super(p, command, args);
		this.p = p;
	}
	
	public Player getPlayer() {
		return p;
	}
}
