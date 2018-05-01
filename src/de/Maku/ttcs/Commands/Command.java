package de.Maku.ttcs.Commands;

import org.bukkit.entity.Player;

public interface Command {

	public boolean execute(String[] args, Player p);
	public void help(Player p);
	
}
