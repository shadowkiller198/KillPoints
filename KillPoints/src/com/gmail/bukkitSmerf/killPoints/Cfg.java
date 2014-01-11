package com.gmail.bukkitSmerf.killPoints;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Cfg {

	private static boolean useMySQL, aSyncAutoSave;
	private static String mySQLUser, mySQLDb, mySQLPass, mySQLHost;
	private static long startingPoints, autoSave;
	private static int negativeWhen, minPercentage, maxPercentage, percentageToGetNegative, minPoints, maxPoints, percentageToGetNegative2,
			minPoints2, maxPoints2, loseMinPercentage, loseMaxPercentage;
	private static Map<String, DmgCfg> dmgCfgs = new HashMap<String, DmgCfg>();

	public static DmgCfg getDmgCfg(String string) {
		return dmgCfgs.get(string);
	}

	public class DmgCfg {

		public final int losePercentage, loseMinPercentage, loseMaxPercentage;
		public final double multi;
		public final boolean deathMsgEnabled;
		public final String deathMsg;

		DmgCfg(int losePercentage, int loseMinPercentage, int loseMaxPercentage, double multi, boolean deathMsgEnabled, String deathMsg) {
			this.losePercentage = losePercentage;
			this.loseMinPercentage = loseMinPercentage;
			this.loseMaxPercentage = loseMaxPercentage;
			this.multi = multi;
			this.deathMsgEnabled = deathMsgEnabled;
			this.deathMsg = deathMsg;
		}
	}

	public Cfg() {
		try {
			Utils.log("&b Loading Config.yml...");
			File configFile = new File(KillPoints.getPluginDataFolder(), "config.yml");
			if (!configFile.exists()) {
				Utils.warn("&b I don't see any config file... Creating new one.");
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
				copy(KillPoints.getConfigRes(), configFile);
				Utils.log("&b Created new config file.");
			}
			FileConfiguration config = new YamlConfiguration();
			config.load(configFile);
			ConfigurationSection section = config.getConfigurationSection("General");
			useMySQL = section.getBoolean("UseMySQL", false);
			if (useMySQL) {
				Utils.log("&b MySQL enabled, starting connection.");
				ConfigurationSection mySQL = section.getConfigurationSection("MySQL");
				mySQLUser = mySQL.getString("User");
				mySQLDb = mySQL.getString("Db");
				mySQLPass = mySQL.getString("Pass");
				mySQLHost = mySQL.getString("Host");
			}
			Db.connectToDatabase();
			startingPoints = section.getLong("StartingPoints");
			autoSave = section.getInt("AutoSave", -1);
			if (autoSave > 0)
				autoSave *= 1200;
			aSyncAutoSave = section.getBoolean("aSyncAutoSave", false);
			section = config.getConfigurationSection("Kills");
			negativeWhen = section.getInt("NegativeWhen");
			minPercentage = section.getInt("MinPercentage");
			maxPercentage = section.getInt("MaxPercentage");
			percentageToGetNegative = section.getInt("PercentageToGetNegative");
			percentageToGetNegative2 = section.getInt("PercentageToGetNegative2");
			minPoints = section.getInt("MinPoints");
			maxPoints = section.getInt("MaxPoints");
			minPoints2 = section.getInt("MinPoints2");
			maxPoints2 = section.getInt("MaxPoints2");
			loseMinPercentage = section.getInt("LoseMinPercentage");
			loseMaxPercentage = section.getInt("LoseMaxPercentage");
			if (!section.isConfigurationSection("Deaths")) {
				for (int i = 0; i < 10; i++)
					Utils.warn("&4 Error when trying load config file! Update you config to 1.2!!\n http://pastebin.com/Ysap6wGm");
				Bukkit.shutdown();
				return;
			}
			section = section.getConfigurationSection("Deaths");
			for (DamageCause dc : DamageCause.values()) {
				String dmgc = dc.toString();
				if (!dc.equals(DamageCause.ENTITY_ATTACK) && section.isConfigurationSection(dmgc)) {
					ConfigurationSection dmgSection = section.getConfigurationSection(dmgc);
					dmgCfgs.put(dmgc, new DmgCfg(dmgSection.getInt("LosePercentage"), dmgSection.getInt("LoseMinPercentage"), dmgSection.getInt("LoseMaxPercentage"), dmgSection.getDouble("Multi"), dmgSection.getBoolean("DeathMsgEnabled"), dmgSection.getString("DeathMsg")));
				}
			}
			for (EntityType et : EntityType.values()) {
				String entt = et.toString();
				if (section.isConfigurationSection(entt)) {
					ConfigurationSection dmgSection = section.getConfigurationSection(entt);
					dmgCfgs.put(entt, new DmgCfg(dmgSection.getInt("LosePercentage"), dmgSection.getInt("LoseMinPercentage"), dmgSection.getInt("LoseMaxPercentage"), dmgSection.getDouble("Multi"), dmgSection.getBoolean("DeathMsgEnabled"), dmgSection.getString("DeathMsg")));
				}
			}
			new Msg(config.getConfigurationSection("Messages"));
			PlayersHandler.loadPlayers();
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

	public static long getAutoSave() {
		return autoSave;
	}

	public static boolean isaSyncAutoSave() {
		return aSyncAutoSave;
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
