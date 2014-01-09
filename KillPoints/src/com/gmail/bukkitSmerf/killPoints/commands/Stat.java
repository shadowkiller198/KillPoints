package com.gmail.bukkitSmerf.killPoints.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.bukkitSmerf.killPoints.LocalPlayer;
import com.gmail.bukkitSmerf.killPoints.Msg;
import com.gmail.bukkitSmerf.killPoints.PlayersHandler;
import com.gmail.bukkitSmerf.killPoints.Utils;

public class Stat implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (sender.hasPermission("Kills.OwnStats"))
				if (sender instanceof Player)
					return send(PlayersHandler.getPlayer(sender.getName()), sender);
				else
					return true;
			else
				return Utils.fixAndSend(Msg.getNoPermissions().replace("{%Permission%}", "Kills.OwnStats"), sender);
		} else {
			if (!sender.hasPermission("Kills.OtherStats"))
				return Utils.fixAndSend(Msg.getNoPermissions().replace("{%Permission%}", "Kills.OtherStats"), sender);
			LocalPlayer p = PlayersHandler.tryFindPlayer(args[0]);
			if (p != null)
				return send(p, sender);
			return send(PlayersHandler.getPlayer(sender.getName()), sender);
		}
	}

	private boolean send(LocalPlayer p, CommandSender s) {
		return Utils.fixAndSend(Msg.getStat().replace("{%player%}", p.getName()).replace("{%points%}", p.getPoints() + "").replace("{%kills%}", p.getKills() + "").replace("{%deaths%}", p.getDeaths() + "").replace("{%kd%}", p.getKD() + "") + "\nplugin by &aBukkitSmerf", s);
	}
}
