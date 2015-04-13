package me.bugsyftw.heroes.projectiles;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.bugsyftw.heroes.projectiles.events.CustomProjectileHitEvent;
import net.minecraft.server.v1_7_R2.AxisAlignedBB;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EnumMovingObjectType;
import net.minecraft.server.v1_7_R2.IProjectile;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.MinecraftServer;
import net.minecraft.server.v1_7_R2.MovingObjectPosition;
import net.minecraft.server.v1_7_R2.Vec3D;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

@SuppressWarnings({ "unchecked", "rawtypes", "incomplete-switch" })
public class ProjectileScheduler implements Runnable, IProjectile, CustomProjectile {
	private String name;
	private EntityLiving shooter;
	private Entity e;
	private Random random;
	private int age;
	private int lastTick;
	private int id;
	private List<Runnable> runnables = new ArrayList();
	private List<TypedRunnable<ProjectileScheduler>> typedRunnables = new ArrayList();
	private boolean ignoreSomeBlocks = false;
	private Vector bbv = new Vector(1.0F, 1.0F, 1.0F);

	public ProjectileScheduler(String name, org.bukkit.entity.Entity e, LivingEntity shooter, float power, Plugin plugin) {
		this.name = name;
		this.shooter = ((CraftLivingEntity) shooter).getHandle();
		this.e = ((CraftEntity) e).getHandle();
		try {
			Field f = Entity.class.getDeclaredField("random");
			f.setAccessible(true);
			this.random = ((Random) f.get(this.e));
		} catch (Throwable t) {
			t.printStackTrace();
		}
		this.lastTick = MinecraftServer.currentTick;
		this.e.setPositionRotation(shooter.getLocation().getX(), shooter.getLocation().getY(), shooter.getLocation().getZ(), shooter.getLocation().getYaw(), shooter.getLocation().getPitch());
		this.e.locX -= MathHelper.cos(this.e.yaw / 180.0F * 3.1415927F) * 0.16F;
		this.e.locY -= 0.10000000149011612D;
		this.e.locZ -= MathHelper.sin(this.e.yaw / 180.0F * 3.1415927F) * 0.16F;
		this.e.setPosition(this.e.locX, this.e.locY, this.e.locZ);
		this.e.height = 0.0F;
		float f = 0.4F;
		this.e.motX = (-MathHelper.sin(this.e.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.e.pitch / 180.0F * 3.1415927F) * f);
		this.e.motZ = (MathHelper.cos(this.e.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.e.pitch / 180.0F * 3.1415927F) * f);
		this.e.motY = (-MathHelper.sin(this.e.pitch / 180.0F * 3.1415927F) * f);
		shoot(this.e.motX, this.e.motY, this.e.motZ, power * 1.5F, 1.0F);
		this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 1L, 1L);
	}

	public void run() {
		int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
		this.age += elapsedTicks;
		this.lastTick = MinecraftServer.currentTick;
		if (this.age >= 1000) {
			this.e.die();
			Bukkit.getScheduler().cancelTask(this.id);
		}
		Vec3D vec3d = Vec3D.a(this.e.locX, this.e.locY, this.e.locZ);
		Vec3D vec3d1 = Vec3D.a(this.e.locX + this.e.motX, this.e.locY + this.e.motY, this.e.locZ + this.e.motZ);
		MovingObjectPosition movingobjectposition = this.e.world.rayTrace(vec3d, vec3d1, false, true, false);

		vec3d = Vec3D.a(this.e.locX, this.e.locY, this.e.locZ);
		vec3d1 = Vec3D.a(this.e.locX + this.e.motX, this.e.locY + this.e.motY, this.e.locZ + this.e.motZ);
		if (movingobjectposition != null) {
			vec3d1 = Vec3D.a(movingobjectposition.pos.a, movingobjectposition.pos.b, movingobjectposition.pos.c);
		}
		Entity entity = null;
		List<Entity> list = this.e.world.getEntities(this.e, this.e.boundingBox.a(this.e.motX, this.e.motY, this.e.motZ).grow(2.0D, 2.0D, 2.0D));
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
			if ((movingobjectposition.type == EnumMovingObjectType.BLOCK) && (!isIgnored(this.e.world.getWorld().getBlockAt(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d).getType()))) {
				event = new CustomProjectileHitEvent(this, this.e.world.getWorld().getBlockAt(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d), CraftBlock.notchToBlockFace(movingobjectposition.face));
				Bukkit.getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
					this.e.die();
					Bukkit.getScheduler().cancelTask(this.id);
				}
			} else if ((movingobjectposition.entity != null) && ((movingobjectposition.entity instanceof EntityLiving))) {
				LivingEntity living = (LivingEntity) movingobjectposition.entity.getBukkitEntity();
				event = new CustomProjectileHitEvent(this, living);
				Bukkit.getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
					this.e.die();
					Bukkit.getScheduler().cancelTask(this.id);
				}
			}
		} else if (this.e.onGround) {
			event = new CustomProjectileHitEvent(this, this.e.getBukkitEntity().getLocation().getBlock().getRelative(BlockFace.DOWN), BlockFace.UP);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				this.e.die();
				Bukkit.getScheduler().cancelTask(this.id);
			}
		}
		if (this.e.isAlive()) {
			for (Runnable r : this.runnables) {
				r.run();
			}
			for (TypedRunnable<ProjectileScheduler> r : this.typedRunnables) {
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
		this.e.motX = d0;
		this.e.motY = d1;
		this.e.motZ = d2;
		float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

		this.e.lastYaw = (this.e.yaw = (float) (Math.atan2(d0, d2) * 180.0D / 3.1415927410125732D));
		this.e.lastPitch = (this.e.pitch = (float) (Math.atan2(d1, f3) * 180.0D / 3.1415927410125732D));
	}

	public EntityType getEntityType() {
		return this.e.getBukkitEntity().getType();
	}

	public org.bukkit.entity.Entity getEntity() {
		return this.e.getBukkitEntity();
	}

	public LivingEntity getShooter() {
		return (LivingEntity) this.shooter.getBukkitEntity();
	}

	public String getProjectileName() {
		return this.name;
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

	public boolean isInvulnerable() {
		return this.e.isInvulnerable();
	}

	public void addRunnable(Runnable r) {
		this.runnables.add(r);
	}

	public void removeRunnable(Runnable r) {
		this.runnables.remove(r);
	}

	public void addTypedRunnable(TypedRunnable<? extends CustomProjectile> r) {
		this.typedRunnables.add((TypedRunnable<ProjectileScheduler>) r);
	}

	public void removeTypedRunnable(TypedRunnable<? extends CustomProjectile> r) {
		this.typedRunnables.remove(r);
	}

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
