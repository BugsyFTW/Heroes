package me.bugsyftw.heroes.projectiles.events;

import me.bugsyftw.heroes.projectiles.CustomProjectile;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;

public class BlockProjectileHitEvent extends CustomProjectileHitEvent {
	private Material mat;
	private int data;

	public BlockProjectileHitEvent(CustomProjectile pro, Block b, BlockFace f, Material mat, int data) {
		super(pro, b, f);
		this.mat = mat;
		this.data = data;
	}

	public BlockProjectileHitEvent(CustomProjectile pro, LivingEntity ent, Material mat, int data) {
		super(pro, ent);
		this.mat = mat;
		this.data = data;
	}

	public Material getMaterial() {
		return this.mat;
	}

	public int getData() {
		return this.data;
	}
}
