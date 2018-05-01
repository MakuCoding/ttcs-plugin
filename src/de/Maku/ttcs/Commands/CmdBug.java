package de.Maku.ttcs.Commands;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.Maku.ttcs.main.Main;

public class CmdBug implements Command {

	private Main plugin;
	
	public CmdBug(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean execute(String[] args, Player p) {
		if ((p.isOp() || p.getName().equalsIgnoreCase("Maku_Gamer")) && args.length == 1) {
			if (args[0].equalsIgnoreCase("remove")) {
				plugin.bugs.removeBugs();
				p.sendMessage(plugin.prefix + "Es wurden alle Bugs entfernt!");
				return true;
			} else if (args[0].equalsIgnoreCase("list")) {
				HashMap<String, List<String>> bgs = plugin.bugs.getBugs();
				StringBuilder sb = new StringBuilder();
				for (String s1:bgs.keySet()) {
					sb.append(s1 + ":\n");
					for (String s2:bgs.get(s1)) {
						sb.append("- " + s2 + "\n");
					}
				}
				p.sendMessage(plugin.prefix + "Hier sind alle Bugreports:\n" + sb.toString());
				return true;
			}
		} else if (args[0].equalsIgnoreCase("report")) {
			Bukkit.getConsoleSender().sendMessage("Hallo");
			StringBuilder sb = new StringBuilder();
			for (int i = 1;i < args.length;i++) {
				sb.append(args[i] + " ");
			}
			sb.deleteCharAt(sb.length() - 1);
			plugin.bugs.addBug(p.getName(), sb.toString());
			p.sendMessage(plugin.prefix + "Danke, dass du die Weiterentwicklung dieses Plugins unterstützt ;)");
			return true;
		} else {
			Bukkit.getConsoleSender().sendMessage(args[0]);
		}
		return false;
	}

	@Override
	public void help(Player p) {
		p.sendMessage(plugin.prefix + "Hilfe für den /bug-Command:\n"
									+ "/bug report <Bug>\n"
									+ "/bug list\n"
									+ "/bug remove");
	}

}
