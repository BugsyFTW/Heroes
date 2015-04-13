package me.bugsyftw.heroes.player.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.bugsyftw.heroes.database.DataHandler;
import me.bugsyftw.heroes.player.Hero;
import me.bugsyftw.heroes.player.HeroPlayer;

public class PlayerProfile {

	private HeroCoin coins;
	private KillDeathRatio kd;
	private WinLossRatio wl;
	private Map<Hero, Boolean> heroes = new HashMap<Hero, Boolean>();
	private Map<Hero, Integer> hero_upgrades = new HashMap<Hero, Integer>();
	private List<AchievementList> achieved;
	private PlayerRank rank;
	private WinKill winkill;

	public PlayerProfile(HeroCoin coins, KillDeathRatio kd, WinLossRatio wl, Map<Hero, Boolean> heroes, PlayerRank rank, List<AchievementList> achieved, WinKill winkill, Map<Hero, Integer> hu) {
		this.coins = coins;
		this.kd = kd;
		this.wl = wl;
		this.heroes = heroes;
		this.rank = rank;
		this.achieved = achieved;
		this.winkill = winkill;
		this.hero_upgrades = hu;
	}

	public PlayerRank getRank() {
		return rank;
	}

	public KillDeathRatio getKD() {
		return kd;
	}

	public WinLossRatio getWL() {
		return wl;
	}

	public HeroCoin getCoins() {
		return coins;
	}

	public List<AchievementList> getAchievements() {
		return achieved;
	}

	public Map<Hero, Boolean> getHeroesMap() {
		return heroes;
	}
	
	public Map<Hero, Integer> getUpgradesMap() {
		return hero_upgrades;
	}
	
	public WinKill getWinKill() {
		return winkill;
	}

	public static PlayerProfile getProfileFromUUID(UUID uuid) {
		return HeroPlayer.fromPlayer(DataHandler.getPlayerByUUID(uuid)).getProfile();
	}

}
