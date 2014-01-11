package com.gmail.bukkitSmerf.killPoints.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.gmail.bukkitSmerf.killPoints.PlayersHandler;

public class OnLogin implements Listener {

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if (!e.getResult().equals(Result.ALLOWED))
				return;
		String p = e.getPlayer().getName();
		if (PlayersHandler.isPlayer(p))
			PlayersHandler.loadPlayer(p);
		else
			PlayersHandler.createPlayer(p);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		String name = e.getPlayer().getName();
		PlayersHandler.unloadPlayer(name);
	}
}
