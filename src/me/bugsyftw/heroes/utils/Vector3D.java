package me.bugsyftw.heroes.utils;
 
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
 
public class Vector3D {
    /**
     * Represents the null (0, 0, 0) origin.
     */
    public static final Vector3D ORIGIN = new Vector3D(0, 0, 0);
 
    // Use protected members, like Bukkit
    public final double x;
    public final double y;
    public final double z;
 
    /**
     * Construct an immutable 3D vector.
     */
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
 
    /**
     * Construct an immutable floating point 3D vector from a location object.
     * @param location - the location to copy.
     */
    public Vector3D(Location location) {
        this(location.toVector());
    }
 
    /**
     * Construct an immutable floating point 3D vector from a mutable Bukkit vector.
     * @param vector - the mutable real Bukkit vector to copy.
     */
    public Vector3D(Vector vector) {
        if (vector == null)
            throw new IllegalArgumentException("Vector cannot be NULL.");
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }
 
    /**
     * Convert this instance to an equivalent real 3D vector.
     * @return Real 3D vector.
     */
    public Vector toVector() {
        return new Vector(x, y, z);
    }
 
    /**
     * Adds the current vector and a given position vector, producing a result vector.
     * @param other - the other vector.
     * @return The new result vector.
     */
    public Vector3D add(Vector3D other) {
        if (other == null)
            throw new IllegalArgumentException("other cannot be NULL");
        return new Vector3D(x + other.x, y + other.y, z + other.z);
    }
 
    /**
     * Adds the current vector and a given vector together, producing a result vector.
     * @param other - the other vector.
     * @return The new result vector.
     */
    public Vector3D add(double x, double y, double z) {
        return new Vector3D(this.x + x, this.y + y, this.z + z);
    }
 
    /**
     * Substracts the current vector and a given vector, producing a result position.
     * @param other - the other position.
     * @return The new result position.
     */
    public Vector3D subtract(Vector3D other) {
        if (other == null)
            throw new IllegalArgumentException("other cannot be NULL");
        return new Vector3D(x - other.x, y - other.y, z - other.z);
    }
 
    /**
     * Substracts the current vector and a given vector together, producing a result vector.
     * @param other - the other vector.
     * @return The new result vector.
     */
    public Vector3D subtract(double x, double y, double z) {
        return new Vector3D(this.x - x, this.y - y, this.z - z);
    }
 
    /**
     * Multiply each dimension in the current vector by the given factor.
     * @param factor - multiplier.
     * @return The new result.
     */
    public Vector3D multiply(int factor) {
        return new Vector3D(x * factor, y * factor, z * factor);
    }
 
    /**
     * Multiply each dimension in the current vector by the given factor.
     * @param factor - multiplier.
     * @return The new result.
     */
    public Vector3D multiply(double factor) {
        return new Vector3D(x * factor, y * factor, z * factor);
    }
 
    /**
     * Divide each dimension in the current vector by the given divisor.
     * @param divisor - the divisor.
     * @return The new result.
     */
    public Vector3D divide(int divisor) {
        if (divisor == 0)
            throw new IllegalArgumentException("Cannot divide by null.");
        return new Vector3D(x / divisor, y / divisor, z / divisor);
    }
 
    /**
     * Divide each dimension in the current vector by the given divisor.
     * @param divisor - the divisor.
     * @return The new result.
     */
    public Vector3D divide(double divisor) {
        if (divisor == 0)
            throw new IllegalArgumentException("Cannot divide by null.");
        return new Vector3D(x / divisor, y / divisor, z / divisor);
    }
 
    /**
     * Retrieve the absolute value of this vector.
     * @return The new result.
     */
    public Vector3D abs() {
        return new Vector3D(Math.abs(x), Math.abs(y), Math.abs(z));
    }
 
    @Override
    public String toString() {
        return String.format("[x: %s, y: %s, z: %s]", x, y, z);
    }
    
	public static Entity getEntityInSight(Player player, Double scanDistance){
        Location playerLoc = player.getEyeLocation();
        Vector3D playerDirection = new Vector3D(playerLoc.getDirection());
        Vector3D start = new Vector3D(playerLoc);
        Vector3D end = start.add(playerDirection.multiply(scanDistance));
        Entity inSight = null;
 
        for(Entity nearbyEntity : player.getNearbyEntities(scanDistance, scanDistance, scanDistance)){
            Vector3D nearbyLoc = new Vector3D(nearbyEntity.getLocation());
 
            //Bounding box
//            Vector3D min = nearbyLoc.subtract(0.5D, 0, 0.5D);
//            Vector3D max = nearbyLoc.add(0.5D, 1.67D, 0.5D);
            
            Vector3D min = nearbyLoc.subtract(1D, .2D, 1D);
            Vector3D max = nearbyLoc.add(1D, 2D, 1D);
 
            if(hasIntersection(start, end, min, max)){
                if(inSight == null || inSight.getLocation().distanceSquared(playerLoc) > nearbyEntity.getLocation().distanceSquared(playerLoc)){
                    inSight = nearbyEntity;
                }
            }
        }
 
        return inSight;
    }
 
    private static boolean hasIntersection(Vector3D start, Vector3D end, Vector3D min, Vector3D max) {
        final double epsilon = 0.0001f;
 
        Vector3D d = end.subtract(start).multiply(0.5);
        Vector3D e = max.subtract(min).multiply(0.5);
        Vector3D c = start.add(d).subtract(min.add(max).multiply(0.5));
        Vector3D ad = d.abs();
 
        if(Math.abs(c.x) > e.x + ad.x){
            return false;
        }
 
        if(Math.abs(c.y) > e.y + ad.y){
            return false;
        }
 
        if(Math.abs(c.z) > e.x + ad.z){
            return false;
        }
 
        if(Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon){
            return false;
        }
 
        if(Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon){
            return false;
        }
 
        if(Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon){
            return false;
        }
 
        return true;
    }
    
    public static List<Entity> getNearbyEntities(Location where, int range) {
    	List<Entity> found = new ArrayList<Entity>();
    	 
    	for (Entity entity : where.getWorld().getEntities()) {
	    	if (isInBorder(where, entity.getLocation(), range)) {
	    		found.add(entity);
	    	}
	    }
    	return found;
    }
    
    public static boolean isInBorder(Location center, Location notCenter, int range) {
    		int x = center.getBlockX(), z = center.getBlockZ(), y = center.getBlockY();
    		int x1 = notCenter.getBlockX(), z1 = notCenter.getBlockZ(), y1 = notCenter.getBlockY();
    	 
    		if (x1 >= (x + range) || z1 >= (z + range) || y1 >= (y + range) || x1 <= (x - range) || z1 <= (z - range) || y1 <= (y - range)) {
    			return false;
    		}
    		return true;
    	}
}