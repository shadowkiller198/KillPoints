package com.gmail.bukkitSmerf.killPoints;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayersHandler {

	private static Map<String, LocalPlayer> players = new HashMap<String, LocalPlayer>();

	public static Collection<LocalPlayer> getLocalPlayers() {
		return players.values();
	}

	public static boolean isLoaded(String player) {
		return players.containsKey(player.toLowerCase());
	}

	public static LocalPlayer getPlayer(String player) {
		return players.get(player.toLowerCase());
	}

	public static LocalPlayer tempLoadPlayer(String player) {
		if (getPlayer(player) == null)
			return Db.loadPlayer(player);
		return getPlayer(player);
	}

	public static void createPlayer(String player) {
		Db.createPlayer(player);
		loadPlayer(player);
	}

	public static void sendPlayer(LocalPlayer player) {
		players.put(player.getName().toLowerCase(), player);
	}

	public static void loadPlayers() {
		for (Player player : Bukkit.getOnlinePlayers())
			players.put(player.getName().toLowerCase(), Db.loadPlayer(player.getName()));
	}

	public static void loadPlayer(String player) {
		players.put(player.toLowerCase(), Db.loadPlayer(player));
	}

	public static void unloadPlayer(String player) {
		Db.savePlayer(players.get(player.toLowerCase()));
		players.remove(player.toLowerCase());
	}

	public static void unloadPlayers() {
		for (Player player : Bukkit.getOnlinePlayers())
			Db.savePlayer(players.get(player.getName().toLowerCase()));
		players.clear();
	}

	public static boolean isPlayer(String player) {
		return Db.isPlayer(player);
	}

	public static LocalPlayer tryGetPlayer(String nickname) {
		if (PlayersHandler.isPlayer(nickname))
			if (PlayersHandler.isLoaded(nickname))
				return PlayersHandler.getPlayer(nickname);
			else
				return PlayersHandler.tempLoadPlayer(nickname);
		else
			return null;
	}

	public static LocalPlayer tryFindPlayer(String nickname) {
		if (!PlayersHandler.isPlayer(nickname)) {
			Player p = Bukkit.getPlayer(nickname);
			if (p != null)
				return tryGetPlayer(p.getName());
			else
				return null;
		} else
			return tryGetPlayer(nickname);
	}
}