package de.Maku.ttcs.Commands;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.Maku.ttcs.main.Checker;
import de.Maku.ttcs.main.Main;
import de.Maku.ttcs.trains.Trains;

public class CmdTrain implements Command {
	
	private Main plugin;
	private HashMap<Player, Trains> tp = new HashMap<Player, Trains>();
	
	public CmdTrain(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean execute(String[] args, Player p) {
		Trains train = null;
		if (args.length > 0) {
			switch (args[0].toLowerCase()) {
	
			case "announce":
			case "ann":
				if (p.hasPermission("ttcs.train.announce")) {
					if (args.length > 4) {
						ArrayList<String> stops = new ArrayList<String>();
						for (int i = 3;i < args.length;i++) {
							stops.add(args[i]);
						}
						int hour = 0;
						int min = 0;
						try {
							hour = Integer.parseInt(args[2].split(":")[0]);
							min = Integer.parseInt(args[2].split(":")[1]);
						} catch (Exception e) {
							p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Du musst eine Zeit im Format HH:MM angeben");
						}
						train = Trains.addTrain(args[1], stops, hour, min, p, plugin);
						if (train == null) {
							p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Diese Zugnummer ist schon vorhanden/Es gibt dieses Kürzel nicht!");
							return true;
						} else {
							tp.put(p, train);
							StringBuilder sb = new StringBuilder();
							for (int i = 0;i < stops.size();i++) {
								if (i == 0) {
									sb.append(" von " + stops.get(i));
								} else if (i > 0 && i < stops.size() - 1){
									sb.append(" über " + stops.get(i));
								} else if (i == stops.size() - 1) {
									sb.append(" nach " + stops.get(i));
								}
							}
							if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == hour && Calendar.getInstance().get(Calendar.MINUTE) == min) {
								Bukkit.broadcastMessage(plugin.prefix + train.getColor() + train.getType() + train.getNumber() + " fährt jetzt" + sb.toString());
								p.sendMessage(plugin.prefix + ChatColor.DARK_GREEN + "Der Zug wurde angekündigt!");
								train.setChecker(new Checker(plugin, train));
								return true;
							} else {
								String text = train.getColor() + train.getType() + train.getNumber() + " fährt jetzt von " + train.getStops().get(train.getStopIndex()) + " ab!";
								train.setChecker(new Checker(plugin, train, text));
								
								Bukkit.broadcastMessage(plugin.prefix + train.getColor() + train.getType() + train.getNumber() + ": Abfahrt um " + train.getTime() + " Uhr" + sb.toString());
								p.sendMessage(plugin.prefix + ChatColor.DARK_GREEN + "Der Zug wurde angekündigt!");
								return true;
							}
						}
					} else {
						p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Du hast zu wenig/falsche Parameter übergeben!");
						p.sendMessage(plugin.prefix + "Syntax: /train announce <Zugtyp + Zugnummer> <Abfahrtszeit> <Startbahnhof> [Halt1] [Halt2] ... <Zielbahnhof>\n" +
								"Beispiel: /train announce RE100 15:00 Lyndern Knuffingen Kleinod");
						return true;
					}
				}
				
			case "list":
			case "ls":
				if (p.hasPermission("ttcs.train.list")) {
					ArrayList<Trains> trains = Trains.getTrains();
					StringBuilder sb = new StringBuilder();
					for (Trains t:trains) {
						sb.append(t.getColor() + ((trains.indexOf(t) + 1) + ". " + t.getType() + t.getNumber() + ": "));
						if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == t.getHour() && Calendar.getInstance().get(Calendar.MINUTE) <= t.getMin()) {
							sb.append("Abfahrt " + t.getTime() + " Uhr von ");
						} else if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == t.getDay() && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <= t.getHour()) {
							sb.append("Abfahrt " + t.getTime() + " Uhr von ");
						} else {
							sb.append(" Nächster Halt ");
						}
						for (int i = t.getStopIndex();i < t.getStops().size();i++) {
							sb.append(t.getStops().get(i) + " -> ");
						}
						sb.delete(sb.length() - 4, sb.length());
						sb.append(t.getDelay() != 0 ? " (+" + t.getDelay() + " Minuten" + (t.getDelayReason() != "" ? " wegen: " + t.getDelayReason():"") + ")\n":"\n");
					}
					if (trains.size() > 0) {
					p.sendMessage("----------------Angekündigte Zugfahrten----------------\n" +
								sb.toString() + ChatColor.RESET
								+ "----------------------------------------------------------");
					} else {
						p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Es sind keine Zugfahrten angekündigt!");
					}
					return true;
				}
				
			case "remove":
			case "rem":
				if (p.hasPermission("ttcs.train.remove")) {
					if (args.length == 2) {
						train = Trains.remTrain(args[1]);
						if (train == null) {
							if (args[1].equalsIgnoreCase("my") && tp.containsKey(p)) {
								train = tp.get(p);
								if (Trains.remTrain(train)) {
									p.sendMessage(plugin.prefix + ChatColor.DARK_GREEN + "Der Zug " + train.getType() + train.getNumber() + " wurde erfolgreich gelöscht!");
									tp.put(p, train);
									return true;
								}
							} else {
								p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Es wurde kein Zug mit dieser Bezeichnung gefunden!");
								return true;
							}
						} else {
							p.sendMessage(plugin.prefix + ChatColor.DARK_GREEN + "Der Zug " + train.getType() + train.getNumber() + " wurde erfolgreich gelöscht!");
							tp.put(p, train);
							return true;
						}
					} else {
						p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Du hast zu viele/zu wenig Parameter übergeben!");
						p.sendMessage(plugin.prefix + ChatColor.WHITE + "Syntax: /train remove <Zugtyp + Zugnummer|ID(aus der Liste)|my(dein aktiver Zug)>" +
									"Beispiele: /train remove RE456 oder\n/train remove 2 oder\n/train remove my");
						return true;
					}
				}
				
			case "delay":
			case "del":
				if (p.hasPermission("ttcs.train.delay")) {
					int delay = 0;
					try {
						delay = Integer.parseInt(args[2]);
					} catch (Exception e) {
						p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Du musst eine Zahl als Verspätung eingeben!");
						return true;
					}
					if (args.length == 3) {
						train = Trains.delaying(args[1], delay);
						if (train == null) {
							if (args[1].equalsIgnoreCase("my") && tp.containsKey(p)) {
								train = tp.get(p);
								Trains.delaying(train, delay);
								p.sendMessage(plugin.prefix + train.getType() + train.getNumber() + ": Verspätung aktualisiert!");
								Bukkit.broadcastMessage(plugin.prefix + ChatColor.DARK_RED + train.getType() + train.getNumber() + " ist " + delay + " Minuten verspätet!");
								tp.put(p, train);
								return true;
							} else {
								p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Es wurde kein Zug mit dieser Bezeichnung gefunden!");
								return true;
							}
						} else {
							p.sendMessage(plugin.prefix + train.getType() + train.getNumber() + ": Verspätung aktualisiert!");
							Bukkit.broadcastMessage(plugin.prefix + ChatColor.DARK_RED + train.getType() + train.getNumber() + " ist " + delay + " Minuten verspätet!");
							tp.put(p, train);
							return true;
						}
					} else if (args.length > 3) {
						StringBuilder srb = new StringBuilder();
						for (int i = 3;i < args.length;i++) {
							srb.append(args[i] + " ");
						}
						srb.deleteCharAt(srb.length() - 1);
						train = Trains.delaying(args[1], delay, srb.toString());
						if (train == null) {
							if (args[1].equalsIgnoreCase("my") && tp.containsKey(p)) {
								train = tp.get(p);
								Trains.delaying(train, delay, srb.toString());
								p.sendMessage(plugin.prefix + train.getType() + train.getNumber() + ": Verspätung aktualisiert!");
								Bukkit.broadcastMessage(plugin.prefix + ChatColor.DARK_RED + train.getType() + train.getNumber() + " ist " + delay + " Minuten verspätet!\n"
													+ "Grund: " + srb.toString());
								tp.put(p, train);
								return true;
							} else {
								p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Es wurde kein Zug mit dieser Bezeichnung gefunden!");
								return true;
							}
						} else {
							p.sendMessage(plugin.prefix + train.getType() + train.getNumber() + ": Verspätung aktualisiert!");
							Bukkit.broadcastMessage(plugin.prefix + ChatColor.DARK_RED + train.getType() + train.getNumber() + " ist " + delay + " Minuten verspätet!\n"
												+ "Grund: " + srb.toString());
							tp.put(p, train);
							return true;
						}
					} else {
						p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Du hast zu wenig Parameter übergeben!");
						p.sendMessage(plugin.prefix + ChatColor.RESET + "Syntax: /train delay <Zugtyp + Zugnummer> <Zeit in Minuten> [Grund]");
						return true;
					}
				}
				
			case "arrive":
			case "arrival":
			case "arr":
				if ( p.hasPermission("ttcs.train.arrive")) {
					if (args.length == 2) {
						train = Trains.getTrain(args[1]);
						if (train == null) {
							if (args[1].equalsIgnoreCase("my") && tp.containsKey(p)) {
								train = tp.get(p);
								Bukkit.broadcastMessage(plugin.prefix + train.getColor() + train.getType() + train.getNumber() + ": Nächster Halt: " +
										train.getStops().get(train.getStopIndex()) + (train.getStops().size() == train.getStopIndex() + 1?
										"\nDieser Zug endet hier! Wir bitten alle Fahrgäste auszusteigen, verabschieden uns im Namen der "
										+ train.getEVU() + " und wünschen Ihnen noch einen angenehmen Tag!":""));
								tp.put(p, train);
								return true;
							} else {
								p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Es wurde kein Zug mit dieser Bezeichnung gefunden!");
								return true;
							}
						} else {
							Bukkit.broadcastMessage(plugin.prefix + train.getColor() + train.getType() + train.getNumber() + ": Nächster Halt: " +
									train.getStops().get(train.getStopIndex()) + (train.getStops().size() == train.getStopIndex() + 1?
									"\nDieser Zug endet hier! Wir bitten alle Fahrgäste auszusteigen, verabschieden uns im Namen der "
									+ train.getEVU() + " und wünschen Ihnen noch einen angenehmen Tag!":""));
							tp.put(p, train);
							return true;
						}
					} else if (args.length == 3) {
						int hour = 0;
						int min = 0;
						try {
							hour = Integer.parseInt(args[2].split(":")[0]);
							min = Integer.parseInt(args[2].split(":")[1]);
						} catch (Exception e) {
							p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Die eingegeben Uhrzeit ist ungültig! Format: HH:MM");
							return true;
						}
						train = Trains.getTrain(args[1]);
						if (train == null) {
							if (args[1].equalsIgnoreCase("my") && tp.containsKey(p)) {
								train = tp.get(p);
								Bukkit.broadcastMessage(plugin.prefix + train.getColor() + train.getType() + train.getNumber() + ": Nächster Halt: " +
										train.getStops().get(train.getStopIndex()) + " Ankunft: " + hour + ":" + (min < 10 ? "0" + min:min) + " Uhr" 
										+ (train.getStops().size() == train.getStopIndex() + 1?"\nDieser Zug endet hier! Wir bitten alle Fahrgäste "
										+ "auszusteigen, verabschieden uns im Namen der " + train.getEVU() + " und wünschen Ihnen noch einen angenehmen Tag!":""));
								tp.put(p, train);
								return true;
							} else {
								p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Es wurde kein Zug mit dieser Bezeichnung gefunden!");
								return true;
							}
						} else {
							Bukkit.broadcastMessage(plugin.prefix + train.getColor() + train.getType() + train.getNumber() + ": Nächster Halt: " +
									train.getStops().get(train.getStopIndex()) + " Ankunft: " + hour + ":" + (min < 10 ? "0" + min:min) + " Uhr" 
									+ (train.getStops().size() == train.getStopIndex() + 1?"\nDieser Zug endet hier! Wir bitte alle Fahrgäste "
									+ "auszusteigen, verabschieden uns im Namen der " + train.getEVU() + " und wünschen Ihnen noch einen angenehmen Tag!":""));
							tp.put(p, train);
							return true;
						}
					} else {
						p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Du hast falsche Parameter übergeben!");
						p.sendMessage(plugin.prefix + ChatColor.RESET + "Syntax: /train arrive <Zugtyp + Zugnummer|ID(aus der Liste)> [Ankunftszeit]");
						return true;
					}
				}
				
			case "depart":
			case "depature":
			case "dep":
				if (p.hasPermission("ttcs.train.depart")) {
					if (args.length == 2) {
						train = Trains.depart(args[1]);
						if (train == null) {
							if (args[1].equalsIgnoreCase("my") && tp.containsKey(p)) {
								train = tp.get(p);
								Trains.depart(train);
								Bukkit.broadcastMessage(plugin.prefix + train.getColor() + train.getType() + train.getNumber() + " fährt jetzt von " + train.getStops().get(train.getStopIndex() - 1) + " ab!");
								train.setChecker(new Checker(plugin, train));
								tp.put(p, train);
								return true;
							} else {
								p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Es wurde kein Zug mit dieser Bezeichnung gefunden!");
								return true;
							}
						} else {
							Bukkit.broadcastMessage(plugin.prefix + train.getColor() + train.getType() + train.getNumber() + " fährt jetzt von " + train.getStops().get(train.getStopIndex() - 1) + " ab!");
							try {
								if (train.getChecker().isAlive()) {
									train.getChecker().interrupt();
								}
							} catch (Exception e) {}
							tp.put(p, train);
							return true;
						}
					} else if (args.length == 3) {
						int dephour = 0;
						int depmin = 0;
						try {
							dephour = Integer.parseInt(args[2].split(":")[0]);
							depmin = Integer.parseInt(args[2].split(":")[1]);
						} catch (Exception e) {
							p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Die eingegeben Uhrzeit ist ungültig! Format: HH:MM");
							return true;
						}
						train = Trains.depart(args[1], dephour, depmin);
						if (train == null) {
							if (args[1].equalsIgnoreCase("my") && tp.containsKey(p)) {
								train = tp.get(p);
								Bukkit.broadcastMessage(plugin.prefix + train.getColor() + train.getType() + train.getNumber() + " fährt um " + train.getTime() + " Uhr von " + train.getStops().get(train.getStopIndex()) + " ab!");
								Trains.depart(train, dephour, depmin);
								String text = train.getColor() + train.getType() + train.getNumber() + " fährt jetzt von " + train.getStops().get(train.getStopIndex()) + " ab!";
								train.setChecker(new Checker(plugin, train, text));
								tp.put(p, train);
								return true;
							} else {
								p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Es wurde kein Zug mit dieser Bezeichnung gefunden!");
								return true;
							}
						} else {
							Bukkit.broadcastMessage(plugin.prefix + train.getColor() + train.getType() + train.getNumber() + " fährt um " + train.getTime() + " Uhr von " + train.getStops().get(train.getStopIndex()) + " ab!");
							String text = train.getColor() + train.getType() + train.getNumber() + " fährt jetzt von " + train.getStops().get(train.getStopIndex()) + " ab!";
							train.setChecker(new Checker(plugin, train, text));
							tp.put(p, train);
							return true;
						}
					} else {
						p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Du hast falsche Parameter übergeben!");
						p.sendMessage(plugin.prefix + ChatColor.RESET + "Syntax: /train depart <Zugtyp + Zugnummer|ID(aus der Liste)> [Abfahrtszeit]");
						return true;
					}
				}
				
			case "skip":
			case "sk":
				if (p.hasPermission("ttcs.train.skip")) {
					if (args.length == 2) {
						train = Trains.skip(args[1]);
						if (train == null) {
							if (args[1].equalsIgnoreCase("my") && tp.containsKey(p)) {
								train = tp.get(p);
								Trains.skip(train);
								p.sendMessage(plugin.prefix + ChatColor.GREEN + "Die aktuelle Haltestelle wurde übersprungen. Die nächste ist nun " + train.getStops().get(train.getStopIndex()));
								return true;
							} else {
								p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Es wurde kein Zug mit dieser Bezeichnung gefunden!");
								return true;
							}
						} else {
							p.sendMessage(plugin.prefix + ChatColor.GREEN + "Die aktuelle Haltestelle wurde übersprungen. Die nächste ist nun " + train.getStops().get(train.getStopIndex()));
							tp.put(p, train);
							return true;
						}
					} else {
						p.sendMessage(plugin.prefix + ChatColor.DARK_RED + "Du hast falsche Parameter übergeben!");
						p.sendMessage(plugin.prefix + ChatColor.RESET + "Syntax: /train skip <Zugtyp + Zugnummer|ID(aus der Liste)>");
						return true;
					}
				}
				
			case "types":
				if (p.hasPermission("ttcs.train.announce")) {
					p.sendMessage(plugin.prefix + ChatColor.RESET + "Verfügbare Zuggattungen:\n"
						+ String.join(", ", TrainTypes.getTypes());
				}
				return true;
			}
		} else {
			p.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Train Announcer von Maku\n" + ChatColor.RESET + "Für Hilfe gebe /train help ein");
			return true;
		}
		return false;
	}

	@Override
	public void help(Player p) {
		p.sendMessage(plugin.prefix + "Hilfe zum /train Command:\n"
				+ "/train announce <Zugtyp + Zugnummer> <Abfahrtszeit> <Halte>\n"
				+ "/train list\n"
				+ "/train remove <Zugtyp + Zugnummer|ID(aus der Liste)|my>\n"
				+ "/train delay <Zugtyp + Zugnummer|ID(aus der Liste)|my> <Uhrzeit> [Grund]\n"
				+ "/train arrive <Zugtyp + Zugnummer|ID(aus der Liste)|my> [Uhrzeit]\n"
				+ "/train depart <Zugtyp + Zugnummer|ID(aus der Liste)|my> [Uhrzeit]\n"
				+ "<obligatorisch>, [optional], | = oder\nmy = aktiver Zug des Spielers"
				+ "Bitte meldet Bugs mit /bug report <Bug>");
	}

}