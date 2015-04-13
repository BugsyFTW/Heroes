package me.bugsyftw.heroes;

import me.bugsyftw.heroes.game.GameType;
import me.bugsyftw.heroes.player.HeroPlayer;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public enum HeroAid{
	HEALTH("Health 1",new Potion(PotionType.INSTANT_HEAL,1).toItemStack(1)),
	HEALTH2("Health 2",new Potion(PotionType.INSTANT_HEAL,2).toItemStack(1)),
	SUPER_SOLDIER("Super Soldier Serum",new Potion(PotionType.WATER).toItemStack(1)),
	HALF_COOLDWON("Half Cooldown",new Potion(PotionType.SPEED,1).toItemStack(1)),
	TESSERACT("Tesseract",new ItemStack(Material.DIAMOND_BLOCK));

	private String name;
	private ItemStack is;
	HeroAid(String name, ItemStack is){
		this.name = name;
		this.is = is;
	}
	
	public String getName(){
		return this.toString();
	}
	
	public String getDataName(){
		String s = name;
		name.replace(" ", "").toLowerCase();
		return s;
	}
	
	public ItemStack getItem() {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("§r" + name);
		is.setItemMeta(im);
		return is;
	}

	
	public static void activate(HeroAid a, HeroPlayer hp){
		Inventory inv = hp.getPlayer().getInventory();
		inv.remove(a.getItem());
		switch(a){
		case HALF_COOLDWON:
			hp.halfCooldown = true;
			break;
		case HEALTH:
			addHealth(hp.getPlayer(), 4);
			break;
		case HEALTH2:
			addHealth(hp.getPlayer(), 8);
			break;
		case SUPER_SOLDIER:
			hp.superSoliderSerum = true;
			break;
		case TESSERACT:
			if(Heroes.game != null && Heroes.game.getType()  == GameType.FREE_FOR_ALL){
				hp.getPlayer().sendMessage("§cTesseract can only be used in 1vs1");
			}else{
				//activate
			}
			break;
		default:
			break;
		}
	}
	
	private static void addHealth(Player p, double amount){
		Damageable dm = p;
		if(dm.getHealth() + amount > dm.getMaxHealth()){
			p.setHealth(dm.getMaxHealth());
		}else{
			p.setHealth(dm.getHealth() + amount);
		}
	}

}
