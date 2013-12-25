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
		getCommand("TopKill").setExecutor(
				new com.gmail.bukkitSmerf.killPoints.commands.TopKill());
		getCommand("Stat").setExecutor(
				new com.gmail.bukkitSmerf.killPoints.commands.Stat());
		getCommand("KillReload").setExecutor(
				new com.gmail.bukkitSmerf.killPoints.commands.Reload());
		Bukkit.getPluginManager().registerEvents(
				new com.gmail.bukkitSmerf.killPoints.listeners.OnKill(), this);
		Bukkit.getPluginManager().registerEvents(
				new com.gmail.bukkitSmerf.killPoints.listeners.OnLogin(), this);
		Utils.log("&aPlugin &9KillLevels&a by &bBukkitSmerf&a zosta³ uruchomiony.");
	}

	public static File getPluginDataFolder() {
		return pluginDataFolder;
	}

	public static InputStream getConfigRes() {
		return configRes;
	}

}
