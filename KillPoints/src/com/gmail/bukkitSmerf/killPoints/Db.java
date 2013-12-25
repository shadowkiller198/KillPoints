package com.gmail.bukkitSmerf.killPoints;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Db {
	private static String url;
	private static String driver;
	private static Statement st;
	private static Connection con = null;
	private static boolean useMySQl = false;

	private Db(String host, String db, String username, String password) {
		url = "jdbc:mysql://" + host + "/" + db + "?user=" + username
				+ "&password=" + password;
		driver = ("com.mysql.jdbc.Driver");
	}

	private Db(String filePath) {
		url = "jdbc:sqlite:" + new File(filePath).getAbsolutePath();
		driver = ("org.sqlite.JDBC");
	}

	private Connection open(int type) throws SQLException,
			ClassNotFoundException {
		Class.forName(driver);
		con = DriverManager.getConnection(url);
		return con;
	}

	private static void update(String sql) {
		try {
			st.executeUpdate(sql);
		} catch (SQLException e) {
		}
	}

	private static ResultSet query(String sql) throws SQLException {
		return st.executeQuery(sql);
	}

	public static boolean connectToDatabase() {
		try {
			if (Cfg.isUseMySQL()) {
				con = new Db(Cfg.getMySQLHost(), Cfg.getMySQLDb(),
						Cfg.getMySQLUser(), Cfg.getMySQLPass()).open(0);
				st = con.createStatement();
				update("CREATE TABLE IF NOT EXISTS bukkitsmerf (player VARCHAR(20), kills INT(7), deaths INT(7), kd DOUBLE, points BIGINT);");
				useMySQl = true;
				Utils.log("&a Connected to MySQL!");
			} else {
				con = new Db(KillPoints.getPluginDataFolder() + File.separator
						+ "SQLite.db").open(0);
				st = con.createStatement();
				update("CREATE TABLE IF NOT EXISTS bukkitsmerf (player, kills, deaths, kd, points);");
				Utils.log("&a Connected to SQLite!");
			}
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			Utils.warn("&4 Error when try connect to DataBase. &c(is MySQL: "
					+ useMySQl + ")");
			return false;
		}
	}

	public static void loadPlayers() {
		for (Player p : Bukkit.getOnlinePlayers())
			loadPlayer(p.getName());
	}

	public static LocalPlayer getPlayer(String playerName) {
		playerName = playerName.toLowerCase();
		try {
			ResultSet result = query("SELECT kills,points,deaths FROM bukkitsmerf WHERE player='"
					+ playerName + "'");
			if (result.next())
				return new LocalPlayer(playerName, result.getInt("kills"),
						result.getInt("deaths"), result.getLong("points"));
			else
				return new LocalPlayer(playerName);
		} catch (SQLException e) {
			e.printStackTrace();
			Utils.warn("Error when try load: " + playerName + " from dataBase");
			return null;
		}
	}

	public static void loadPlayer(String playerName) {
		playerName = playerName.toLowerCase();
		try {
			if (isPlayer(playerName)) {
				ResultSet result = query("SELECT * FROM bukkitsmerf WHERE player='"
						+ playerName + "'");
				if (result.next()) {
					Cfg.addPlayer(new LocalPlayer(playerName, result
							.getInt("kills"), result.getInt("deaths"), result
							.getLong("points")));
				}
			} else {
				update("INSERT INTO bukkitsmerf (player, kills, deaths, kd, points) VALUES ('"
						+ playerName
						+ "', 0, 0, 0, "
						+ Cfg.getStartingPoints()
						+ ");");
				loadPlayer(playerName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Utils.warn("Error when try load: " + playerName + " from dataBase");
		}
	}

	public static void savePlayers() {
		for (Player p : Bukkit.getOnlinePlayers())
			savePlayer(Cfg.getPlayer(p.getName()));
	}

	public static void savePlayer(LocalPlayer player) {
		update("UPDATE bukkitsmerf SET kills=" + player.kills + ", deaths="
				+ player.deaths + ", kd=" + player.getKD() + ", points="
				+ player.points + " WHERE player='" + player.playerName + "'");
	}

	public static boolean isPlayer(String playerName) {
		playerName = playerName.toLowerCase();
		try {
			ResultSet result = query("SELECT player FROM bukkitsmerf WHERE player='"
					+ playerName + "'");
			if (result.next())
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			Utils.warn("Error when try check if: " + playerName
					+ " is in dataBase");
			return false;
		}
	}

	public static List<LocalPlayer> getTopKills(int from) {
		return getTop("kills", from);
	}

	public static List<LocalPlayer> getTopDeaths(int from) {
		return getTop("deaths", from);
	}

	public static List<LocalPlayer> getTopPoints(int from) {
		return getTop("points", from);
	}

	public static List<LocalPlayer> getTopKD(int from) {
		return getTop("kd", from);
	}

	private static List<LocalPlayer> getTop(String col, int from) {
		try {
			savePlayers();
			ArrayList<LocalPlayer> top = new ArrayList<LocalPlayer>();
			ResultSet result = query("SELECT player FROM bukkitsmerf ORDER BY "
					+ col + " " + "DESC LIMIT " + (from - 1) + ",10");
			ArrayList<String> players = new ArrayList<String>();
			while (result.next())
				players.add(result.getString("player"));
			for (String p : players)
				if (Cfg.isLoadedPlayer(p))
					top.add(Cfg.getPlayer(p));
				else
					top.add(getPlayer(p));
			return top;
		} catch (SQLException e) {
			e.printStackTrace();
			Utils.warn("Error when try get TopList from database");
			return new ArrayList<LocalPlayer>();
		}
	}
}
