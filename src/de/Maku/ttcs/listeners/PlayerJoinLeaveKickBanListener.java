package de.Maku.ttcs.listeners;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinLeaveKickBanListener implements Listener{
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.getUniqueId() == UUID.fromString("97716bc2-06ae-4e5a-894b-3b17cfc4cd02")) {
			
		}
	}
	
}
