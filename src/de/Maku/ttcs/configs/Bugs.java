package de.Maku.ttcs.configs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Bugs {
	
	private static File ConfigFile;
	private static FileConfiguration config;

	public Bugs() {
		ConfigFile = new File("plugins/TTCS", "Bugs.yml");
		config = YamlConfiguration.loadConfiguration(ConfigFile);
		config.options().header("Bitte nicht verändern! Hier werden Bug-Reports gespeichert!");
		try {save();} catch (Exception e) {}
	}
	
	public void save() throws IOException {
		config.save(ConfigFile);
	}
	
	public void addBug(String playername, String bugreport) {
		List<String> plyrs = config.getStringList("Playernames");
		if (!plyrs.contains(playername)) plyrs.add(playername);
		config.set("Playernames", plyrs);
		List<String> bgs = config.getStringList("Bugs." + playername);
		bgs.add(bugreport);
		config.set("Bugs." + playername, bgs);
		try {save();} catch (Exception e) {}
	}
	
	public HashMap<String, List<String>> getBugs() {
		List<String> plyrs = config.getStringList("Playernames");
		HashMap<String, List<String>> bugs = new HashMap<String, List<String>>();
		for (String s1:plyrs) {
			for (String s2:config.getStringList("Bugs." + s1)) {
				if (bugs.containsKey(s1)) {
					List<String> ls = bugs.get(s1);
					ls.add(s2);
					bugs.replace(s1, ls);
				} else {
					List<String> ls = new ArrayList<String>();
					ls.add(s2);
					bugs.put(s1, ls);
				}
			}
		}
		return bugs;
	}
	
	public void removeBugs() {
		List<String> plyrs = config.getStringList("Playernames");
		plyrs.forEach(s -> config.set("Bugs." + s, new ArrayList<String>()));
		config.set("Playernames", new ArrayList<String>());
		try {save();} catch (Exception e) {}
	}
	
}
