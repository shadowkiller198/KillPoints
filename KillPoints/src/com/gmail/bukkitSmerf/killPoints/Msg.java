package com.gmail.bukkitSmerf.killPoints;

import org.bukkit.configuration.ConfigurationSection;

public class Msg {
	private static String noPermissions, pointsCmd, killsCmd, deathsCmd, kdCmd,
			onKill, lose, win, newTop, pointsTop, killsTop, deathsTop, kdTop,
			topTop, topBot, topPoints, topDeaths, topKills, topKd, stat;

	public static String getTopTop() {
		return topTop;
	}

	public static String getTopBot() {
		return topBot;
	}

	public static String getTopPoints() {
		return topPoints;
	}

	public static String getTopDeaths() {
		return topDeaths;
	}

	public static String getTopKills() {
		return topKills;
	}

	public static String getTopKd() {
		return topKd;
	}

	public static String getOnKill() {
		return onKill;
	}

	public static String getLose() {
		return lose;
	}

	public static String getWin() {
		return win;
	}

	public static String getNewTop() {
		return newTop;
	}

	public static String getPointsTop() {
		return pointsTop;
	}

	public static String getKillsTop() {
		return killsTop;
	}

	public static String getDeathsTop() {
		return deathsTop;
	}

	public static String getKdTop() {
		return kdTop;
	}

	public static String getNoPermissions() {
		return noPermissions;
	}

	public static String getPointsCmd() {
		return pointsCmd;
	}

	public static String getKillsCmd() {
		return killsCmd;
	}

	public static String getDeathsCmd() {
		return deathsCmd;
	}

	public static String getKdCmd() {
		return kdCmd;
	}

	public static String getStat() {
		return stat;
	}

	public Msg(ConfigurationSection msgCfg) {
		noPermissions = msgCfg.getString("NoPermissions");
		pointsCmd = msgCfg.getString("TopListCmd.Points");
		killsCmd = msgCfg.getString("TopListCmd.Kills");
		deathsCmd = msgCfg.getString("TopListCmd.Deaths");
		kdCmd = msgCfg.getString("TopListCmd.KD");
		onKill = msgCfg.getString("OnKill");
		lose = msgCfg.getString("Lose");
		win = msgCfg.getString("Win");
		newTop = msgCfg.getString("NewTop");
		pointsTop = msgCfg.getString("PointsTop");
		killsTop = msgCfg.getString("KillsTop");
		deathsTop = msgCfg.getString("DeathsTop");
		kdTop = msgCfg.getString("KdTop");
		topTop = msgCfg.getString("TopList.Top");
		topBot = msgCfg.getString("TopList.Bot");
		topPoints = msgCfg.getString("TopList.Points");
		topKills = msgCfg.getString("TopList.Kills");
		topDeaths = msgCfg.getString("TopList.Deaths");
		topKd = msgCfg.getString("TopList.KD");
		stat = msgCfg.getString("Stat");
	}
}
