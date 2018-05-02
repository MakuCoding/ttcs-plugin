package de.Maku.ttcs.main;

import java.util.Calendar;

import org.bukkit.Bukkit;

import de.Maku.ttcs.trains.Trains;

public class Checker extends Thread {
	
	private Main plugin;
	private String text;
	private Trains train;
													 
	public Checker(Main plugin, Trains train, String text) {				
		this.plugin = plugin;
		this.train = train;
		this.text = text;
	}
	
	public Checker(Main plugin, Trains train) {
		this.plugin = plugin;
		this.train = train;
		this.text = "";
	}
	
	public void run() {
		while (true) {
			try {
				if (text != "" && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == train.getHour() && Calendar.getInstance().get(Calendar.MINUTE) == train.getMin()) {
					Bukkit.broadcastMessage(plugin.prefix + text);
					if (train.getStopIndex() < train.getStops().size() - 1) train.addStopIndex();
					text = "";
				}
				if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 5 == train.getCrHour()) {
					Trains.remTrain(train);
					break;
				} else if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) != train.getDay() && train.getCrHour() >= 5) {
					Trains.remTrain(train);
					break;
				}
				Thread.sleep(60000);
			} catch (Exception e) {}
		}
	}

}
