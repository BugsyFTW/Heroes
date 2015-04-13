package me.bugsyftw.heroes.game;

import java.util.ArrayList;
import java.util.List;

import me.bugsyftw.heroes.Heroes;
import me.bugsyftw.heroes.game.games.FreeForAll;
import me.bugsyftw.heroes.game.games.OneVSOne;
import me.bugsyftw.heroes.game.games.Tournament;
import me.bugsyftw.heroes.player.HeroPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public abstract class Game {

	public GameType type;
	public SpawnCollection spawns;
	public GameState state = GameState.PREGAME;
	public boolean endRound = false;
	boolean special = false;
	public boolean inCountdown = false;

	public static String PREFIX = "§7[§cHB§7]§r ";

	public Scoreboard board;
	public Objective ob;

	public List<String> players = new ArrayList<String>();

	public static void createGame(GameType type) {
		Heroes.getInstance().getConfig().set("Type", type.toString());
		Heroes.getInstance().saveConfig();
		switch (type) {
		case ONE_VS_ONE:
			OneVSOne ovo = new OneVSOne();
			Heroes.game = ovo;
			break;
		case FREE_FOR_ALL:
			FreeForAll ffa = new FreeForAll();
			Heroes.game = ffa;
			break;
		case TOURNAMENT:
			Tournament t = new Tournament();
			Heroes.game = t;
			break;
		default:
			break;
		}
	}

	public void loadGame() {
		this.type = GameType.fromConfigString(Heroes.getInstance().getConfig().getString("Type"));
		spawns = new SpawnCollection(this, SpawnCollection.getLocations());
		switch (type) {
		case ONE_VS_ONE:
			OneVSOne ovo = new OneVSOne();
			Heroes.game = ovo;
			break;
		case FREE_FOR_ALL:
			FreeForAll ffa = new FreeForAll();
			Heroes.game = ffa;
			break;
		case TOURNAMENT:
			Tournament t = new Tournament();
			Heroes.game = t;
			break;
		default:
			break;
		}
	}

	public abstract void addPlayer(HeroPlayer hp, Player p);

	public abstract void startGame(Player p);

	public abstract void endGame(Player p);

	public abstract void addKill(Player killer, Player killed);

	public abstract void giveScoreboard(final Player p);

	public void onPlayerQuit(Player p) {
		if (state == GameState.PREGAME) {
			players.remove(p.getName());
			broadcastMessage(p.getName() + ChatColor.GRAY + " has left the battle! " + ChatColor.GOLD + "(" + getGameSize() + "/" + getType().getMaxSpawns() + ")");
		} else if (state == GameState.PLAYING) {
			players.remove(p.getName());
			broadcastMessage(p.getName() + ChatColor.GRAY + " has left the battle!");
			if (Bukkit.getServer().getOnlinePlayers().length < type.getMinSpawns()) {
				forceEndGame();
			}
		}
	}

	public void startCountdown() {
		inCountdown = true;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Heroes.getInstance(), new Runnable() {
			int timer = 20;

			public void run() {
				if (timer != -1) {
					if (timer != 0) {
						for(Player p : Bukkit.getServer().getOnlinePlayers()){
							p.setLevel(timer);
						}
						timer--;
					} else {
						timer--;
						if (getGameSize() < getType().getMinSpawns()) {
							broadcastMessage(PREFIX + ChatColor.RED + "Not enough players to start the game. Restarting...");
							forceEndGame();
						}
						setState(GameState.PLAYING);
						for(Player p : Bukkit.getServer().getOnlinePlayers()){
							startGame(p);
						}
					}
				}
				;
			}
		}, 0L, 20L);
	}

	@SuppressWarnings("deprecation")
	public void spawnPlayers() {
		ArrayList<Location> loc = new ArrayList<Location>(spawns.getLocationsCollection());
		int size = loc.size() - 1;
		for (final String all : players) {
			final Player p = Bukkit.getServer().getPlayer(all);
			p.teleport(loc.get(size));
			size--;
		}

	}
	
	@SuppressWarnings("deprecation")
	public void forceEndGame() {
		if (getState() == GameState.PREGAME) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Heroes.getInstance(), new BukkitRunnable() {
				@Override
				public void run() {
					for (final String all : players) {
						final Player on = Bukkit.getServer().getPlayer(all);
						HeroPlayer hp = HeroPlayer.fromPlayer(on);
						Heroes.getInstance().datahandler.saveProfile(hp, on);
						Heroes.sendToHub(on);
					}
				}
			}, (20 * 5));
			Bukkit.getScheduler().scheduleSyncDelayedTask(Heroes.getInstance(), new BukkitRunnable() {
				@Override
				public void run() {
					setState(GameState.OFFLINE);
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "restart");
				}
			}, (20 * 10));
		} else if (getState() == GameState.PLAYING) {
			setState(GameState.ENDGAME);
			for (final String all : players) {
				final Player on = Bukkit.getServer().getPlayer(all);
				HeroPlayer hp = HeroPlayer.fromPlayer(on);
				hp.undisguisePlayer();
				hp.unreadyHero();
				on.getInventory().clear();
				on.setHealth(20D);
				Heroes.getInstance().datahandler.saveProfile(hp, on);
			}
			broadcastMessage(PREFIX + "Server Restarting...");
			Bukkit.getScheduler().scheduleSyncDelayedTask(Heroes.getInstance(), new BukkitRunnable() {
				@Override
				public void run() {
					for (final String all : players) {
						final Player p = Bukkit.getServer().getPlayer(all);
						Heroes.sendToHub(p);
					}
				}
			}, (20 * 5));
			Bukkit.getScheduler().scheduleSyncDelayedTask(Heroes.getInstance(), new BukkitRunnable() {
				@Override
				public void run() {
					setState(GameState.OFFLINE);
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "restart");
				}
			}, (20 * 10));
		}
	}

	public Scoreboard getBoard() {
		return board;
	}

	public void setValidConfig(boolean b) {
		Heroes.getInstance().getConfig().set("Valid", b);
	}

	public boolean isValid() {
		if (Heroes.game != null) return true;
		return false;
	}

	public void broadcastMessage(String msg) {
		Bukkit.getServer().broadcastMessage(msg);
	}

	public SpawnCollection getSpawnCollection() {
		return spawns;
	}

	public GameType getType() {
		return type;
	}

	public void setType(GameType type) {
		this.type = type;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public int getGameSize() {
		return Bukkit.getServer().getOnlinePlayers().length;
	}

	public boolean doSpecial() {

		double r = Math.random() * 100;

		if (r <= 5) {
			special = true;
			return true;
		}
		special = false;
		return false;
	}

	public boolean isSpecial() {
		return special;
	}
}
