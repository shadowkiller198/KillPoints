package com.gmail.bukkitSmerf.killPoints.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.gmail.bukkitSmerf.killPoints.LocalPlayer;
import com.gmail.bukkitSmerf.killPoints.Msg;
import com.gmail.bukkitSmerf.killPoints.PlayersHandler;
import com.gmail.bukkitSmerf.killPoints.Utils;

public class OnKill implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onKill(PlayerDeathEvent e) {
		Player pKiller = e.getEntity().getKiller();
		if (pKiller == null)
			return;
		LocalPlayer killed = PlayersHandler.tryGetPlayer(e.getEntity().getName());
		LocalPlayer killer = PlayersHandler.tryGetPlayer(pKiller.getName());
		boolean kill = true;
		if (killed.getName().equals(killer.getName()))
			kill = false;
		long statVPoints = (kill) ? killer.addKill(killed) : 0;
		long statLPoints = (kill) ? killed.addDeath(statVPoints) : 0;
		e.setDeathMessage(Utils.fixMsg(Msg.getOnKill().replace("{%killer%}", killer.getName()).replace("{%killed%}", killed.getName()).replace("{%lose/win%}", ((statVPoints > 0) ? Msg.getWin() : Msg.getLose())).replace("{%Vpoints%}", statVPoints + "").replace("{%Vpointsnew%}", killer.getPoints() + "").replace("{%Vkd%}", killer.getKD() + "").replace("{%Vkills%}", killer.getKills() + "").replace("{%Vdeaths%}", killer.getDeaths() + "").replace("{%Lpoints%}", statLPoints + "").replace("{%Lpointsnew%}", killed.getPoints() + "").replace("{%Lkills%}", killed.getKills() + "").replace("{%Ldeaths%}", killed.getDeaths() + "").replace("{%Lkd%}", killed.getKD() + "")));
	}
}
