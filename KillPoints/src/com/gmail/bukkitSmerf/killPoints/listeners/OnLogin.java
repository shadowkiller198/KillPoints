package com.gmail.bukkitSmerf.killPoints.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.bukkitSmerf.killPoints.Cfg;
import com.gmail.bukkitSmerf.killPoints.Db;

public class OnLogin implements Listener {

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Db.loadPlayer(e.getPlayer().getName());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		String name = e.getPlayer().getName();
		Db.savePlayer(Cfg.getPlayer(name));
		Cfg.removePlayer(name);
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {
		String name = e.getPlayer().getName();
		Db.savePlayer(Cfg.getPlayer(name));
		Cfg.removePlayer(name);
	}

}
