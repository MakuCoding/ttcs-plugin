package de.Maku.ttcs.trains;

import java.util.ArrayList;
import java.util.Calendar;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.Maku.ttcs.main.Checker;
import de.Maku.ttcs.main.Main;

public class Trains {

	private String type;
	private String number;
	private ArrayList<String> stops = new ArrayList<String>();
	private int day;
	private int hour;
	private int crhour;
	private int min;
	private int crmin;
	private int delay;
	private String delayReason;
	private int stopindex;
	private ChatColor color;
	private String evu;
	private Player player;
	private Thread checker;
	
	private static ArrayList<Trains> trains = new ArrayList<Trains>();
	
	public static Trains addTrain(String id, ArrayList<String> stops, int hour, int min, Player player, Main plugin) {
		for (Trains t:trains) {
			if ((t.getType() + t.getNumber()).equalsIgnoreCase(id)) return null;
		}
		
		String type = "";
		String number = "";
		Trains neu = null;
		
		if (plugin.traintypes.getTypes().contains(id.substring(0, 2).toUpperCase())) {
			type = id.substring(0, 2).toUpperCase();
		} else if (plugin.traintypes.getTypes().contains(id.substring(0, 3).toUpperCase())) {
			type = id.substring(0, 3).toUpperCase();
		} else if (plugin.traintypes.getTypes().contains(id.substring(0, 4).toUpperCase())) {
			type = id.substring(0, 4).toUpperCase();
		} else if (plugin.traintypes.getTypes().contains(id.substring(0, 5).toUpperCase())) {
			type = id.substring(0, 5).toUpperCase();
		} else if (plugin.traintypes.getTypes().contains(id.substring(0, 6).toUpperCase())) {
			type = id.substring(0, 6).toUpperCase();
		} else if (plugin.traintypes.getTypes().contains(id.substring(0, 7).toUpperCase())) {
			type = id.substring(0, 7).toUpperCase();
		} else if (plugin.traintypes.getTypes().contains(id.substring(0, 8).toUpperCase())) {
			type = id.substring(0, 8).toUpperCase();
		} else if (plugin.traintypes.getTypes().contains(id.substring(0, 9).toUpperCase())) {
			type = id.substring(0, 9).toUpperCase();
		} else if (plugin.traintypes.getTypes().contains(id.substring(0, 10).toUpperCase())) {
			type = id.substring(0, 10).toUpperCase();
		} else if (plugin.traintypes.getTypes().contains(id.substring(0, 11).toUpperCase())) {
			type = id.substring(0, 11).toUpperCase();
		}
		else return null;
		
		number = id.substring(type.length());
		
		for (String s:plugin.traintypes.getEVUs()) {
			if (plugin.traintypes.getTypes(s).contains(type)) {
				neu = new Trains(type, number, stops, hour, min, ChatColor.getByChar(plugin.traintypes.getColor(s)), s, player);
				neu.setChecker(new Checker(plugin, neu));
			}
		}
		
		if (neu != null) trains.add(neu);
		return neu;
	}
	
	public static Trains remTrain(String id) {
		Trains train = null;
		try {
			train = trains.get(Integer.parseInt(id) - 1);
			trains.remove(trains.get(Integer.parseInt(id) - 1));
		} catch (Exception e) {
			for (Trains t:trains) {
				if ((t.getType() + t.getNumber()).equalsIgnoreCase(id)) {
					trains.remove(t);
					return t;
				}
			}
		}
		return train;
	}

	public static boolean remTrain(Trains train) {
		if (trains.contains(train)) {
			return trains.remove(train);
		}
		return false;
	}
	
	public static ArrayList<Trains> getTrains() {
		return trains;
	}
	
	public static Trains delaying(String id, int delay) {
		Trains train = null;
		try {
			train = trains.get(Integer.parseInt(id) - 1);
			train.setDelay(delay);
		} catch (Exception e) {
			for (Trains t:trains) {
				if ((t.getType() + t.getNumber()).equalsIgnoreCase(id)) {
					t.setDelay(delay);
					return t;
				}
			}
		}
		return train;
	}
	
	public static Trains delaying(String id, int delay, String reason) {
		Trains train = null;
		try {
			train = trains.get(Integer.parseInt(id) - 1);
			train.setDelay(delay);
			train.setDelayReason(reason);
		} catch (Exception e) {
			for (Trains t:trains) {
				if ((t.getType() + t.getNumber()).equalsIgnoreCase(id)) {
					t.setDelay(delay);
					t.setDelayReason(reason);
					return t;
				}
			}
		}
		return train;
	}
	
	public static void delaying(Trains train, int delay) {
		if (trains.contains(train)) {
			train.setDelay(delay);
		}
	}
	
	public static void delaying(Trains train, int delay, String reason) {
		if (trains.contains(train)) {
			train.setDelay(delay);
			train.setDelayReason(reason);
		}
	}
	
	public static Trains depart(String id) {
		Trains train = null;
		try {
			train = trains.get(Integer.parseInt(id) - 1);
			if (train.getStopIndex() < train.getStops().size() - 1) train.addStopIndex();
		} catch (Exception e) {
			for (Trains t:trains) {
				if ((t.getType() + t.getNumber()).equalsIgnoreCase(id)) {
					if (t.getStopIndex() < train.getStops().size() - 1) t.addStopIndex();
					return t;
				}
			}
		}
		return train;
	}
	
	public static void depart(Trains train) {
		if (trains.contains(train)) {
			if (train.getStopIndex() < train.getStops().size() - 1)	train.addStopIndex();
		}
	}
	
	public static Trains depart(String id, int dephour, int depmin) {
		Trains train = null;
		try {
			train = trains.get(Integer.parseInt(id) - 1);
				train.setHour(dephour);
				train.setMin(depmin);
				train.setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		} catch (Exception e) {
			for (Trains t:trains) {
				if ((t.getType() + t.getNumber()).equalsIgnoreCase(id)) {
						t.setHour(dephour);
						t.setMin(depmin);
						t.setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
					return t;
				}
			}
		}
		return train;
	}
	
	public static void depart(Trains train, int dephour, int depmin) {
		if (trains.contains(train)) {
			train.setHour(dephour);
			train.setMin(depmin);
			train.setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		}
	}
	
	public static Trains skip(String id) {
		Trains train = null;
		try {
			train = trains.get(Integer.parseInt(id) - 1);
			if (train.getStopIndex() < train.getStops().size() - 1) train.addStopIndex();
		} catch (Exception e) {
			for (Trains t:trains) {
				if ((t.getType() + t.getNumber()).equalsIgnoreCase(id)) {
					if (t.getStopIndex() < t.getStops().size() - 1) t.addStopIndex();
					return t;
				}
			}
		}
		return train;
	}
	
	public static void skip(Trains train) {
		if (trains.contains(train) && train.getStopIndex() < train.getStops().size() - 1) train.addStopIndex();
	}
	
	public static Trains getTrain(String id) {
		try {
			return trains.get(Integer.parseInt(id) - 1);
		} catch (Exception e) {
			for (Trains t:trains) {
				if ((t.getType() + t.getNumber()).equalsIgnoreCase(id)) {
					return t;
				}
			}
		}
		return null;
	}
	
	public Trains(String type, String number, ArrayList<String> stops, int hour, int min, ChatColor color, String evu, Player player) {
		this.type = type;
		this.number = number;
		this.stops = stops;
		this.day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		this.hour = hour;
		this.hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		this.min = min;
		this.min = Calendar.getInstance().get(Calendar.MINUTE);
		this.stopindex = 0;
		this.delay = 0;
		this.delayReason = "";
		this.color = color;
		this.evu = evu;
		this.player = player;
		this.checker = null;
	}

	public String getType() {
		return this.type;
	}
	
	public String getNumber() {
		return this.number;
	}
	
	public ArrayList<String> getStops() {
		return this.stops;
	}
	
	public String getTime() {
		if (this.min < 10) {
			return this.hour + ":" + "0" + this.min;
		}
		return this.hour + ":" + this.min;
	}
	
	public int getDay() {
		return this.day;
	}
	
	public int getHour() {
		return this.hour;
	}
	
	public int getCrHour() {
		return this.crhour;
	}
	
	public int getMin() {
		return this.min;
	}
	
	public int getCrMin() {
		return this.crmin;
	}
	
	public int getDelay() {
		return this.delay;
	}
	
	public String getDelayReason() {
		return this.delayReason;
	}
	
	public int getStopIndex() {
		return this.stopindex;
	}
	
	public ChatColor getColor() {
		return this.color;
	}

	public String getEVU() {
		return this.evu;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Thread getChecker() {
		return this.checker;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public void setStops(ArrayList<String> stops) {
		this.stops = stops;
	}
	
	public void addStop(String stop) {
		this.stops.add(stop);
	}
	
	public void remStop(String stop) {
		this.stops.remove(stop);
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public void setCrHour(int crhour) {
		this.crhour = crhour;
	}
	
	public void setMin(int min) {
		this.min = min;
	}
	
	public void setCrMin(int crmin) {
		this.crmin = crmin;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public void setDelayReason(String reason) {
		this.delayReason = reason;
	}
	
	public void addStopIndex() {
		this.stopindex++;
	}
	
	public void setColor(ChatColor color) {
		this.color = color;
	}

	public void setEVU(String evu) {
		this.evu = evu;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void setChecker(Thread checker) {
		try {
			if (this.checker.isAlive()) this.checker.interrupt();
		} catch (Exception e) {}
		this.checker = checker;
	}
	
}