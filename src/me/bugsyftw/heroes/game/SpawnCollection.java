package me.bugsyftw.heroes.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import me.bugsyftw.heroes.Heroes;

import org.bukkit.Location;
import org.bukkit.World;

public class SpawnCollection {

	private Map<String, Location> locations;
	private Game game;
	private Location lobby;

	public SpawnCollection(Game game, Map<String, Location> locations) {
		this.locations = locations;
		this.game = game;
	}

	public SpawnCollection(Game game) {
		this.game = game;
		this.locations = new HashMap<String, Location>();
	}

	public boolean addSpawn(String name, Location loc) {
		if (name.equalsIgnoreCase("lobby")) {
			this.lobby = loc;
			toConfig(name, loc);
			return true;
		}

		if (!locations.containsKey(name)) {
			locations.put(name, loc);
			toConfig(name, loc);
			return true;
		}

		return false;
	}

	public void addSpawns(Map<String, Location> locs) {
		for (Entry<String, Location> l : locs.entrySet()) {
			addSpawn(l.getKey(), l.getValue());
		}
	}

	public boolean removeSpawn(String name) {
		if (name.equalsIgnoreCase("lobby")) {
			this.lobby = null;
			return true;
		}

		if (locations.containsKey(name)) {
			locations.remove(name);
			return true;
		}

		return false;
	}

	public Location getSpawn(String name) {
		if (locations.containsKey(name)) {
			return locations.get(name);
		}

		return null;
	}

	public Location getRandomSpawn() {
		Random r = new Random();
		if (getSpawnSize() > 0) return getSpawnList().get(r.nextInt(getSpawnSize()));

		return null;
	}

	private void toConfig(String name, Location loc) {
		String code = loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", " + loc.getPitch() + ", " + loc.getYaw();
		Heroes.getInstance().getConfig().set("Spawn." + name, code);
		Heroes.getInstance().saveConfig();
	}
	
	public static Map<String, Location> getLocations() {
		Map<String, Location> locs = new HashMap<String, Location>();
		
		for(String key : Heroes.getInstance().getConfig().getConfigurationSection("Spawn").getKeys(false)){
			String loc = Heroes.getInstance().getConfig().getString("Spawn." + key);
			String[] decodify = loc.split(", ");
            World w = Heroes.getInstance().getServer().getWorld(decodify[0]);
            double x = Double.valueOf(decodify[1]);
            double y = Double.valueOf(decodify[2]);
            double z = Double.valueOf(decodify[3]);
            float pitch = Float.valueOf(decodify[5]);
            float yaw = Float.valueOf(decodify[4]);
            Location loca = new Location(w,x,y,z,pitch,yaw);
            locs.put(key, loca);
		}
		return locs;
	}

	public Game getGame() {
		return game;
	}

	public int getSpawnSize() {
		return locations.size();
	}

	public List<Location> getSpawnList() {
		return new ArrayList<Location>(locations.values());
	}

	public List<String> getSpawnNameList() {
		return new ArrayList<String>(locations.keySet());
	}

	public Collection<Location> getLocationsCollection() {
		return locations.values();
	}

	public Map<String, Location> getLocationsMap() {
		return locations;
	}

	public Location getLobby() {
		return lobby;
	}
}
