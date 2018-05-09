package de.Maku.ttcs.configs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class TrainTypes {
	
	private static File ConfigFile;
	private static FileConfiguration config;
	
	public TrainTypes() {
		ConfigFile = new File("plugins/TTCS", "Zugtypen.yml");
		boolean b = ConfigFile.exists();
		config = YamlConfiguration.loadConfiguration(ConfigFile);
		config.options().header("Neue EVUs einfach der ersten Liste hinzufügen\n"
							+ "Unter Farbcodes können die Farben für das jeweilige EVU festgelegt werden (Minecraft-Farbcodes)\n"
							+ "Für jedes EVU wird eine Liste mit verfügbaren Zugtypenabkürzungen angelegt");
		if (!b) setDefaults();
		else if (!config.contains("Farbcodes")) {
			config.set("Farbcodes.DB Fernverkehr", "f");
			config.set("Farbcodes.DB Regio", "c");
			config.set("Farbcodes.DB Cargo", "b");
			config.set("Farbcodes.Privatbahnen", "a");
		} else if (!config.contains("EVUs")) {
			List<String> list1 = new ArrayList<String>();
			list1.add("DB Fernverkehr");
			list1.add("DB Regio");
			list1.add("DB Cargo");
			list1.add("Privatbahnen");
			config.set("EVUs", list1);
		} else if (!config.contains("TransportiertPassagiere")) {
			config.set("TransportiertPassagiere.DB Fernverkehr", true);
			config.set("TransportiertPassagiere.DB Regio", true);
			config.set("TransportiertPassagiere.DB Cargo", false);
			config.set("TransportiertPassagiere.Privatbahnen", true);
		}
		try {save();} catch (Exception e) {}
	}
	
	public void save() throws IOException {
		config.save(ConfigFile);
	}
	
	public void setDefaults() {
		List<String> list1 = new ArrayList<String>();
		list1.add("DB Fernverkehr");
		list1.add("DB Regio");
		list1.add("DB Cargo");
		list1.add("Privatbahnen");
		config.set("EVUs", list1);
		config.set("Farbcodes.DB Fernverkehr", "f");
		config.set("Farbcodes.DB Regio", "c");
		config.set("Farbcodes.DB Cargo", "b");
		config.set("Farbcodes.Privatbahnen", "a");
		
		List<String> list2 = new ArrayList<String>();
		list2.add("ICE");
		list2.add("IC");
		list2.add("SZ");
		config.set("DB Fernverkehr", list2);
		
		List<String> list3 = new ArrayList<String>();
		list3.add("RE");
		list3.add("RB");
		config.set("DB Regio", list3);
		
		List<String> list4 = new ArrayList<String>();
		list4.add("GZ");
		list4.add("BZ");
		config.set("DB Cargo", list4);
		
		List<String> list5 = new ArrayList<String>();
		list5.add("PB");
		config.set("Privatbahnen", list5);
		
		try {save();} catch (Exception e) {}
	}
	
	public List<String> getEVUs() {
		return config.getStringList("EVUs");
	}
	
	public List<String> getTypes() {
		List<String> evus = getEVUs();
		List<String> typen = new ArrayList<String>();
		evus.forEach(s -> config.getStringList(s).forEach(ss -> typen.add(ss)));
		return typen;
	}
	
	public List<String> getTypes(String evu) {
		return config.getStringList(evu);
	}
	
	public char getColor(String evu) {
		return config.getString("Farbcodes." + evu).charAt(0);
	}
	
	public boolean getPassenger(String evu) {
		return config.getBoolean("TransportiertPassagiere." + evu);
	}

}
