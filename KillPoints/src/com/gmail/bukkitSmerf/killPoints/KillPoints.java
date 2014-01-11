package com.gmail.bukkitSmerf.killPoints;

import java.io.File;
import java.io.InputStream;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class KillPoints extends JavaPlugin {

	private static File pluginDataFolder;
	private static InputStream configRes;

	@Override
	public void onEnable() {
		pluginDataFolder = this.getDataFolder();
		configRes = getResource("Config.yml");
		Utils.setConsole(Bukkit.getConsoleSender());
		new Cfg();
		getCommand("TopKill").setExecutor(new com.gmail.bukkitSmerf.killPoints.commands.TopKill());
		getCommand("Stat").setExecutor(new com.gmail.bukkitSmerf.killPoints.commands.Stat());
		getCommand("KillReload").setExecutor(new com.gmail.bukkitSmerf.killPoints.commands.Reload());
		Bukkit.getPluginManager().registerEvents(new com.gmail.bukkitSmerf.killPoints.listeners.OnKill(), this);
		Bukkit.getPluginManager().registerEvents(new com.gmail.bukkitSmerf.killPoints.listeners.OnLogin(), this);
		if (Cfg.getAutoSave() > 0) {
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					Utils.warn("Saving players...");
					Db.savePlayers();
					Utils.warn("Saved");
				}
			};
			if (Cfg.isaSyncAutoSave())
				Bukkit.getScheduler().runTaskTimerAsynchronously(this, runnable, Cfg.getAutoSave(), Cfg.getAutoSave());
			else
				Bukkit.getScheduler().runTaskTimer(this, runnable, Cfg.getAutoSave(), Cfg.getAutoSave());
		}
		Utils.log("&aPlugin &9KillLevels&a by &bBukkitSmerf&a zosta³ uruchomiony.");
	}

	public static File getPluginDataFolder() {
		return pluginDataFolder;
	}

	public static InputStream getConfigRes() {
		return configRes;
	}

	@Override
	public void onDisable() {
		Utils.warn("Saving players... (Stopping)");
		Db.savePlayers();
		Utils.warn("Saved (Stopping)");
	}
}
