package com.gmail.bukkitSmerf.killPoints.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.bukkitSmerf.killPoints.Cfg;
import com.gmail.bukkitSmerf.killPoints.Db;
import com.gmail.bukkitSmerf.killPoints.LocalPlayer;
import com.gmail.bukkitSmerf.killPoints.Msg;
import com.gmail.bukkitSmerf.killPoints.Utils;

public class Stat implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (args.length == 0) {
			if (sender.hasPermission("Kills.OwnStats"))
				if (sender instanceof Player) {
					LocalPlayer p = Cfg.getPlayer(sender.getName()
							.toLowerCase());
					return send(p, sender);
				} else
					return true;
			else
				return Utils.fixAndSend(
						Msg.getNoPermissions().replace("{%Permission%}",
								"Kills.OwnStats"), sender);
		} else {
			if (!sender.hasPermission("Kills.OtherStats"))
				return Utils.fixAndSend(
						Msg.getNoPermissions().replace("{%Permission%}",
								"Kills.OtherStats"), sender);
			LocalPlayer p = null;
			if (Cfg.isLoadedPlayer(args[0]))
				p = Cfg.getPlayer(args[0].toLowerCase());
			else
				p = Db.getPlayer(args[0].toLowerCase());
			return send(p, sender);
		}
	}

	private boolean send(LocalPlayer p, CommandSender s) {
		return Utils.fixAndSend(
				Msg.getStat().replace("{%player%}", p.getName())
						.replace("{%points%}", p.getPoints() + "")
						.replace("{%kills%}", p.getKills() + "")
						.replace("{%deaths%}", p.getDeaths() + "")
						.replace("{%kd%}", p.getKD() + "")
						+ "\nplugin by &aBukkitSmerf", s);
	}
}
