package me.bugsyftw.heroes.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.bugsyftw.heroes.abilities.AbilityItems;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum Hero {
	
	THING("Thing", "It's clobberin time!", "HThingB", true, 0, 0),
	HAWKEYE("Hawkeye", "It's okay everybody, I'm an Avenger", "HHawkeyeB", true, 0, 0),
	BLAKCWIDOW("Black Widow", "Здрав�?твуйте, �? �?аташа Романова", "HBlackWidowB", true, 0, 0),
	AQUAMAN("Aquaman", "EeeeeeiK eEik *Bloop* eEeeK ~Splash~", "HAquaManB", false, 250, 0),
	INVISIBLE_WOMAM("Invisible Woman", "Johnny, Flame Off! You'll get ashes all over my furniture", "HInvisibleWomanB", false, 500, 0),
	FLASH("Flash", "I'll have us wrapped up in a flash", "HFlashB", false, 750, 0),
	ANTMAN("Antman", "Go to the ants thou sluggard!", "HAntmanB", false, 1000, 0),
	CAPTAIN_AMERICA("Captain America", "I've been asleep for 70 years.\n I think I've had enough rest", "HCaptainAmericaB", false, 1500, 0),
	HUMAN_TORCH("Human Torch", "Flame on!", "HHumanTorchB",false, 2000, 0),
	WOLVERINE("Wolverine", "I can do this all day, you twisted mutant bitch!", "HWolverineB", false, 2500, 0),
	THOR("Thor", "Whosoever holds this hammer, if he be worthy, shall possess the power of THOR!", "HThorB", false, 5000, 0),
	IRON_MAN("Iron Man", "Genius, billionaire, playboy, philanthropist", "HIronManB", false, 7500, 0),
	DEADPOOL("Deadpool", "My common sense is tingling", "HDeapoolB", false, 0, 0),
	SPIDER_MAN("Spider-Man", "With great power comes great responsibility", "HSpiderManB", false, 0, 0),
	SUPER_MAN("Superman", "It's a bird... It's a plane... It's Superman!", "HSupermanB", false, 0, 0),
	HULK("Hulk", "HULK SMASH!", "HHulkB", false, 0, 0),
	BATMAN("Batman", "I'm Batman", "HBatmanB", false, 0, 0);

	private String name;
	private String saying;
	private boolean b;
	private int price;
	private int upgrade;
	
	private Hero(String name, String saying, String username, boolean b, int price, int upgrade) {
		this.name = name;
		this.saying = saying;
		this.b = b;
		this.price = price;
		this.setUpgrade(upgrade);
	}

	public static List<Hero> getHeroList() {
		return Arrays.asList(values());
		
	}
	
	public static Hero fromString(String s) {
		for (Hero h : Hero.values()) {
			if(s.equalsIgnoreCase(s.toString())){
				return h;
			}
		}
		return null;
	}
	
	public ItemStack getItem() {
		ItemStack item = null;
		switch (this) {
		case THING:
			 return item = new ItemStack(Material.CLAY_BRICK);
		case HAWKEYE:
			return item = new ItemStack(Material.BOW);
		case BLAKCWIDOW:
			return item = new ItemStack(Material.SPIDER_EYE);
		case AQUAMAN:
			return item = new ItemStack(Material.WATER_BUCKET);
		case INVISIBLE_WOMAM:
			return item = new ItemStack(Material.POTION, 1 ,(short)14);
		case FLASH:
			return item = new ItemStack(Material.FEATHER);
		case ANTMAN:
			return item = new ItemStack(Material.MONSTER_EGG, 1, (short) 60);
		case CAPTAIN_AMERICA:
			return item = new ItemStack(Material.NETHER_STAR);
		case HUMAN_TORCH:
			return item = new ItemStack(Material.FIRE);
		case WOLVERINE:
			return item = new ItemStack(Material.BONE);
		case THOR:
			return item = new ItemStack(Material.IRON_AXE);
		case IRON_MAN:
			return item = new ItemStack(Material.IRON_INGOT);
		case DEADPOOL:
			return item = new ItemStack(Material.SULPHUR);
		case SPIDER_MAN:
			return item = new ItemStack(Material.WEB);
		case SUPER_MAN:
			return item = new ItemStack(Material.EMERALD);
		case BATMAN:
			return item = new ItemStack(Material.MONSTER_EGG, 1, (short)65);
		case HULK:
			 item = new ItemStack(Material.LEATHER_LEGGINGS);
			 return item;
		default:
			break;
		}
		return item;
	}
	
	public String getDataName(){
		String s = this.name;
		s = s.replace(" ", "");
		s = s.replace("-", "");
		return s.toLowerCase();
	}
	
	public List<String> getLoreSaying(){
		return new ArrayList<String>(Arrays.asList("§7" + saying));
	}
	
	public int getPrice() {
		return price;
	}
	
	public boolean isUnlocked() {
		return b;
	}
	
	public void setUnlocked(boolean b) {
		this.b = b;
	}

	public String getName() {
		return name;
	}

	public String getSaying() {
		return saying;
	}

	public String username() {
		switch (this) {
		case THING:
			return "HThingB";
		case HAWKEYE:
			return "HHawkeyeB";
		case BLAKCWIDOW:
			return "HBlackWidowB";
		case AQUAMAN:
			return "HAquaManB";
		case INVISIBLE_WOMAM:
			return "HInvisibleWomanB";
		case FLASH:
			return "HFlashB";
		case ANTMAN:
			return "HAntManB";
		case CAPTAIN_AMERICA:
			return "HCaptainAmericaB";
		case HUMAN_TORCH:
			return "HHumanTorchB";
		case WOLVERINE:
			return "HWolverineB";
		case THOR:
			return "HThorB";
		case IRON_MAN:
			return "HIronManB";
		case DEADPOOL:
		case SPIDER_MAN:
			return "HSpidermanB";
		case SUPER_MAN:
			return "HSupermanB";
		case BATMAN:
			return "HBatmanB";
		case HULK:
			return "HHulkB";
		default:
			break;
		}
		return "ERROR";
	}
	
	public void giveItems(Player p) {
		for (PotionEffect pt : p.getActivePotionEffects()){
			p.removePotionEffect(pt.getType());
		}
		switch (this) {
		case THING:
			p.getInventory().addItem(AbilityItems.Rocks());
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1), true);
			break;
		case HAWKEYE:
			p.getInventory().addItem(AbilityItems.H_BOW());
			p.getInventory().addItem(new ItemStack(Material.ARROW));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1), true);
			break;
		case BLAKCWIDOW:
			p.getInventory().addItem(AbilityItems.Pistol());
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1), true);
			break;
		case AQUAMAN:
			p.getInventory().addItem(AbilityItems.FishShot());
			p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 2), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1), true);
			break;
		case INVISIBLE_WOMAM:
			p.getInventory().addItem(AbilityItems.Invisibility());
			p.getInventory().addItem(AbilityItems.ShockWave());
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1), true);
			break;
		case FLASH:
			p.getInventory().addItem(AbilityItems.FireFloor());
			p.getInventory().addItem(AbilityItems.SuperSpeed());
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 5), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1), true);
			break;
		case ANTMAN:
			p.getInventory().addItem(AbilityItems.ANT_GIANT());
			p.getInventory().addItem(AbilityItems.ANT_TINY());
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1), true);
			break;
		case CAPTAIN_AMERICA:
			//TODO
			break;
		case HUMAN_TORCH:
			//TODO
			break;
		case WOLVERINE:
			//TODO
			break;
		case THOR:
			p.getInventory().addItem(AbilityItems.Mjolnir());
			p.getInventory().addItem(AbilityItems.PointedJump());
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3), true);
			//TODO
			break;
		default:
			break;
		}
	}

	public int getUpgrade() {
		return upgrade;
	}

	public void setUpgrade(int upgrade) {
		this.upgrade = upgrade;
	}

}
