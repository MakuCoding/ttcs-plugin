package de.Maku.ttcs.main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.Maku.ttcs.Commands.CmdBug;
import de.Maku.ttcs.Commands.CmdTrain;
import de.Maku.ttcs.configs.Bugs;
import de.Maku.ttcs.configs.TrainTypes;
import de.Maku.ttcs.listeners.PlayerJoinLeaveKickBanListener;

public class Main extends JavaPlugin {
	
	public TrainTypes traintypes;
	public Bugs bugs;
	public String prefix = ChatColor.BLACK + "[" + ChatColor.DARK_RED + "TTCS" + ChatColor.BLACK + "] " + ChatColor.RESET;
	private HashMap<String, de.Maku.ttcs.Commands.Command> cmds = new HashMap<String, de.Maku.ttcs.Commands.Command>();
	
	public void onEnable() {
		
		traintypes = new TrainTypes();
		bugs = new Bugs();
		Bukkit.getConsoleSender().sendMessage("[TTCS] Plugin enabled!");
		registerCommands();
		registerListener();
		
	}
	
	public void onDisable() {
		
		Bukkit.getConsoleSender().sendMessage("[TTCS] Plugin disabled!");
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
	
		if (cmds.containsKey(cmd.getName().toLowerCase())) {
			if (!cmds.get(cmd.getName().toLowerCase()).execute(args, p)) {
				cmds.get(cmd.getName().toLowerCase()).help(p);
				return true;
			}
			return true;
		}
		
		return false;
	}
	
	public void registerCommands() {
		cmds.put("train", new CmdTrain(this));
		cmds.put("bug", new CmdBug(this));
	}
	
	public void registerListener() {
		Bukkit.getPluginManager().registerEvents(new PlayerJoinLeaveKickBanListener(), this);
	}

}