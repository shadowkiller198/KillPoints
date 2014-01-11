package com.gmail.bukkitSmerf.killPoints.listeners;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.gmail.bukkitSmerf.killPoints.Cfg;
import com.gmail.bukkitSmerf.killPoints.Cfg.DmgCfg;
import com.gmail.bukkitSmerf.killPoints.LocalPlayer;
import com.gmail.bukkitSmerf.killPoints.Msg;
import com.gmail.bukkitSmerf.killPoints.PlayersHandler;
import com.gmail.bukkitSmerf.killPoints.Utils;

public class OnKill implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onKill(PlayerDeathEvent e) {
		LocalPlayer killed = PlayersHandler.tryGetPlayer(e.getEntity().getName());
		Player pKiller = e.getEntity().getKiller();
		if (pKiller != null) {
			LocalPlayer killer = PlayersHandler.tryGetPlayer(pKiller.getName());
			boolean kill = true;
			if (killed.getName().equals(killer.getName()))
				kill = false;
			long statVPoints = (kill) ? killer.addKill(killed) : 0;
			long statLPoints = (kill) ? killed.addDeath(statVPoints) : 0;
			e.setDeathMessage(Utils.fixMsg(Msg.getOnKill().replace("{%killer%}", killer.getName()).replace("{%killed%}", killed.getName()).replace("{%lose/win%}", ((statVPoints > 0) ? Msg.getWin() : Msg.getLose())).replace("{%Vpoints%}", statVPoints + "").replace("{%Vpointsnew%}", killer.getPoints() + "").replace("{%Vkd%}", killer.getKD() + "").replace("{%Vkills%}", killer.getKills() + "").replace("{%Vdeaths%}", killer.getDeaths() + "").replace("{%Lpoints%}", statLPoints + "").replace("{%Lpointsnew%}", killed.getPoints() + "").replace("{%Lkills%}", killed.getKills() + "").replace("{%Ldeaths%}", killed.getDeaths() + "").replace("{%Lkd%}", killed.getKD() + "")));
		} else {
			EntityDamageEvent dmg = e.getEntity().getLastDamageCause();
			DamageCause dmgCause = dmg.getCause();
			String cause = null;
			if (!dmgCause.equals(DamageCause.ENTITY_ATTACK)) {
				cause = dmgCause.toString();
			} else {
				Entity killer = null;
				if ((dmg != null) && !dmg.isCancelled() && (dmg instanceof EntityDamageByEntityEvent)) {
					Entity damager = ((EntityDamageByEntityEvent) dmg).getDamager();
					if (damager instanceof Projectile) {
						LivingEntity shooter = ((Projectile) damager).getShooter();
						if (shooter != null)
							killer = shooter;
					}
					killer = damager;
				}
				if (killer == null)
					return;
				cause = killer.getType().toString();
			}
			long p = killed.getPoints();
			String dMsg = addDeath(killed, cause);
			p -= killed.getPoints();
			if (dMsg != null)
				e.setDeathMessage(Utils.fixMsg(dMsg.replace("{%killed%}", killed.getName()).replace("{%lose/win%}", ((p > 0) ? Msg.getWin() : Msg.getLose())).replace("{%Lpoints%}", p + "").replace("{%Lpointsnew%}", killed.getPoints() + "").replace("{%Lkills%}", killed.getKills() + "").replace("{%Ldeaths%}", killed.getDeaths() + "").replace("{%Lkd%}", killed.getKD() + "")));
			else
				e.setDeathMessage(Utils.fixMsg(e.getDeathMessage().replace("{%killed%}", killed.getName()).replace("{%lose/win%}", ((p > 0) ? Msg.getWin() : Msg.getLose())).replace("{%Lpoints%}", p + "").replace("{%Lpointsnew%}", killed.getPoints() + "").replace("{%Lkills%}", killed.getKills() + "").replace("{%Ldeaths%}", killed.getDeaths() + "").replace("{%Lkd%}", killed.getKD() + "")));
		}
	}

	private static String addDeath(LocalPlayer killed, String string) {
		DmgCfg dmgCfg = Cfg.getDmgCfg(string);
		if (dmgCfg == null || dmgCfg.losePercentage == -1)
			return null;
		Random rand = new Random();
		long lPoints = (long) -(((rand.nextInt(100) + dmgCfg.losePercentage) >= 100) ? ((rand.nextInt(dmgCfg.loseMinPercentage + dmgCfg.loseMaxPercentage) + dmgCfg.loseMinPercentage) * dmgCfg.multi) : 0);
		killed.addPoints(lPoints);
		killed.setDeath(killed.getDeaths() + 1);
		return dmgCfg.deathMsgEnabled ? dmgCfg.deathMsg : null;
	}
}
