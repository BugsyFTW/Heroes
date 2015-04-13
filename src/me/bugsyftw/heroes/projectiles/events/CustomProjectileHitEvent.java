package me.bugsyftw.heroes.projectiles.events;

import me.bugsyftw.heroes.projectiles.CustomProjectile;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomProjectileHitEvent extends Event implements Cancellable {
	private static HandlerList handlers = new HandlerList();
	private CustomProjectile projectile;
	private LivingEntity entity;
	private Block block;
	private BlockFace face;
	private boolean cancelled;

	public CustomProjectileHitEvent(CustomProjectile pro, LivingEntity ent) {
		this.projectile = pro;
		this.entity = ent;
	}

	public CustomProjectileHitEvent(CustomProjectile pro, Block b, BlockFace f) {
		this.projectile = pro;
		this.block = b;
		this.face = f;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public CustomProjectile getProjectile() {
		return this.projectile;
	}

	public LivingEntity getHitEntity() {
		return this.entity;
	}

	public Block getHitBlock() {
		return this.block;
	}

	public BlockFace getHitFace() {
		return this.face;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public HitType getHitType() {
		if ((this.block == null) && (this.entity != null)) {
			return HitType.ENTITY;
		}
		if ((this.block != null) && (this.entity == null) && (this.face != null)) {
			return HitType.BLOCK;
		}
		return null;
	}

	public EntityType getProjectileType() {
		return getProjectile().getEntityType();
	}

	public static enum HitType {
		ENTITY, BLOCK;
	}

	public String toString() {
		if (getHitType() == HitType.ENTITY) {
			return "{" + getClass().getName() + " projectile:" + this.projectile.toString() + ", hit entity:" + this.entity.toString() + "}";
		}
		if (getHitType() == HitType.BLOCK) {
			return "{" + getClass().getName() + " projectile:" + this.projectile.toString() + ", hit block:" + this.block.toString() + ", face hit:" + this.face.toString() + "}";
		}
		return getClass().getName();
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean value) {
		this.cancelled = value;
	}
}
