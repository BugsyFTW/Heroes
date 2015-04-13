package me.bugsyftw.heroes.game.games;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import me.bugsyftw.heroes.Heroes;
import me.bugsyftw.heroes.game.Game;
import me.bugsyftw.heroes.game.GameState;
import me.bugsyftw.heroes.player.Hero;
import me.bugsyftw.heroes.player.HeroPlayer;

public class FreeForAll extends Game {

	private boolean spawned = false;

	@Override
	public void addPlayer(HeroPlayer hp, Player p) {
		if (Heroes.game.isValid()) {
			if (!players.contains(p.getName())) {
				players.add(p.getName());
				if(hp == null)
					hp = new HeroPlayer(p.getName());
				broadcastMessage(PREFIX + hp.getPlayer().getName() + ChatColor.GRAY + " has joined the battle! " + ChatColor.GOLD + "(" + getGameSize() + "/" + getType().getMaxSpawns() + ")");
				hp.setHero(Hero.HAWKEYE); // Default Hero
				hp.setProfile(Heroes.getInstance().datahandler.getPlayerProfile(hp, p));
				if (getGameSize() >= getType().getMinSpawns() && getGameSize() <= getType().getMaxSpawns() && !inCountdown) {
					startCountdown();
					broadcastMessage(PREFIX + ChatColor.GOLD + "Game Starting in 20 seconds!");
				}
			}
		}

	}

	@Override
	public void startGame(final Player p) {
		endRound = true;
		if (!spawned) {
			spawnPlayers();
			spawned = true;
		}
		if (isSpecial()) {
			p.sendMessage(PREFIX + ChatColor.GRAY + "This Game is special! A bonus will be added to the winner!");
		}
		giveScoreboard(p);
		p.getInventory().clear();
		p.setLevel(0);
		final HeroPlayer hp = HeroPlayer.fromPlayer(p);
		hp.heal();
		hp.disguisePlayer();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Heroes.getInstance(), new BukkitRunnable() {
			int timer = 3;

			public void run() {
				if (timer != -1) {
					if (timer != 0) {
						p.sendMessage(PREFIX + "§oGame starts in " + timer);
						timer--;
					} else {
						timer--;
						p.sendMessage(PREFIX + "§aFIGHT");
						endRound = false;
						hp.giveInventory();
					}
				}
			}
		}, 0L, 20L);

	}

	@Override
	public void endGame(Player winner) {
		setState(GameState.ENDGAME);
		broadcastMessage(PREFIX + "§8" + winner.getName() + " §r§ohas won the game!");
		HeroPlayer h = HeroPlayer.fromPlayer(winner);
		h.getProfile().getWinKill().addWin(1);
		if (isSpecial()) {
			winner.sendMessage(PREFIX + ChatColor.GREEN + "You have gained §a+[300HC]" + ChatColor.GREEN + " for winnnig the game!" + ChatColor.RED + "(Special Game)");
			h.getProfile().getCoins().addHeroCoins(300);
		} else {
			winner.sendMessage(PREFIX + ChatColor.GREEN + "You have gained §a+[30HC]" + ChatColor.GREEN + " for winnnig the game!");
			h.getProfile().getCoins().addHeroCoins(30);
		}
		for (Player all : Bukkit.getServer().getOnlinePlayers()) {
			HeroPlayer hp = HeroPlayer.fromPlayer(all);
			hp.undisguisePlayer();
			hp.unreadyHero();
		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(Heroes.getInstance(), new BukkitRunnable() {
			@Override
			public void run() {
				for (final Player on : Bukkit.getServer().getOnlinePlayers()) {
					Heroes.sendToHub(on);
				}
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "restart");
			}
		}, (20 * 10));

	}

	@SuppressWarnings("deprecation")
	@Override
	public void addKill(Player killer, Player killed) {
		if (isValid() && Heroes.game != null) {
			if (getState() == GameState.PLAYING) {
				HeroPlayer hp = HeroPlayer.fromPlayer(killer);
				hp.addKill(1);
				hp.getProfile().getCoins().addHeroCoins(5);
				hp.getPlayer().sendMessage(PREFIX + "§a+[5HC] §rfor killing " + killed.getName());
				Heroes.sendToHub(killed);
				board.getObjective(DisplaySlot.SIDEBAR).getScore(killer).setScore(hp.getKills());
				board.getObjective(DisplaySlot.SIDEBAR).getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.RED + "Players left:")).setScore(Bukkit.getServer().getOnlinePlayers().length);
				if (Bukkit.getServer().getOnlinePlayers().length == 1) {
					endGame(killer);
				}
			}
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void giveScoreboard(final Player p) {
		board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		ob = board.registerNewObjective("game_score", "dummy");
		ob.setDisplayName(ChatColor.YELLOW + "Timer");
		ob.setDisplaySlot(DisplaySlot.SIDEBAR);
		for (Player on : Bukkit.getServer().getOnlinePlayers()) {
			ob.getScore(on).setScore(0);
		}
		ob.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.RED + "Players left:")).setScore(Bukkit.getServer().getOnlinePlayers().length);
		;
		p.setScoreboard(board);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Heroes.getInstance(), new BukkitRunnable() {
			int min = 9;
			int sec = 60;

			@Override
			public void run() {
				if (min != -1) {
					if (min != 0) {
						if (sec != 0) {
							sec--;
							if (sec < 10) {
								ob.setDisplayName(PREFIX + "FFA §6" + min + ":0" + sec);
								p.setScoreboard(board);
							} else {
								ob.setDisplayName(PREFIX + "FFA §6" + min + ":" + sec);
								p.setScoreboard(board);
							}
						} else {
							ob.setDisplayName(PREFIX + "FFA §6" + min + ":0" + sec);
							p.setScoreboard(board);
							min--;
							sec = 60;
						}
					} else {
						min--;
						forceEndGame();
					}
				}
				;
			}
		}, 0L, 20L);
	}
}
