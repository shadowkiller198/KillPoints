package com.gmail.bukkitSmerf.killPoints.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.bukkitSmerf.killPoints.Db;
import com.gmail.bukkitSmerf.killPoints.LocalPlayer;
import com.gmail.bukkitSmerf.killPoints.Msg;
import com.gmail.bukkitSmerf.killPoints.Utils;

public class TopKill implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (args.length == 0)
			return sendTop("p", 1, sender, false);
		else if (args.length == 1)
			if (args[0].equals("-a"))
				return sendTop(Msg.getPointsCmd(), 1, sender, true);
			else
				return sendTop(args[0], 1, sender, false);
		else if (args.length == 2)
			if (args[0].equals("-a"))
				return sendTop(args[1], 1, sender, true);
			else if (args[1].equals("-a"))
				return sendTop(args[0], 1, sender, true);
			else
				try {
					return sendTop(args[1], Integer.parseInt(args[0]), sender,
							false);
				} catch (NumberFormatException e) {
					try {
						return sendTop(args[0], Integer.parseInt(args[1]),
								sender, false);
					} catch (NumberFormatException e1) {
						return sendTop(args[0], 1, sender, false);
					}
				}
		else {
			int from = 1;
			boolean isAll = false;
			boolean getFrom = false;
			String topArg = null;
			for (String arg : args)
				try {
					if (args.equals("-a")) {
						isAll = true;
						continue;
					}
					if (!getFrom) {
						from = Integer.parseInt(args[0]);
						getFrom = true;
						continue;
					}
					topArg = arg;
				} catch (NumberFormatException e) {
					continue;
				}
			if (topArg != null)
				return sendTop(topArg, from, sender, isAll);
			return sendTop("p", from, sender, isAll);
		}
	}

	private static boolean sendTop(String arg0, int from, CommandSender sender,
			boolean toAll) {
		if (arg0.equalsIgnoreCase(Msg.getDeathsCmd())) {
			if (!toAll) {
				if (sender.hasPermission("Kills.TopList.Deaths.Normal")) {
					return Utils.fixAndSend(sendTopDeaths(from), sender);
				} else
					return Utils.fixAndSend(
							Msg.getNoPermissions().replace("{%Permission%}",
									"Kills.TopList.Deaths.Normal"), sender);
			} else {
				if (sender.hasPermission("Kills.TopList.Deaths.ToAll")) {
					return sendTopDeathsToAll(from);
				} else
					return Utils.fixAndSend(
							Msg.getNoPermissions().replace("{%Permission%}",
									"Kills.TopList.Deaths.ToAll"), sender);
			}
		} else if (arg0.equalsIgnoreCase(Msg.getKillsCmd())) {
			if (!toAll) {
				if (sender.hasPermission("Kills.TopList.Kills.Normal")) {
					return Utils.fixAndSend(sendTopKills(from), sender);
				} else
					return Utils.fixAndSend(
							Msg.getNoPermissions().replace("{%Permission%}",
									"Kills.TopList.Kills.Normal"), sender);
			} else {
				if (sender.hasPermission("Kills.TopList.Kills.ToAll")) {
					return sendTopKillsToAll(from);
				} else
					return Utils.fixAndSend(
							Msg.getNoPermissions().replace("{%Permission%}",
									"Kills.TopList.Kills.ToAll"), sender);
			}
		} else if (arg0.equalsIgnoreCase(Msg.getKdCmd())) {
			if (!toAll) {
				if (sender.hasPermission("Kills.TopList.Kd.Normal")) {
					return Utils.fixAndSend(sendTopKd(from), sender);
				} else
					return Utils.fixAndSend(
							Msg.getNoPermissions().replace("{%Permission%}",
									"Kills.TopList.Kd.Normal"), sender);
			} else {
				if (sender.hasPermission("Kills.TopList.Kd.ToAll")) {
					return sendTopKdToAll(from);
				} else
					return Utils.fixAndSend(
							Msg.getNoPermissions().replace("{%Permission%}",
									"Kills.TopList.Kd.ToAll"), sender);
			}
		} else {
			if (!toAll) {
				if (sender.hasPermission("Kills.TopList.Points.Normal")) {
					return Utils.fixAndSend(sendTopPoints(from), sender);
				} else
					return Utils.fixAndSend(
							Msg.getNoPermissions().replace("{%Permission%}",
									"Kills.TopList.Points.Normal"), sender);
			} else {
				if (sender.hasPermission("Kills.TopList.Points.ToAll")) {
					return sendTopPointsToAll(from);
				} else
					return Utils.fixAndSend(
							Msg.getNoPermissions().replace("{%Permission%}",
									"Kills.TopList.Points.ToAll"), sender);
			}
		}
	}

	private static String sendTopx(boolean toAll, String what, int from) {
		List<LocalPlayer> topList = null;
		topList = (what.equals("kd")) ? Db.getTopPoints(from) : (what
				.equals("k")) ? Db.getTopKills(from) : (what.equals("d")) ? Db
				.getTopDeaths(from) : Db.getTopPoints(from);
		String msg = Msg.getTopTop();
		for (LocalPlayer topPlayer : topList) {
			msg += "\n"
					+ Msg.getTopPoints()
							.replace("{%pos%}", from + "")
							.replace("{%player%}", topPlayer.getName())
							.replace(
									"{%top%}",
									((what.equals("kd")) ? topPlayer.getKD()
											: (what.equals("k")) ? topPlayer
													.getKills() : (what
													.equals("d")) ? topPlayer
													.getDeaths() : topPlayer
													.getPoints())
											+ "");
			from++;
		}
		msg += "\n" + Msg.getTopBot();
		if (!toAll)
			return msg;
		Bukkit.broadcastMessage(Utils.fixMsg(msg));
		return null;
	}

	private static String sendTopPoints(int from) {
		return sendTopx(false, "p", from);
	}

	private static boolean sendTopPointsToAll(int from) {
		sendTopx(true, "p", from);
		return true;
	}

	private static String sendTopKills(int from) {
		return sendTopx(false, "k", from);
	}

	private static boolean sendTopKillsToAll(int from) {
		sendTopx(true, "k", from);
		return true;
	}

	private static String sendTopDeaths(int from) {
		return sendTopx(false, "d", from);
	}

	private static boolean sendTopDeathsToAll(int from) {
		sendTopx(true, "d", from);
		return true;
	}

	private static String sendTopKd(int from) {
		return sendTopx(false, "kd", from);
	}

	private static boolean sendTopKdToAll(int from) {
		sendTopx(true, "kd", from);
		return true;
	}
}