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
		return players.containsKey(player);
	}

	public static LocalPlayer getPlayer(String player) {
		return players.get(player);
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
		players.put(player.getName(), player);
	}

	public static void loadPlayers() {
		for (Player p : Bukkit.getOnlinePlayers())
			if (!isLoaded(p.getName()))
				loadPlayer(p.getName());
	}

	public static void reLoadPlayers() {
		unloadPlayers();
		loadPlayers();
	}

	public static void loadPlayer(String player) {
		players.put(player, Db.loadPlayer(player));
	}

	public static void unloadPlayer(String player) {
		Db.savePlayer(players.get(player));
		players.remove(player);
	}

	public static void unloadPlayers() {
		for (Player player : Bukkit.getOnlinePlayers())
			Db.savePlayer(players.get(player.getName()));
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