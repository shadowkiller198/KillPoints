package com.gmail.bukkitSmerf.killPoints;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Cfg {
	private static Map<String, LocalPlayer> players = new HashMap<String, LocalPlayer>();
	private static boolean useMySQL;
	private static String mySQLUser, mySQLDb, mySQLPass, mySQLHost;
	private static long startingPoints;
	private static int negativeWhen, minPercentage, maxPercentage,
			percentageToGetNegative, minPoints, maxPoints,
			percentageToGetNegative2, minPoints2, maxPoints2,
			loseMinPercentage, loseMaxPercentage;

	public static void addPlayer(LocalPlayer player) {
		players.put(player.getName().toLowerCase(), player);
	}

	public static LocalPlayer getPlayer(String name) {
		if (players.containsKey(name.toLowerCase()))
			return players.get(name.toLowerCase());
		return null;
	}

	public static boolean isLoadedPlayer(String name) {
		if (players.containsKey(name.toLowerCase()))
			return true;
		return false;
	}

	public static void removePlayer(String name) {
		if (players.containsKey(name.toLowerCase())){
			players.remove(name.toLowerCase());
		}
	}

	public Cfg() {
		try {
			Utils.log("&b Loading Config.yml...");
			File configFile = new File(KillPoints.getPluginDataFolder(),
					"config.yml");
			if (!configFile.exists()) {
				Utils.warn("&b I don't see any config file... Creating new one.");
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
				copy(KillPoints.getConfigRes(), configFile);
				Utils.log("&b Created new config file.");
			}
			FileConfiguration config = new YamlConfiguration();
			config.load(configFile);
			ConfigurationSection section = config
					.getConfigurationSection("General");
			useMySQL = section.getBoolean("UseMySQL", false);
			if (useMySQL) {
				Utils.log("&b MySQL enabled, starting connection.");
				ConfigurationSection mySQL = section
						.getConfigurationSection("MySQL");
				mySQLUser = mySQL.getString("User");
				mySQLDb = mySQL.getString("Db");
				mySQLPass = mySQL.getString("Pass");
				mySQLHost = mySQL.getString("Host");
			}
			Db.connectToDatabase();
			startingPoints = section.getLong("StartingPoints");
			section = config.getConfigurationSection("Kills");
			negativeWhen = section.getInt("NegativeWhen");
			minPercentage = section.getInt("MinPercentage");
			maxPercentage = section.getInt("MaxPercentage");
			percentageToGetNegative = section.getInt("PercentageToGetNegative");
			percentageToGetNegative2 = section
					.getInt("PercentageToGetNegative2");
			minPoints = section.getInt("MinPoints");
			maxPoints = section.getInt("MaxPoints");
			minPoints2 = section.getInt("MinPoints2");
			maxPoints2 = section.getInt("MaxPoints2");
			loseMinPercentage = section.getInt("LoseMinPercentage");
			loseMaxPercentage = section.getInt("LoseMaxPercentage");
			new Msg(config.getConfigurationSection("Messages"));
			Db.loadPlayers();
			Utils.log("&b Loaded config file.");
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			for (int i = 0; i < 5; i++)
				Utils.warn("&4 Error when trying create or load config file! Check your server files!");
		}
	}

	public static boolean isUseMySQL() {
		return useMySQL;
	}

	public static String getMySQLUser() {
		return mySQLUser;
	}

	public static String getMySQLDb() {
		return mySQLDb;
	}

	public static String getMySQLPass() {
		return mySQLPass;
	}

	public static String getMySQLHost() {
		return mySQLHost;
	}

	public static long getStartingPoints() {
		return startingPoints;
	}

	public static int getNegativeWhen() {
		return negativeWhen;
	}

	public static int getMinPercentage() {
		return minPercentage;
	}

	public static int getMaxPercentage() {
		return maxPercentage;
	}

	public static int getPercentageToGetNegative() {
		return percentageToGetNegative;
	}

	public static int getPercentageToGetNegative2() {
		return percentageToGetNegative2;
	}

	public static int getMinPoints() {
		return minPoints;
	}

	public static int getMaxPoints() {
		return maxPoints;
	}

	public static int getMinPoints2() {
		return minPoints2;
	}

	public static int getMaxPoints2() {
		return maxPoints2;
	}

	public static int getLoseMinPercentage() {
		return loseMinPercentage;
	}

	public static int getLoseMaxPercentage() {
		return loseMaxPercentage;
	}

	public static void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
