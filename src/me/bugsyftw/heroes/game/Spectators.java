package me.bugsyftw.heroes.game;

import java.util.ArrayList;
import java.util.List;

import me.bugsyftw.heroes.GhostFactory;
import me.bugsyftw.heroes.Heroes;
import me.bugsyftw.heroes.player.HeroPlayer;

public class Spectators {
	
	private static GhostFactory ghost;
	public static List<HeroPlayer> spectators = new ArrayList<HeroPlayer>();
	
	public static boolean isSpectating(HeroPlayer name){
		for(HeroPlayer hp: spectators){
			if(hp.equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public static void clearSpectators(){
		for(HeroPlayer hp : spectators){
			hp.getPlayer().setAllowFlight(false);
			ghost.removeGhost(hp.getPlayer());
		}
		spectators.clear();
	}
	
	public static void setSpectating(HeroPlayer hp){
		if(ghost == null){ createFactory(); }
		if(!spectators.contains(hp) && Heroes.game != null && Heroes.game.players.contains(hp.getPlayer().getName())) {
			hp.unreadyHero();
			hp.getPlayer().setAllowFlight(true);
			spectators.add(hp);
			ghost.addGhost(hp.getPlayer());
		}
	}
	
	private static void createFactory(){
		ghost = new GhostFactory(Heroes.getInstance(), true);
		ghost.create();
	}
	
	
}
