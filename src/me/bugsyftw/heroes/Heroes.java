package me.bugsyftw.heroes;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

import me.bugsyftw.heroes.abilities.AbilityListener;
import me.bugsyftw.heroes.database.DataHandler;
import me.bugsyftw.heroes.game.Game;
import me.bugsyftw.heroes.game.GameType;
import me.bugsyftw.heroes.game.SpawnCollection;
import me.bugsyftw.heroes.listeners.ConnectionListener;
import me.bugsyftw.heroes.listeners.GameListeners;
import me.bugsyftw.heroes.player.data.SkyRank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;

public class Heroes extends JavaPlugin {

	private static Logger log = Logger.getLogger("Minecraft");
	private static Heroes instance;
	public static Game game;
	public static DisguiseCraftAPI dcAPI;
	public DataHandler datahandler = new DataHandler(this);

	public static void logMessage(String msg) {
		log.info(msg);
	}

	public void onEnable() {
		instance = this;
		getConfig().addDefault("Valid", false);
		getConfig().options().copyDefaults(true);
		saveConfig();
		registerEvents();
		setupDisguiseCraft();
		datahandler.init();
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		if (getConfig().getBoolean("Valid") == true) {
			Game.createGame(GameType.fromConfigString(getConfig().getString("Type")));
			game.setType(GameType.fromConfigString(getConfig().getString("Type")));
			game.spawns = new SpawnCollection(game, SpawnCollection.getLocations());
			game.doSpecial();
		} else {
			logMessage(ChatColor.RED.toString() + "&lThis server hasn't been assigned a GameType or Spawns!");
		}
	}

	public void onDisable() {
		datahandler.db.closeConnection();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("creategame")) {
			if (!(sender instanceof Player)) return false;
			if (!sender.isOp()) return false;
			Player p = (Player) sender;
			if (args.length == 0) {
				p.sendMessage(ChatColor.RED + "/creategame <ffa,1v1,tournament>");
				return false;
			}
			if (game == null) {
				Game.createGame(GameType.fromString(args[0]));
				game.setType(GameType.fromString(args[0]));
				game.spawns = new SpawnCollection(game);
				p.sendMessage(ChatColor.GOLD + "You need to add " + game.getType().getMinSpawns() + " spawns! /addspawn <name>");
				game.setValidConfig(true);
				game.doSpecial();
				getConfig().set("Name", p.getWorld().getName());
				saveConfig();
			} else {
				return false;
			}
		} else if (label.equalsIgnoreCase("addspawn")) {
			if (!(sender instanceof Player)) return false;
			if (!sender.isOp()) return false;
			Player p = (Player) sender;
			if (args.length == 0) {
				p.sendMessage(ChatColor.RED + "/addspawn <name>");
				return false;
			}
			Location loc = p.getLocation();
			if (game.getSpawnCollection().addSpawn(args[0], loc)) {
				if (args[0].equalsIgnoreCase("lobby")) {
					p.sendMessage(ChatColor.YELLOW + "Added Lobby Sucefully!");
					saveConfig();
				} else {
					p.sendMessage(ChatColor.YELLOW + "Added '" + args[0] + "' to spawns(" + game.getSpawnCollection().getSpawnSize() + "/" + game.getType().getMaxSpawns() + ")");
					saveConfig();
				}
			}
			return false;
		} else if (label.equalsIgnoreCase("rank")) {
			if (!(sender instanceof Player)) return false;
			if (!sender.isOp()) return false;
			if (args.length >= 0 && args.length <= 2) {
				sender.sendMessage(ChatColor.RED + "/rank set <name> <ID>");
				return false;
			}
			if (args.length == 3) {
				@SuppressWarnings("deprecation")
				UUID uuid = Bukkit.getPlayer(args[1]).getUniqueId();
				SkyRank rank = SkyRank.fromID(Integer.parseInt(args[2]));
				if (isInteger(args[2])) {
					if (Integer.parseInt(args[2]) > 0 && Integer.parseInt(args[2]) < 7) {
						if (datahandler.setRank(uuid, rank)) {
							sender.sendMessage(ChatColor.GREEN + args[1] + " rank is now " + rank.getName());
						} else {
							sender.sendMessage(ChatColor.RED + "Something went wrong when you tried to set the rank! Either the player you typed has never been on the server or the id you have typed is wrong!");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Invalid Rank!");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "<ID> paramter must be a number!");
				}
			}
		} else if (label.equalsIgnoreCase("hub")) {
			if (!(sender instanceof Player)) return false;
			Player p = (Player) sender;
			sendToHub(p);
		}
		return false;
	}

	public static void sendToHub(Player p) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF("Lobby");
		} catch (IOException e) {
			e.printStackTrace();
		}
		p.sendPluginMessage(Heroes.getInstance(), "BungeeCord", b.toByteArray());
	}

	public void registerEvents() {
		getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
		getServer().getPluginManager().registerEvents(new GameListeners(), this);
		getServer().getPluginManager().registerEvents(new SelectMenu(), this);
		getServer().getPluginManager().registerEvents(new AbilityListener(), this);
	}

	public void setupDisguiseCraft() {
		dcAPI = DisguiseCraft.getAPI();
	}

	public static Heroes getInstance() {
		return instance;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
