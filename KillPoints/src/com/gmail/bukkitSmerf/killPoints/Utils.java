package com.gmail.bukkitSmerf.killPoints;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Utils {

	private static ConsoleCommandSender console;

	public static void setConsole(ConsoleCommandSender console) {
		Utils.console = console;
	}

	public static void log(String string) {
		Utils.fixAndSend("&4[&bKillPoints&4][&aINFO&4]&r " + string, console);
	}

	public static void sendToAdmins(String string) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.hasPermission("ProWarns.Admin.Info"))
				Utils.fixAndSend("&4[&bKillPoints&4][&aINFO&4]&r " + string, p);
		}
	}

	public static void warn(String string) {
		Utils.fixAndSend("&4[&bKillPoints&4][&cWARNING&4]&r " + string, console);
	}

	public static boolean fixAndSend(String m, CommandSender s) {
		try {
			s.sendMessage(fixMsg(m));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String fixMsg(String msg) {
		return ChatColor.translateAlternateColorCodes('&',
				msg.replace("\\n", "\n"));
	}
}
