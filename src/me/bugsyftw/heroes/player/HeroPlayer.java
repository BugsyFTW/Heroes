package me.bugsyftw.heroes.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.bugsyftw.heroes.Heroes;
import me.bugsyftw.heroes.database.DataHandler;
import me.bugsyftw.heroes.game.Game;
import me.bugsyftw.heroes.player.data.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import pgDev.bukkit.DisguiseCraft.disguise.Disguise;
import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;

public class HeroPlayer {

	public static List<HeroPlayer> heroes = new ArrayList<HeroPlayer>();

	private String name;
	private Hero hero;
	private Player lastDamager;
	public int kills = 0;
	private PlayerProfile profile;
	public Boolean halfCooldown = false;
	public Boolean superSoliderSerum = false;
	public Game game = Heroes.game;

	public HeroPlayer(String name) {
		this.name = name;
		heroes.add(this);
	}

	public void setPlayerProfile(PlayerProfile profile) {
		this.setProfile(profile);
	}

	public void disguisePlayer() {
		if (getPlayer() != null && getHero() != null) {
			Player p = getPlayer();
			try{
				undisguisePlayer();
				Heroes.dcAPI.disguisePlayer(p, new Disguise(Heroes.dcAPI.newEntityID(), getHero().username(), DisguiseType.Player));
			}catch(Exception e){
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "disguise " + p.getName() + " p " + getHero().username());
			}
		}
	}

	public void undisguisePlayer() {
		if (getPlayer() != null) {
			Player p = getPlayer();
			if (Heroes.dcAPI.isDisguised(p)) {
				Heroes.dcAPI.undisguisePlayer(p);
			}
		}
	}

	public void clearPotionEffects() {
		for (PotionEffect pe : getPlayer().getActivePotionEffects()) {
			getPlayer().removePotionEffect(pe.getType());
		}
	}

	public void heal() {
		Damageable d = (Damageable) getPlayer();
		d.setHealth(d.getMaxHealth());
		getPlayer().setFoodLevel(20);
		getPlayer().setSaturation(100f);
	}

	@SuppressWarnings("deprecation")
	public void unreadyHero() {
		undisguisePlayer();
		heal();
		getPlayer().getInventory().clear();
		getPlayer().updateInventory();
	}
	
	public void giveInventory() {
		getHero().giveItems(getPlayer());
	}

	public Player getPlayerByUUID(UUID uuid) {
		return DataHandler.getPlayerByUUID(uuid);
	}

	@SuppressWarnings("deprecation")
	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(getName());
	}

	public String getName() {
		return name;
	}

	public static HeroPlayer fromPlayer(Player p) {
		for (HeroPlayer hp : heroes) {
			if (hp.name.equals(p.getName())) {
				return hp;
			}
		}
		return null;
	}
	
	public void setLastDamager(Player lastDamager) {
		this.lastDamager = lastDamager;
	}
	
	public Player getLastDamager() {
		return lastDamager;
	}
	
	public int getKills() {
		return kills;
	}
	
	public void addKill(int a) {
		this.kills = this.kills + a;
		profile.getWinKill().addKill(a);
	}
	
	public void clearKills() {
		this.kills = 0;
	}
	
	public void setKills(int a) {
		this.kills = a;
	}

	public List<HeroPlayer> getHeroes() {
		return heroes;
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero h) {
		if (h == null) {
			hero = null;
			return;
		}
		hero = h;
	}

	public PlayerProfile getProfile() {
		return profile;
	}

	public void setProfile(PlayerProfile profile) {
		this.profile = profile;
	}
}
