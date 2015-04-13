package me.bugsyftw.heroes.listeners;

import me.bugsyftw.heroes.Heroes;
import me.bugsyftw.heroes.SelectMenu;
import me.bugsyftw.heroes.game.Game;
import me.bugsyftw.heroes.game.GameState;
import me.bugsyftw.heroes.player.HeroPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class ConnectionListener implements Listener {

	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
		Game game = Heroes.game;
		if (game != null && Bukkit.getOnlinePlayers().length <= game.getType().getMaxSpawns()){
			e.setJoinMessage("");
			e.getPlayer().setLevel(0);
			e.getPlayer().setHealth(20D);
			e.getPlayer().getInventory().clear();
		    for (PotionEffect effect : e.getPlayer().getActivePotionEffects())
		        e.getPlayer().removePotionEffect(effect.getType());
			HeroPlayer hp = HeroPlayer.fromPlayer(e.getPlayer());
		    SelectMenu.giveHeroSelect(e.getPlayer());
			game.addPlayer(hp, e.getPlayer());
		}
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		final Game game = Heroes.game;
		final Player p = e.getPlayer();
		if (game != null) {
			if (game.getState() == GameState.PREGAME && Bukkit.getOnlinePlayers().length <= game.getType().getMaxSpawns()) {
				final HeroPlayer hp = new HeroPlayer(p.getName());
				Heroes.getInstance().datahandler.addToDatabase(p);
				Heroes.getInstance().datahandler.getPlayerProfile(hp, p);
			} else if (game.getState() != GameState.PREGAME) {
				e.disallow(Result.KICK_FULL, ChatColor.RED + "This game session is in progress");
			}
		} else {
			p.sendMessage(ChatColor.RED + "No game for this server was created!");
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		final Game game = Heroes.game;
		final Player p = e.getPlayer();
		e.setQuitMessage("");
		if (game != null){
			Bukkit.getScheduler().scheduleSyncDelayedTask(Heroes.getInstance(), new BukkitRunnable() {
				@Override
				public void run() {
					game.onPlayerQuit(p);
					HeroPlayer hp = HeroPlayer.fromPlayer(p);
					Heroes.getInstance().datahandler.saveProfile(hp, p);
				}
			}, 5L);
		}
	}
	
	@EventHandler
	public void foodLevel(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onServerPing(ServerListPingEvent e) {
		if (Heroes.game != null){
			e.setMotd(Heroes.game.getState().toString() + ", " + Heroes.game.getType().getName());
			e.setMaxPlayers(Heroes.game.getType().getMaxSpawns());
		}
	}
}
