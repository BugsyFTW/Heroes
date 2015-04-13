package me.bugsyftw.heroes.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import me.bugsyftw.heroes.Heroes;
import me.bugsyftw.heroes.player.Hero;
import me.bugsyftw.heroes.player.HeroPlayer;
import me.bugsyftw.heroes.player.data.AchievementList;
import me.bugsyftw.heroes.player.data.HeroCoin;
import me.bugsyftw.heroes.player.data.KillDeathRatio;
import me.bugsyftw.heroes.player.data.PlayerProfile;
import me.bugsyftw.heroes.player.data.PlayerRank;
import me.bugsyftw.heroes.player.data.SkyRank;
import me.bugsyftw.heroes.player.data.WinKill;
import me.bugsyftw.heroes.player.data.WinLossRatio;
import me.bugsyftw.heroes.player.data.uuid.UUIDManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DataHandler {

	private Heroes plugin;
	public MySQL db;

	public DataHandler(Heroes plugin) {
		this.plugin = plugin;
	}
	
	public DataHandler() {}

	public void init() {
		db = new MySQL(plugin, "88.198.219.140", "3306", "skypiggies", "PiggyDev", "skypiggies");
		db.openConnection();
		System.out.println("Connection Succefully loaded");
	}

	public int getHeroCoins(Player p) {
		String uuid = UUIDManager.getUUIDFromPlayer(p.getName()).toString();
		if (!db.checkConnection()) db.openConnection();
		try {
			Statement query = db.getConnection().createStatement();
			ResultSet rs = query.executeQuery("SELECT * FROM 'playerdata' WHERE 'UUID'='" + uuid + "';");
			if (!rs.next()) return 0;
			return rs.getInt("herocoins");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;

	}

	public double getKD(HeroPlayer hp) {
		String uuid = UUIDManager.getUUIDFromPlayer(hp.getPlayer().getName()).toString();
		if (!db.checkConnection()) db.openConnection();
		try {
			Statement query = db.getConnection().createStatement();
			ResultSet rs = query.executeQuery("SELECT * FROM 'playerdata' WHERE 'UUID'='" + uuid + "';");
			if (!rs.next()) return 0.0D;
			return Double.valueOf(rs.getDouble("kdratio"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0.0D;
	}

	public double getWL(HeroPlayer hp) {
		String uuid = UUIDManager.getUUIDFromPlayer(hp.getPlayer().getName()).toString();
		if (!db.checkConnection()) db.openConnection();
		try {
			Statement query = db.getConnection().createStatement();
			ResultSet rs = query.executeQuery("SELECT * FROM 'playerdata' WHERE 'UUID'='" + uuid + "';");
			if (!rs.next()) return 0.0D;
			return Double.valueOf(rs.getDouble("wlratio"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0.0D;
	}

	public PlayerRank getRank(HeroPlayer hp) {
		String uuid = UUIDManager.getUUIDFromPlayer(hp.getPlayer().getName()).toString();
		if (!db.checkConnection()) db.openConnection();
		try {
			Statement query = db.getConnection().createStatement();
			ResultSet rs = query.executeQuery("SELECT * FROM 'playerdata' WHERE 'UUID'='" + uuid + "';");
			if (!rs.next()) return null;
			return new PlayerRank(SkyRank.fromString(rs.getString("rank")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PlayerProfile getPlayerProfile(HeroPlayer hp, Player p) {
		String uuid = UUIDManager.getUUIDFromPlayer(p.getName()).toString();
		Map<Hero, Boolean> heroes = new HashMap<Hero, Boolean>();
		Map<Hero, Integer> upgrades = new HashMap<Hero, Integer>();
		List<AchievementList> achieved = new ArrayList<AchievementList>();
		PlayerProfile profile = null;
		if (!db.checkConnection()) db.openConnection();
		try {
			PreparedStatement query = db.getConnection().prepareStatement("SELECT * FROM playerdata, herodata, achievements WHERE playerdata.UUID=? AND herodata.UUID=? AND achievements.UUID=? AND heroupgrades.UUID=?;");
			query.setString(1, uuid);
			query.setString(2, uuid);
			query.setString(3, uuid);
			query.setString(4, uuid);
			
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				for (Hero h : Hero.values()) {
					if(heroes.containsKey(h) == false)
						heroes.put(h, rs.getBoolean(h.getDataName()));
				}
				for (AchievementList al : AchievementList.values()) {
					if (rs.getString(al.dataName()).equalsIgnoreCase(al.dataName())) {
						achieved.add(AchievementList.fromDataID(rs.getString(al.dataName())));
						al.setAchieved(rs.getBoolean(al.dataName()));
					}
				}
				/** TODO IDK IF IT WILL WORK */
				for(Hero h : Hero.values()) {
					if (upgrades.containsKey(h) == false) {
						upgrades.put(h, rs.getInt(h.getDataName()));
					}
				}
				if (profile == null) {
					profile = new PlayerProfile(new HeroCoin(rs.getInt("herocoins")), new KillDeathRatio(Double.valueOf(rs.getDouble("kdratio"))), new WinLossRatio(Double.valueOf(rs.getDouble("wlratio"))), heroes, new PlayerRank(SkyRank.fromString(rs.getString("rank"))), achieved, new WinKill(rs.getInt("wins"), rs.getInt("kills")), upgrades);
				}
			}
			for(Entry<Hero, Boolean> h : profile.getHeroesMap().entrySet()){
				h.getKey().setUnlocked(h.getValue());
			}
			rs.close();
			query.close();
			return profile;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void saveProfile(HeroPlayer hp, Player p) {
		String uuid = UUIDManager.getUUIDFromPlayer(p.getName()).toString();
		PlayerProfile profile = hp.getProfile();
		if (!db.checkConnection()) db.openConnection();
		try {
			PreparedStatement query = db.getConnection().prepareStatement("UPDATE playerdata SET herocoins=?, kdratio=?, wlratio=?, rank=?, wins=?, kills=? WHERE UUID=?;");
			query.setInt(1, profile.getCoins().getAmount());
			query.setDouble(2, profile.getKD().getRatio());
			query.setDouble(3, profile.getWL().getRation());
			query.setString(4, profile.getRank().getRank().getName());
			query.setInt(5, profile.getWinKill().getWins());
			query.setInt(6, profile.getWinKill().getKills());
			query.setString(7, uuid);

			query.executeUpdate();
			query.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public List<Hero> getAvailableHeroes(HeroPlayer hp) {
		String uuid = UUIDManager.getUUIDFromPlayer(hp.getPlayer().getName()).toString();
		List<Hero> heroes = new ArrayList<Hero>();
		if (!db.checkConnection()) db.openConnection();
		try {
			Statement query = db.getConnection().createStatement();
			ResultSet rs = query.executeQuery("SELECT * FROM 'herodata' WHERE 'UUID'='" + uuid + "';");
			while (rs.next()) {
				for (Hero h : hp.getHero().values()) {
					heroes.add(Hero.fromString(rs.getString(h.getName())));
				}
			}
			return heroes;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addToDatabase(Player p) {
		String uuid = UUIDManager.getUUIDFromPlayer(p.getName()).toString();
		if (!db.checkConnection()) db.openConnection();
		try {
			if (containsPlayer(UUIDManager.getUUIDFromPlayer(p.getName()))) return;
			PreparedStatement pt = db.getConnection().prepareStatement("INSERT INTO playerdata (UUID,herocoins,kdratio,wlratio,rank,wins,kills) VALUES (?,0,0,0,?,0,0);");
			pt.setString(1, uuid);
			pt.setString(2, SkyRank.DEFAULT.getName());

			pt.execute();

			pt.close();

			PreparedStatement pt2 = db.getConnection().prepareStatement("INSERT INTO herodata VALUES (?,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0);");
			pt2.setString(1, uuid);

			pt2.execute();

			pt2.close();

			PreparedStatement pt3 = db.getConnection().prepareStatement("INSERT INTO achievements VALUES (?,0,0,0,0,0,0,0,0,0,0);");
			pt3.setString(1, uuid);

			pt3.execute();

			pt3.close();
			
			PreparedStatement pt4 = db.getConnection().prepareStatement("INSERT INTO heroupgrades VALUES (?,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);");
			pt4.setString(1, uuid);

			pt4.execute();

			pt4.close();

		} catch (SQLException e) {
			e.printStackTrace();
			p.kickPlayer(ChatColor.RED + "Something went wrong when you connected to the server, reconnect!");
		}
	}

	public boolean setRank(UUID uuid, SkyRank rank) {
		if (!db.checkConnection()) db.openConnection();
		if (containsPlayer(uuid)) {
			try {

				PreparedStatement pt = db.getConnection().prepareStatement("UPDATE playerdata SET rank=? WHERE UUID='?';");
				pt.setString(1, rank.getName());
				pt.setString(2, uuid.toString());

				pt.executeUpdate();
				pt.close();

				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	public synchronized boolean containsPlayer(UUID uuid) {
		try {
			PreparedStatement sql = db.getConnection().prepareStatement("SELECT * FROM `playerdata` WHERE UUID=?;");
			sql.setString(1, uuid.toString());

			ResultSet result = sql.executeQuery();
			boolean contains_player = result.next();

			sql.close();
			result.close();

			return contains_player;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public synchronized boolean containsServer(String name) {
		try {
			PreparedStatement sql = db.getConnection().prepareStatement("SELECT * FROM `serverdata` WHERE name=?;");
			sql.setString(1, name);

			ResultSet result = sql.executeQuery();
			boolean contains_server = result.next();

			sql.close();
			result.close();

			return contains_server;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Player getPlayerByUUID(UUID uuid) {
		for (Player p : Heroes.getInstance().getServer().getOnlinePlayers())
			if (p.getUniqueId().equals(uuid)) return p;

		throw new IllegalArgumentException();
	}
}
