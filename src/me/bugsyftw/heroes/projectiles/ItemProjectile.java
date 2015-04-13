package me.bugsyftw.heroes.projectiles;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import me.bugsyftw.heroes.projectiles.events.CustomProjectileHitEvent;
import me.bugsyftw.heroes.projectiles.events.ItemProjectileHitEvent;
import me.bugsyftw.heroes.utils.ParticleEffect;
import net.minecraft.server.v1_7_R2.AxisAlignedBB;
import net.minecraft.server.v1_7_R2.Block;
import net.minecraft.server.v1_7_R2.Blocks;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityItem;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EnumMovingObjectType;
import net.minecraft.server.v1_7_R2.IProjectile;
import net.minecraft.server.v1_7_R2.Item;
import net.minecraft.server.v1_7_R2.ItemStack;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.MinecraftServer;
import net.minecraft.server.v1_7_R2.MovingObjectPosition;
import net.minecraft.server.v1_7_R2.Vec3D;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;


@SuppressWarnings("deprecation")
public class ItemProjectile extends EntityItem implements IProjectile, CustomProjectile {
	private EntityLiving shooter;
	private int lastTick;
	private String name;
	private List<Runnable> runnables = new ArrayList<Runnable>();
	private List<TypedRunnable<ItemProjectile>> typedRunnables = new ArrayList<TypedRunnable<ItemProjectile>>();
	private boolean ignoreSomeBlocks = false;
	private Vector bbv = new Vector(0.3F, 0.3F, 0.3F);

	public ItemProjectile(String name, Location loc, org.bukkit.inventory.ItemStack itemstack, LivingEntity shooter, float power) {
		super(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ(), null);
		if (CraftItemStack.asNMSCopy(itemstack) != null) {
			setItemStack(CraftItemStack.asNMSCopy(itemstack));
		} else {
			setItemStack(new ItemStack(Item.d(itemstack.getTypeId()), itemstack.getAmount(), itemstack.getData().getData()));
		}
		if (itemstack.equals(Material.AIR)) {
			System.out.println("You cannot shoot air!");
		}
		this.lastTick = MinecraftServer.currentTick;
		this.name = name;
		this.pickupDelay = 2147483647;
		this.shooter = ((CraftLivingEntity) shooter).getHandle();
		a(0.25F, 0.25F);
		setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.locX -= MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * 0.16F;
		this.locY -= 0.10000000149011612D;
		this.locZ -= MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * 0.16F;
		setPosition(this.locX, this.locY, this.locZ);
		this.height = 0.0F;
		float f = 0.4F;
		this.motX = (-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * f);
		this.motZ = (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * f);
		this.motY = (-MathHelper.sin(this.pitch / 180.0F * 3.1415927F) * f);
		shoot(this.motX, this.motY, this.motZ, power * 1.5F, 1.0F);
		this.world.addEntity(this);
	}

	public ItemProjectile(String name, LivingEntity shooter, org.bukkit.inventory.ItemStack item, float power) {
		super(((CraftLivingEntity) shooter).getHandle().world);
		this.lastTick = MinecraftServer.currentTick;
		this.name = name;
		this.pickupDelay = 2147483647;
		setItemStack(CraftItemStack.asNMSCopy(item));
		this.shooter = ((CraftLivingEntity) shooter).getHandle();
		a(0.25F, 0.25F);
		setPositionRotation(shooter.getLocation().getX(), shooter.getLocation().getY() + shooter.getEyeHeight(), shooter.getLocation().getZ(), shooter.getLocation().getYaw(), shooter.getLocation().getPitch());
		this.locX -= MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * 0.16F;
		this.locY -= 0.10000000149011612D;
		this.locZ -= MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * 0.16F;
		setPosition(this.locX, this.locY, this.locZ);
		this.height = 0.0F;
		float f = 0.4F;
		this.motX = (-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * f);
		this.motZ = (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * f);
		this.motY = (-MathHelper.sin(this.pitch / 180.0F * 3.1415927F) * f);
		shoot(this.motX, this.motY, this.motZ, power * 1.5F, 1.0F);
		this.world.addEntity(this);
	}

	@SuppressWarnings("rawtypes")
	public void h() {
		C();
		int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
		this.pickupDelay -= elapsedTicks;
		this.age += elapsedTicks;
		this.lastTick = MinecraftServer.currentTick;

		this.lastX = this.locX;
		this.lastY = this.locY;
		this.lastZ = this.locZ;
		this.motY -= 0.03999999910593033D;
		this.X = j(this.locX, (this.boundingBox.b + this.boundingBox.e) / 2.0D, this.locZ);
		move(this.motX, this.motY, this.motZ);
		boolean flag = ((int) this.lastX != (int) this.locX) || ((int) this.lastY != (int) this.locY) || ((int) this.lastZ != (int) this.locZ);
		if (((flag) || (this.ticksLived % 25 == 0)) && (this.world.getType(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) == Block.b("lava"))) {
			this.motY = 0.2000000029802322D;
			this.motX = ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
			this.motZ = ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
			makeSound("random.fizz", 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
		}
		float f = 0.98F;
		if (this.onGround) {
			f = 0.5880001F;
			Block i = this.world.getType(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ));
			if (i != null) {
				f = i.frictionFactor * 0.98F;
			}
		}
		this.motX *= f;
		this.motY *= 0.9800000190734863D;
		this.motZ *= f;
		if (this.onGround) {
			this.motY *= -0.5D;
		}
		if (this.age >= 1000) {
			die();
		}
		Vec3D vec3d = Vec3D.a(this.locX, this.locY, this.locZ);
		Vec3D vec3d1 = Vec3D.a(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
		MovingObjectPosition movingobjectposition = this.world.rayTrace(vec3d, vec3d1, false, true, false);

		vec3d = Vec3D.a(this.locX, this.locY, this.locZ);
		vec3d1 = Vec3D.a(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
		if (movingobjectposition != null) {
			vec3d1 = Vec3D.a(movingobjectposition.pos.a, movingobjectposition.pos.b, movingobjectposition.pos.c);
		}
		Entity entity = null;
		List list = this.world.getEntities(this, this.boundingBox.a(this.motX, this.motY, this.motZ).grow(2.0D, 2.0D, 2.0D));
		double d0 = 0.0D;
		EntityLiving entityliving = this.shooter;
		for (int i = 0; i < list.size(); i++) {
			Entity entity1 = (Entity) list.get(i);
			if ((entity1.R()) && (entity1 != entityliving)) {
				AxisAlignedBB axisalignedbb = entity1.boundingBox.grow(this.bbv.getX(), this.bbv.getY(), this.bbv.getZ());
				MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);
				if (movingobjectposition1 != null) {
					double d1 = vec3d.distanceSquared(movingobjectposition1.pos);
					if ((d1 < d0) || (d0 == 0.0D)) {
						entity = entity1;
						d0 = d1;
					}
				}
			}
		}
		if (entity != null) {
			movingobjectposition = new MovingObjectPosition(entity);
		}
		CustomProjectileHitEvent event;
		if (movingobjectposition != null) {
			if ((movingobjectposition.type == EnumMovingObjectType.BLOCK) && (!isIgnored(this.world.getWorld().getBlockAt(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d).getType()))) {
				event = new ItemProjectileHitEvent(this, this.world.getWorld().getBlockAt(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d), CraftBlock.notchToBlockFace(movingobjectposition.face), getItem());
				Bukkit.getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
					if (CraftItemStack.asCraftMirror(getItemStack()).getType().isBlock()) {
						ParticleEffect.displayBlockCrack(getBukkitEntity().getLocation(), Item.b(getItemStack().getItem()), (byte) 0, 0.0F, 0.0F, 0.0F, 1.0F, 20);
					} else {
						ParticleEffect.displayIconCrack(getBukkitEntity().getLocation(), Item.b(getItemStack().getItem()), 0.0F, 0.0F, 0.0F, 0.1F, 20);
					}
					die();
				}
			} else if ((movingobjectposition.entity != null) && ((movingobjectposition.entity instanceof EntityLiving))) {
				LivingEntity living = (LivingEntity) movingobjectposition.entity.getBukkitEntity();
				event = new ItemProjectileHitEvent(this, living, getItem());
				Bukkit.getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
					if (CraftItemStack.asCraftMirror(getItemStack()).getType().isBlock()) {
						ParticleEffect.displayBlockCrack(getBukkitEntity().getLocation(), Item.b(getItemStack().getItem()), (byte) 0, 0.0F, 0.0F, 0.0F, 1.0F, 20);
					} else {
						ParticleEffect.displayIconCrack(getBukkitEntity().getLocation(), Item.b(getItemStack().getItem()), 0.0F, 0.0F, 0.0F, 0.1F, 20);
					}
					die();
				}
			}
		} else if (this.onGround) {
			event = new ItemProjectileHitEvent(this, getBukkitEntity().getLocation().getBlock().getRelative(BlockFace.DOWN), BlockFace.UP, getItem());
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				if (CraftItemStack.asCraftMirror(getItemStack()).getType().isBlock()) {
					ParticleEffect.displayBlockCrack(getBukkitEntity().getLocation(), Item.b(getItemStack().getItem()), (byte) 0, 0.0F, 0.0F, 0.0F, 1.0F, 20);
				} else {
					ParticleEffect.displayIconCrack(getBukkitEntity().getLocation(), Item.b(getItemStack().getItem()), 0.0F, 0.0F, 0.0F, 0.1F, 20);
				}
				die();
			}
		}
		if (isAlive()) {
			for (Runnable r : this.runnables) {
				r.run();
			}
			for (TypedRunnable<ItemProjectile> r : this.typedRunnables) {
				r.run(this);
			}
		}
	}

	public void shoot(double d0, double d1, double d2, float f, float f1) {
		float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

		d0 /= f2;
		d1 /= f2;
		d2 /= f2;
		d0 += this.random.nextGaussian() * 0.007499999832361937D * f1;
		d1 += this.random.nextGaussian() * 0.007499999832361937D * f1;
		d2 += this.random.nextGaussian() * 0.007499999832361937D * f1;
		d0 *= f;
		d1 *= f;
		d2 *= f;
		this.motX = d0;
		this.motY = d1;
		this.motZ = d2;
		float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

		this.lastYaw = (this.yaw = (float) (Math.atan2(d0, d2) * 180.0D / 3.1415927410125732D));
		this.lastPitch = (this.pitch = (float) (Math.atan2(d1, f3) * 180.0D / 3.1415927410125732D));
	}

	public EntityType getEntityType() {
		return EntityType.DROPPED_ITEM;
	}

	public org.bukkit.entity.Entity getEntity() {
		return getBukkitEntity();
	}

	public LivingEntity getShooter() {
		return (LivingEntity) this.shooter.getBukkitEntity();
	}

	public void b_(EntityHuman entityhuman) {
		if ((entityhuman == this.shooter) && (this.age <= 3)) {
			return;
		}
		LivingEntity living = entityhuman.getBukkitEntity();
		CustomProjectileHitEvent event = new ItemProjectileHitEvent(this, living, getItem());
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			if (CraftItemStack.asCraftMirror(getItemStack()).getType().isBlock()) {
				ParticleEffect.displayBlockCrack(getBukkitEntity().getLocation(), Item.b(getItemStack().getItem()), (byte) 0, 0.0F, 0.0F, 0.0F, 1.0F, 20);
			} else {
				ParticleEffect.displayIconCrack(getBukkitEntity().getLocation(), Item.b(getItemStack().getItem()), 0.0F, 0.0F, 0.0F, 0.1F, 20);
			}
			die();
		}
	}

	public String getProjectileName() {
		return this.name;
	}

	public org.bukkit.inventory.ItemStack getItem() {
		return CraftItemStack.asCraftMirror(getItemStack());
	}

	public void setInvulnerable(boolean value) {
		try {
			Field f = getClass().getDeclaredField("invulnerable");
			f.setAccessible(true);
			f.set(this, Boolean.valueOf(value));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void addRunnable(Runnable r) {
		this.runnables.add(r);
	}

	public void removeRunnable(Runnable r) {
		this.runnables.remove(r);
	}

	@SuppressWarnings("unchecked")
	public void addTypedRunnable(TypedRunnable<? extends CustomProjectile> r) {
		this.typedRunnables.add((TypedRunnable<ItemProjectile>) r);
	}

	public void removeTypedRunnable(TypedRunnable<? extends CustomProjectile> r) {
		this.typedRunnables.remove(r);
	}

	public ItemStack getItemStack() {
		ItemStack itemstack = getDataWatcher().getItemStack(10);
		if (itemstack == null) {
			return new ItemStack(Blocks.STONE);
		}
		return itemstack;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean isIgnored(Material m) {
		if (!isIgnoringSomeBlocks()) {
			return false;
		}
		switch (m) {
		case ACACIA_STAIRS:
		case AIR:
		case BAKED_POTATO:
		case BED:
		case BEDROCK:
		case CAKE_BLOCK:
		case CARPET:
		case COOKIE:
		case DOUBLE_PLANT:
		case GREEN_RECORD:
		case GRILLED_PORK:
		case JUKEBOX:
			return true;
		}
		return false;
	}

	public boolean isIgnoringSomeBlocks() {
		return this.ignoreSomeBlocks;
	}

	public void setIgnoreSomeBlocks(boolean ignoreSomeBlocks) {
		this.ignoreSomeBlocks = ignoreSomeBlocks;
	}

	public Vector getBoundingBoxSize() {
		return this.bbv;
	}
}
