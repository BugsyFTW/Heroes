package me.bugsyftw.heroes.projectiles.events;

import me.bugsyftw.heroes.projectiles.CustomProjectile;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class ItemProjectileHitEvent extends CustomProjectileHitEvent {
	private ItemStack item;

	public ItemProjectileHitEvent(CustomProjectile pro, Block b, BlockFace f, ItemStack item) {
		super(pro, b, f);
		this.item = item;
	}

	public ItemProjectileHitEvent(CustomProjectile pro, LivingEntity ent, ItemStack item) {
		super(pro, ent);
		this.item = item;
	}

	public ItemStack getItemStack() {
		return this.item;
	}
}
