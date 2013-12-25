package com.gmail.bukkitSmerf.killPoints;

import java.util.Random;

public class LocalPlayer extends Object {

	@Override
	public String toString() {
		return "{" + playerName + ":\n  Kills: " + kills + "\n  Points: "
				+ points + "\n  Deaths: " + deaths + "}";
	}

	protected int kills;
	protected long points;
	protected int deaths;
	protected final String playerName;

	public LocalPlayer(String playerName) {
		this.playerName = playerName.toLowerCase();
		this.kills = 0;
		this.deaths = 0;
		this.points = Cfg.getStartingPoints();
	}

	public LocalPlayer(String playerName, int kills, int deaths, long points) {
		this.playerName = playerName.toLowerCase();
		this.kills = kills;
		this.deaths = deaths;
		this.points = points;
	}

	private static Random rand = new Random();

	public long addKill(LocalPlayer killed) {
		kills++;
		long killedPoints = killed.getPoints();
		long result = 0;
		if (killedPoints < 1000) {
			result = ((rand.nextInt(Cfg.getMaxPoints() - Cfg.getMinPoints()) + Cfg
					.getMinPoints()) * (((rand.nextInt(100) + Cfg
					.getPercentageToGetNegative()) >= 100) ? -1 : 1));
		} else {
			if (killedPoints > points) {
				long pointsResult = killedPoints - points;
				if (pointsResult >= 0) {
					result = pointsResult
							- (((rand.nextInt(Cfg.getMaxPercentage()
									- Cfg.getMinPercentage()) + Cfg
										.getMinPercentage()) * (pointsResult)) / 100);
				} else {
					result = ((rand.nextInt(Cfg.getMaxPoints()
							- Cfg.getMinPoints()) + Cfg.getMinPoints()) * (((rand
							.nextInt(100) + Cfg.getPercentageToGetNegative()) >= 100) ? -1
							: 1));
				}
			} else {
				result = ((rand.nextInt(Cfg.getMaxPoints2()
						- Cfg.getMinPoints2()) + Cfg.getMinPoints2()) * (((rand
						.nextInt(100) + Cfg.getPercentageToGetNegative2()) >= 100) ? -1
						: 1));
			}
		}
		addPoints(result);
		return result;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public void addPoints(long points) {
		this.points += points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public long addDeath(long winPoints) {
		deaths++;
		long result = ((rand.nextInt(Cfg.getLoseMaxPercentage()
				- Cfg.getLoseMinPercentage()) + Cfg.getLoseMinPercentage()) * winPoints) / 100;
		addPoints(-result);
		return result;
	}

	public void setDeath(int deaths) {
		this.deaths = deaths;
	}

	public String getName() {
		return playerName;
	}

	public double getKD() {
		if (deaths == 0)
			return kills;
		return kills / deaths;
	}

	public int getKills() {
		return kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public long getPoints() {
		return points;
	}
}
