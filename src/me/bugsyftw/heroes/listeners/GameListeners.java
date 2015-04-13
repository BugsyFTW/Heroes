package me.bugsyftw.heroes.listeners;

import me.bugsyftw.heroes.Heroes;
import me.bugsyftw.heroes.abilities.AbilityListener;
import me.bugsyftw.heroes.game.Game;
import me.bugsyftw.heroes.game.GameState;
import me.bugsyftw.heroes.player.HeroPlayer;
import net.minecraft.server.v1_7_R2.EnumClientCommand;
import net.minecraft.server.v1_7_R2.PacketPlayInClientCommand;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Egg;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GameListeners implements Listener {

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		e.setDeathMessage("");
		Player p = e.getEntity();
		e.getDrops().clear();
		e.setDroppedExp(0);
		((CraftPlayer) p).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
		if (e.getEntity().getKiller() instanceof Egg) {
			HeroPlayer hp = HeroPlayer.fromPlayer(p);
			Player k = hp.getLastDamager();
			Game game = Heroes.game;
			if (game != null && game.getState() == GameState.PLAYING) {
				game.addKill(k, (Player) e.getEntity());
			}
		} else if (e.getEntity().getKiller() instanceof Player) {
			DamageCause cause = p.getLastDamageCause().getCause();
			if (cause == DamageCause.ENTITY_ATTACK) {
				Player k = p.getKiller();
				Game game = Heroes.game;
				if (game != null && game.getState() == GameState.PLAYING) {
					game.addKill(k, (Player) e.getEntity());
				}
			}
		} else if ((e.getEntity().getKiller() instanceof Player) == false && ((e.getEntity().getKiller()) instanceof Projectile) == false) {
			HeroPlayer hp = HeroPlayer.fromPlayer(p);
			Player k = hp.getLastDamager();
			Game game = Heroes.game;
			if (game != null && game.getState() == GameState.PLAYING) {
				game.addKill(k, (Player) e.getEntity());
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Game game = Heroes.game;
			if (game.getState() != GameState.PLAYING) {
				e.setCancelled(true);
			} else {
				e.setCancelled(false);
				e.getEntity().getLocation().getWorld().playEffect(e.getEntity().getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
				HeroPlayer hp = HeroPlayer.fromPlayer((Player) e.getEntity());
				hp.setLastDamager((Player) e.getDamager());
			}
		} else if (e.getEntity() instanceof Player && e.getDamager() instanceof Projectile) {
			Game game = Heroes.game;
			if (game.getState() == GameState.PLAYING) {
				Projectile proj = (Projectile) e.getDamager();
				Player shooter = (Player) proj.getShooter();
				e.setDamage(4);
				e.getEntity().getLocation().getWorld().playEffect(e.getEntity().getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
				HeroPlayer hp = HeroPlayer.fromPlayer((Player) e.getEntity());
				hp.setLastDamager(shooter);
			}
		} else if (e.getEntity() instanceof Player && e.getDamager() instanceof LightningStrike) {
			LightningStrike s = (LightningStrike) e.getDamager();
			String name = AbilityListener.strikes.get(s.getUniqueId());
			Player striker = Bukkit.getServer().getPlayer(name);
			e.setDamage(8);
			HeroPlayer hp = HeroPlayer.fromPlayer((Player) e.getEntity());
			hp.setLastDamager(striker);
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Game game = Heroes.game;
			if (game.getState() != GameState.PLAYING) {
				e.setCancelled(true);
			} else {
				e.getEntity().getLocation().getWorld().playEffect(e.getEntity().getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
			}
		}
	}

	@EventHandler
	public void onEntityRespawn(PlayerRespawnEvent e) {
		Game game = Heroes.game;
		if (game.getState() == GameState.PLAYING) {
			if (game.players.contains(e.getPlayer().getName())) {
				e.setRespawnLocation(game.getSpawnCollection().getRandomSpawn());
			}
		}
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onCreatureRespawn(CreatureSpawnEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onInventoryScrool(PlayerItemHeldEvent e) {
		if (e.getNewSlot() != e.getPreviousSlot()) e.getPlayer().setLevel(0);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (Heroes.game != null) {
			Player p = e.getPlayer();
			if (Heroes.game.players.contains(p.getName())) {
				HeroPlayer hp = HeroPlayer.fromPlayer(p);
				String hero = hp.getHero() == null ? "" : hp.getHero().getName();
				e.setFormat(hp.getProfile().getRank().getRank().getName() + "\u276F§8<§r" + p.getName() + "§e\u276F§r" + hero + "§8>§r: " + e.getMessage());
			}
		}
	}

	@EventHandler
	public void onPlayerWalk(PlayerMoveEvent e) {
		if (Heroes.game != null && Heroes.game.endRound == true && Heroes.game.getState() == GameState.PLAYING) {
			if (((e.getTo().getX() != e.getFrom().getX()) || (e.getTo().getZ() != e.getFrom().getZ()))) {
				e.setTo(e.getFrom());
				return;
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (Heroes.game != null) e.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (Heroes.game != null) e.setCancelled(true);
	}
}
