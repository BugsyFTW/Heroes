package me.bugsyftw.heroes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.bugsyftw.heroes.game.Game;
import me.bugsyftw.heroes.game.GameState;
import me.bugsyftw.heroes.player.Hero;
import me.bugsyftw.heroes.player.HeroPlayer;
import me.bugsyftw.heroes.player.data.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SelectMenu implements Listener {

	private static Map<String, Integer> invItems = new HashMap<String, Integer>();

	static {
		invItems.put(Hero.THING.getDataName(), 3);
		invItems.put(Hero.HAWKEYE.getDataName(), 4);
		invItems.put(Hero.BLAKCWIDOW.getDataName(), 5);
		invItems.put(Hero.AQUAMAN.getDataName(), 9);
		invItems.put(Hero.INVISIBLE_WOMAM.getDataName(), 10);
		invItems.put(Hero.FLASH.getDataName(), 11);
		invItems.put(Hero.CAPTAIN_AMERICA.getDataName(), 12);
		invItems.put(Hero.ANTMAN.getDataName(), 13);
		invItems.put(Hero.HUMAN_TORCH.getDataName(), 14);
		invItems.put(Hero.WOLVERINE.getDataName(), 15);
		invItems.put(Hero.THOR.getDataName(), 16);
		invItems.put(Hero.IRON_MAN.getDataName(), 17);
		invItems.put(Hero.DEADPOOL.getDataName(), 20);
		invItems.put(Hero.SPIDER_MAN.getDataName(), 21);
		invItems.put(Hero.SUPER_MAN.getDataName(), 22);
		invItems.put(Hero.HULK.getDataName(), 23);
		invItems.put(Hero.BATMAN.getDataName(), 24);
	}

	public static void giveHeroSelect(Player p) {
		p.getInventory().clear();
		ItemStack is = new ItemStack(Material.BOOK);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("§r§3Comic Book §7§o(Choose a hero!)");
		is.setItemMeta(im);
		p.getInventory().setItem(0, is);
	}

	private static void openInv(Player p) {
		Inventory inv = Bukkit.createInventory(p, 27, "Hero Menu");
		PlayerProfile profile = HeroPlayer.fromPlayer(p).getProfile();
		for (Entry<Hero, Boolean> h : profile.getHeroesMap().entrySet()) {
			h.getKey().setUnlocked(h.getValue());
			if (invItems.containsKey(h.getKey().getDataName())) {
				inv.setItem(invItems.get(h.getKey().getDataName()), createHeroSelectItem(h.getKey(), p));
			}
		}
		p.openInventory(inv);
	}

	private static ItemStack createHeroSelectItem(Hero h, Player p) {
		boolean unlocked = h.isUnlocked();
		List<String> lore0 = unlocked ? h.getLoreSaying() : Arrays.asList("§7Purchase in shop");
		String lore1 = unlocked ? "§r§a§nUnlocked" : "§r§c§nLocked";
		ItemStack item = h.getItem();
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(h.getName());
		List<String> lore = new ArrayList<String>();
		lore.addAll(lore0);
		lore.add(lore1);
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}

	private static void selectHero(HeroPlayer hp, Hero hero) {

		List<String> a = Arrays.asList("thing", "hawkeye", "blackwidow");
		List<String> vip = Arrays.asList("deadpool", "spiderman", "superman", "hulk", "batman");
		if (hero != null) {
			if (hero == Hero.THOR) {
				hp.getPlayer().sendMessage(ChatColor.RED + "This Hero is disabled!");
				return;
			}
			hp.setHero(hero);
			ChatColor c = ChatColor.GOLD;
			String s = hero.getDataName();

			if (a.contains(s))
				c = ChatColor.GREEN;
			else if (vip.contains(s)) c = ChatColor.DARK_RED;

			hp.getPlayer().sendMessage(c + hero.getName() + " §rselected!");
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Game game = Heroes.game;
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getItem() != null && e.getItem().getType() == Material.BOOK) {
				if (game != null && game.getState() == GameState.PREGAME) {
					Player p = e.getPlayer();
					if (game.players.contains(p.getName())) {
						openInv(e.getPlayer());
					}
				}
			}
		}
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		Game game = Heroes.game;
		Player p = (Player) e.getWhoClicked();
		if (game.isValid() && game.players.contains(p.getName())) {
			if (e.getInventory().getTitle() == "Hero Menu") {
				ItemStack is = e.getCurrentItem();
				if (is != null && is.getType() != Material.AIR) {
					for (Hero h : Hero.getHeroList()) {
						if (is.getType() == h.getItem().getType() && is.getDurability() == h.getItem().getDurability()) {
							if (h.isUnlocked() == false) {
								p.sendMessage("§c" + h.getName() + " is locked!");
							} else {
								selectHero(HeroPlayer.fromPlayer(p), h);
							}
							p.closeInventory();
							break;
						}
					}
					e.setResult(Result.DENY);
					e.setCancelled(true);
				}
			}
		} else {
			return;
		}
	}

}
