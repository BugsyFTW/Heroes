package me.bugsyftw.heroes.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class AbilityItems {

	private static String RIGHT_CLICK = ChatColor.GRAY + " (Right-Click)";

	public static ItemStack Batarang() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Batarang"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack H_BOW() {
		ItemStack item = new ItemStack (Material.BOW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Hawkeye's Bow"));
		meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack DeadSword() {
		ItemStack item = new ItemStack(Material.STONE_SWORD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Dead Sword"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack BeastJump() {
		ItemStack item = new ItemStack(Material.FEATHER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Beast Jump"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack Fireball() {
		ItemStack item = new ItemStack(Material.FIREBALL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Fireball"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack FireFloor() {
		ItemStack item = new ItemStack(Material.FIRE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Fire Floor"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack SuperSpeed() {
		ItemStack item = new ItemStack(Material.FEATHER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Super Speed"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack FireFly() {
		ItemStack item = new ItemStack(Material.FEATHER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Fire Flight"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack FishShot() {
		ItemStack item = new ItemStack(Material.RAW_FISH);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Fish Shot"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack GrapplingHook() {
		ItemStack item = new ItemStack(Material.FISHING_ROD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("GrapplingHook"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack Invisibility() {
		ItemStack item = new ItemStack(new Potion(PotionType.INVISIBILITY, 1).toItemStack(1));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Invisibility"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack Laser() {
		ItemStack item = new ItemStack(Material.REDSTONE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Laser"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack Mjolnir() {
		ItemStack item = new ItemStack(Material.IRON_AXE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Mjölnir"));
		meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack Pistol() {
		ItemStack item = new ItemStack(Material.STONE_HOE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Pistol"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack PointedJump() {
		ItemStack item = new ItemStack(Material.FEATHER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Pointed Jump"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack Repulser() {
		ItemStack item = new ItemStack(Material.DIAMOND);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Repulser"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack ShieldBash() {
		ItemStack item = new ItemStack(Material.PISTON_MOVING_PIECE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Shield Bash"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack ShieldThrow() {
		ItemStack item = new ItemStack(Material.THIN_GLASS, 1, (short)3);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Shield Throw"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack ShockBomb() {
		ItemStack item = new ItemStack(Material.COAL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Shock Bomb"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack ShockWave() {
		ItemStack item = new ItemStack(Material.DIAMOND_HOE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Shock Wave"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack SparkFly() {
		ItemStack item = new ItemStack(Material.FEATHER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Spark Fly"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack Web() {
		ItemStack item = new ItemStack(Material.WEB);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Web"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack WebBall() {
		ItemStack item = new ItemStack(Material.SNOW_BALL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Web Ball"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack Rocks() {
		ItemStack item = new ItemStack(Material.SANDSTONE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Rocks"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack ANT_TINY(){
		ItemStack item = new ItemStack(Material.WOOD_BUTTON);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Baby"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack ANT_GIANT(){
		ItemStack item = new ItemStack(Material.WOOD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toName("Giant"));
		item.setItemMeta(meta);
		return item;
	}

	public static String toName(String msg) {
		String c = ChatColor.AQUA + msg + RIGHT_CLICK;
		return c;
	}

	/*public static List<String> toLore(List<String>... strings) {
		return new List<String>().add(strings);
	}*/
}
