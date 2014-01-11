package com.gmail.bukkitSmerf.killPoints.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.bukkitSmerf.killPoints.Cfg;
import com.gmail.bukkitSmerf.killPoints.Db;
import com.gmail.bukkitSmerf.killPoints.Msg;
import com.gmail.bukkitSmerf.killPoints.PlayersHandler;
import com.gmail.bukkitSmerf.killPoints.Utils;

public class Reload implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("Kills.Reload")) {
			if (args.length > 0 && args[0].equalsIgnoreCase("-f")) {
				Db.clear();
			} else if (args.length > 0 && args[0].equalsIgnoreCase("-rp")) {
				PlayersHandler.reLoadPlayers();
			} else if (args.length > 0 && args[0].equalsIgnoreCase("-s")) {
				Db.savePlayers();
			} else {
				new Cfg();
			}
		} else
			return Utils.fixAndSend(Msg.getNoPermissions().replace("{%Permission%}", "Kills.Reload"), sender);
		return Utils.fixAndSend("&aReload complete.", sender);
	}
}
