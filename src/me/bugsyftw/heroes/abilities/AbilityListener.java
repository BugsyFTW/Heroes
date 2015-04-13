package me.bugsyftw.heroes.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import me.bugsyftw.heroes.Heroes;
import me.bugsyftw.heroes.game.Game;
import me.bugsyftw.heroes.game.GameState;
import me.bugsyftw.heroes.player.Hero;
import me.bugsyftw.heroes.player.HeroPlayer;
import me.bugsyftw.heroes.projectiles.BlockProjectile;
import me.bugsyftw.heroes.projectiles.CustomProjectile;
import me.bugsyftw.heroes.projectiles.ItemProjectile;
import me.bugsyftw.heroes.projectiles.events.BlockProjectileHitEvent;
import me.bugsyftw.heroes.projectiles.events.ItemProjectileHitEvent;
import me.bugsyftw.heroes.utils.ParticleEffect;
import me.bugsyftw.heroes.utils.Vector3D;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import pgDev.bukkit.DisguiseCraft.disguise.Disguise;
import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;

public class AbilityListener implements Listener {

	private Map<String, ItemStack> canCast = new HashMap<String, ItemStack>();
	private List<Block> fireBlocks = new ArrayList<Block>();
	private List<String> canFireFloor = new ArrayList<String>();
	private List<String> superFlash = new ArrayList<String>();
	public static Map<UUID, String> strikes = new HashMap<UUID, String>();
	private final PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 255);

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if (Heroes.game != null && Heroes.game.getState() == GameState.PLAYING) {
			final Player p = e.getPlayer();
			final HeroPlayer hero = HeroPlayer.fromPlayer(p);
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (p.getItemInHand().equals(AbilityItems.Rocks())) {
					if (canCast("Rock_" + p.getName(), AbilityItems.Rocks())) {
						int block = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getTypeId();
						int data = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getData();
						if (block == 0) {
							CustomProjectile cp = new BlockProjectile("Rock_" + p.getName(), p, 1, 0, 1.1F);
							newTimer(AbilityItems.Rocks(), cp.getProjectileName(), 2, e.getPlayer());
						}
						CustomProjectile cp = new BlockProjectile("Rock_" + p.getName(), p, block, data, 1.1F);
						newTimer(AbilityItems.Rocks(), cp.getProjectileName(), 4, e.getPlayer());
					}
				} else if (p.getItemInHand().equals(AbilityItems.Pistol())) {
					if (canCast("Pistol_" + p.getName(), AbilityItems.Pistol())) {
						CustomProjectile cp = new ItemProjectile("Pistol_" + p.getName(), p, new ItemStack(Material.GHAST_TEAR), 1.3F);
						p.playEffect(p.getLocation(), Effect.SMOKE, 20);
						newTimer(AbilityItems.Pistol(), cp.getProjectileName(), 2, p);
						for(Player all : Bukkit.getOnlinePlayers()){
							int x = all.getLocation().getBlockX();
							int y = all.getLocation().getBlockY();
							int z = all.getLocation().getBlockZ();
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound hb.gun " + all.getName() + " " + x + " " + y + " " + z + " 5 0 2");

						}					}
				} else if (p.getItemInHand().equals(AbilityItems.PointedJump())) {
					if (canCast("PointedJump_" + p.getName(), AbilityItems.PointedJump())) {
						Vector v = p.getLocation().getDirection();
						v.multiply(3);
						v.setY(1.3);
						p.setVelocity(v);
						newTimer(AbilityItems.PointedJump(), "PointedJump_" + p.getName(), 10, p);
					}
				} else if (p.getItemInHand().equals(AbilityItems.Mjolnir())) {
					if (canCast("Mjolnir_" + p.getName(), AbilityItems.Mjolnir()) == false) {
						// LightningStrike s =
						p.getWorld().strikeLightningEffect(p.getEyeLocation());
						// strikes.put(s.getUniqueId(), p.getName());
						newTimer(AbilityItems.Mjolnir(), "Mjolnir_" + p.getName(), 15, p);
					}
				} else if (p.getItemInHand().equals(AbilityItems.FishShot())) {
					if (canCast("FishShot_" + p.getName(), AbilityItems.FishShot())) {
						Random rand = new Random();
						CustomProjectile cp = new ItemProjectile("FishShot_" + p.getName(), p, new ItemStack(Material.RAW_FISH, 1, (short) rand.nextInt(4)), 1f);
						newTimer(AbilityItems.FishShot(), cp.getProjectileName(), 1, p);
						Random r = new Random();
						int h = r.nextInt(100);
						if (h <= 25){
							for(Player all : Bukkit.getOnlinePlayers()){
								int x = all.getLocation().getBlockX();
								int y = all.getLocation().getBlockY();
								int z = all.getLocation().getBlockZ();
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound hb.aquaman " + all.getName() + " " + x + " " + y + " " + z + " 5 0 2");

							}						}
					}
				} else if (p.getItemInHand().equals(AbilityItems.Invisibility())) {
					if (canCast("Invisibility_" + p.getName(), AbilityItems.Invisibility())) {
						hero.undisguisePlayer();
						hero.getPlayer().sendMessage(Game.PREFIX + "§7§oYou are now invisible!");
						hero.getPlayer().addPotionEffect(effect);
						newTimer(AbilityItems.Invisibility(), "Invisibility_" + p.getName(), 20, p);
						Bukkit.getScheduler().scheduleSyncDelayedTask(Heroes.getInstance(), new BukkitRunnable() {
							@Override
							public void run() {
								hero.getPlayer().removePotionEffect(effect.getType());
								hero.disguisePlayer();
								hero.getPlayer().sendMessage(Game.PREFIX + "§7§oYou are now visible!");
							}
						}, (20 * 15));
					}
				} else if (p.getInventory().equals(AbilityItems.ShockWave())) {
					if (canCast("ShockWave_" + p.getName(), AbilityItems.ShockWave())) {
						Bukkit.getScheduler().scheduleSyncRepeatingTask(Heroes.getInstance(), new BukkitRunnable() {
							int ticks = 0;

							@Override
							public void run() {
								if (ticks != 20) {
									Location loc = p.getEyeLocation();
									int amount = ticks == 0 ? 1 : (int) (ticks * 2.4);
									double i = ticks * .02;
									float spread = (float) i;
									ParticleEffect.ENCHANTMENT_TABLE.display(loc, 20, spread, spread, spread, 0, amount);

									int range = spread < 2 ? 2 : Math.round(spread);
									for (Entity e : Vector3D.getNearbyEntities(loc, range)) {
										if (e instanceof Player) {
											Player pe = (Player) e;
											if (p.getName() != pe.getName()) {
												HeroPlayer hp = HeroPlayer.fromPlayer(pe);
												pe.damage(6D);
												hp.setLastDamager(p);
											}
										}
									}
									ticks++;
								}
							}
						}, 0L, 1L);
						newTimer(AbilityItems.ShockWave(), "ShockWave_" + p.getName(), 10, p);
					}
				} else if (p.getItemInHand().equals(AbilityItems.FireFloor())) {
					if (canCast("FireFloor_" + p.getName(), AbilityItems.FireFloor())) {
						canFireFloor.add(p.getName());
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 4));
						newTimer(AbilityItems.FireFloor(), "FireFloor_" + p.getName(), 15, p);
						Bukkit.getScheduler().scheduleSyncDelayedTask(Heroes.getInstance(), new BukkitRunnable() {
							@Override
							public void run() {
								canFireFloor.remove(p.getName());
								p.removePotionEffect(PotionEffectType.SPEED);
								for (int i = 0; i < fireBlocks.size(); i++) {
									fireBlocks.get(i).setType(Material.AIR);
								}
							}
						}, (20 * 3));
					}
				} else if (p.getItemInHand().equals(AbilityItems.SuperSpeed())) {
					if (canCast("SuperSpeed_" + p.getName(), AbilityItems.SuperSpeed())) {
						superFlash.add(p.getName());
						p.removePotionEffect(PotionEffectType.SPEED);
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (20 * 5), 20));
						newTimer(AbilityItems.SuperSpeed(), "SuperSpeed_" + p.getName(), 10, p);
						for(Player all : Bukkit.getOnlinePlayers()){
							int x = all.getLocation().getBlockX();
							int y = all.getLocation().getBlockY();
							int z = all.getLocation().getBlockZ();
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound hb.flash " + all.getName() + " " + x + " " + y + " " + z + " 5 0 2");

						}
						Bukkit.getScheduler().scheduleSyncDelayedTask(Heroes.getInstance(), new BukkitRunnable() {
							@Override
							public void run() {
								superFlash.remove(p.getName());
								p.removePotionEffect(PotionEffectType.SPEED);
								p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 5), true);							}
						}, (20 * 3));
					}
				} else if (p.getItemInHand().equals(AbilityItems.ANT_GIANT())){
					if (canCast("Giant_" + p.getName(), AbilityItems.ANT_GIANT())){
						if(canCast("Baby_" + p.getName(), AbilityItems.ANT_TINY())){
							p.sendMessage(Game.PREFIX + ChatColor.RED + "You've just became GIANT");
							final HeroPlayer hp = HeroPlayer.fromPlayer(p);
							hp.undisguisePlayer();
							Heroes.dcAPI.disguisePlayer(p, new Disguise(Heroes.dcAPI.newEntityID(), DisguiseType.Giant));
							p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 4), true);
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 2), true);
							newTimer(AbilityItems.ANT_GIANT(), "Giant_" + p.getName(), 10, p);
							Bukkit.getScheduler().scheduleSyncDelayedTask(Heroes.getInstance(), new Runnable(){
								public void run(){
									hp.undisguisePlayer();
									hp.disguisePlayer();
									p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
									p.removePotionEffect(PotionEffectType.SLOW);
									p.sendMessage(Game.PREFIX + "You've just became Normal Size");
								}
							}, (20 * 15));
						}
					}
				} else if (p.getItemInHand().equals(AbilityItems.ANT_TINY())){
					if (canCast("Baby_" + p.getName(), AbilityItems.ANT_TINY())){
						if (canCast("Giant_" + p.getName(), AbilityItems.ANT_GIANT())){
							final HeroPlayer hp = HeroPlayer.fromPlayer(p);
							p.sendMessage(Game.PREFIX + ChatColor.AQUA + "You've just became TINY");
							hp.undisguisePlayer();
							Heroes.dcAPI.disguisePlayer(p, new Disguise(Heroes.dcAPI.newEntityID(), DisguiseType.Zombie).setSingleData("baby"));
							p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4), true);
							p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 2), true);
							newTimer(AbilityItems.ANT_TINY(), "Baby_" + p.getName(), 10, p);
							Bukkit.getScheduler().scheduleSyncDelayedTask(Heroes.getInstance(), new Runnable(){
								public void run(){
									hp.undisguisePlayer();
									hp.disguisePlayer();
									p.removePotionEffect(PotionEffectType.SPEED);
									p.removePotionEffect(PotionEffectType.WEAKNESS);
									p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1), true);
									p.sendMessage(Game.PREFIX + "You've just became Normal Size");
								}
							}, (20 * 15));
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockHit(BlockProjectileHitEvent e) {
		if (e.getHitEntity() instanceof Player) {
			Player p = (Player) e.getHitEntity();
			if (Heroes.game.players.contains(p.getName()) && Heroes.game.getState() == GameState.PLAYING) {
				if (e.getProjectile().getProjectileName().equals("Rock_" + ((Player) e.getProjectile().getShooter()).getName())) {
					p.damage(4D);
					HeroPlayer hp = HeroPlayer.fromPlayer(p);
					hp.setLastDamager((Player) e.getProjectile().getShooter());
					p.getLocation().getWorld().playEffect(p.getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
				}
			}
		}
	}

	@EventHandler
	public void onItemHit(ItemProjectileHitEvent e) {
		if (e.getHitEntity() instanceof Player) {
			Player p = (Player) e.getHitEntity();
			if (e.getProjectile().getProjectileName().equals("Pistol_" + ((Player) e.getProjectile().getShooter()).getName())) {
				p.damage(4);
				HeroPlayer hp = HeroPlayer.fromPlayer(p);
				hp.setLastDamager((Player) e.getProjectile().getShooter());
				p.getLocation().getWorld().playEffect(p.getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
				p.playSound(p.getLocation(), Sound.STEP_SNOW, 10, 5);
			}
		}
	}

	@EventHandler
	public void onFire(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Game game = Heroes.game;
			if (game != null && game.getState() == GameState.PLAYING) {
				Player p = (Player) e.getEntity();
				HeroPlayer hp = HeroPlayer.fromPlayer(p);
				if (e.getCause() == DamageCause.FIRE || e.getCause() == DamageCause.FIRE_TICK) {
					if (canCast("FireFloor_" + p.getName(), AbilityItems.FireFloor())) {
						if (hp.getHero() == Hero.FLASH){
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onImpact(ItemProjectileHitEvent e) {
		int type = e.getItemStack().getData().getData();
		Location loc = e.getProjectile().getEntity().getLocation();
		List<Entity> nearby = e.getProjectile().getEntity().getNearbyEntities(1.2, 1.5, 1.2);
		HeroPlayer hit = null;

		if (e.getHitEntity() != null && e.getHitEntity() instanceof Player) {
			hit = HeroPlayer.fromPlayer((Player) e.getHitEntity());
			Player damager = ((Player) e.getProjectile().getShooter());

			switch (type) {
			case 0: // normal raws fish
				if (hit != null) {
					hit.setLastDamager(damager);
					hit.getPlayer().damage(2d);
					hit.getPlayer().getLocation().getWorld().playEffect(hit.getPlayer().getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
				}
				break;
			case 1: // raw salmon
				if (hit != null) {
					hit.setLastDamager(damager);
					hit.getPlayer().damage(2d);
					Vector v = hit.getPlayer().getLocation().toVector().subtract(loc.toVector()).normalize();
					hit.getPlayer().setVelocity(v.multiply(2));
					hit.getPlayer().getLocation().getWorld().playEffect(hit.getPlayer().getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
				}
				break;
			case 2: // raw clownfish
				if (hit != null) {
					hit.setLastDamager(damager);
					hit.getPlayer().damage(1d);
					hit.getPlayer().setFireTicks(55);
					hit.getPlayer().getLocation().getWorld().playEffect(hit.getPlayer().getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
				}
				break;
			case 3: // raw pufferfish
				loc.getWorld().playSound(loc, Sound.EXPLODE, 3f, 1f);
				ParticleEffect.EXPLODE.display(loc, 20, .1f, .1f, .1f, 0, 2);
				hit.getPlayer().getLocation().getWorld().playEffect(hit.getPlayer().getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
				for (Entity entity : nearby) {

					if (entity instanceof Player) {
						Player p = (Player) entity;

						hit.setLastDamager(damager);
						p.damage(2d);

						PotionEffect pe = new PotionEffect(PotionEffectType.CONFUSION, 100, 1);
						if (!p.hasPotionEffect(pe.getType())) {
							p.addPotionEffect(pe);
						}

					}

				}
			default:
			}

		}
	}

	@EventHandler
	public void fireFloor(PlayerMoveEvent event) {
		Game game = Heroes.game;
		if (game != null && game.getState() == GameState.PLAYING) {
			Player p = event.getPlayer();
			if (canFireFloor.contains(p.getName())) {
				Location pos = p.getLocation();
				Block b = pos.getBlock();
				if (b.getType() != Material.FIRE) {
					b.setType(Material.FIRE);
					fireBlocks.add(b);
				}
				for (Entity e : p.getNearbyEntities(1.5, 1, 1.5)) {
					if (e instanceof Player) {
						Player nearby = (Player) e;
						PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 60, 1);
						if (nearby.getName() != p.getName() && !nearby.getActivePotionEffects().contains(slow)) {
							HeroPlayer hp = HeroPlayer.fromPlayer(nearby);
							hp.setLastDamager(p);
							nearby.addPotionEffect(slow);
							nearby.sendMessage(Game.PREFIX + p.getName() + " §rhas stunned you with his immense speed!");
						}
					}
				}
			} else if (superFlash.contains(p.getName())) {
				p.getLocation().getWorld().playEffect(p.getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
			}
		}
	}

	public void newTimer(final ItemStack item, final String name, final int delay, final Player p) {
		canCast.put(name, item);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Heroes.getInstance(), new BukkitRunnable() {
			int d = delay;

			@Override
			public void run() {
				if (d != -1) {
					if (d != 0) {
						if (p.getItemInHand().equals(item)) {
							p.setLevel(d);
						}
						d--;
					} else {
						if (p.getItemInHand().equals(item)) {
							p.setLevel(d);
						}
						d--;
						canCast.remove(name);
					}
				}
				;
			}
		}, 0L, 20L);
	}

	public boolean canCast(String name, ItemStack item) {
		if (canCast.containsKey(name) && canCast.containsValue(item)) return false;
		return true;
	}
}
