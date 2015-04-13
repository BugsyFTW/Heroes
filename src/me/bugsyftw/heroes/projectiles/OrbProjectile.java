package me.bugsyftw.heroes.projectiles;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import me.bugsyftw.heroes.projectiles.events.CustomProjectileHitEvent;
import net.minecraft.server.v1_7_R2.AxisAlignedBB;
import net.minecraft.server.v1_7_R2.Block;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityExperienceOrb;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EnumMovingObjectType;
import net.minecraft.server.v1_7_R2.IProjectile;
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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class OrbProjectile extends EntityExperienceOrb implements CustomProjectile, IProjectile {
	private EntityLiving shooter;
	private String name;
	private int lastTick;
	private int age;
	private List<Runnable> runnables = new ArrayList();
	private List<TypedRunnable<OrbProjectile>> typedRunnables = new ArrayList();
	private boolean ignoreSomeBlocks = false;
	private Vector bbv = new Vector(0.3F, 0.3F, 0.3F);

	public OrbProjectile(String name, Location loc, LivingEntity shooter, float power) {
		super(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ(), 0);
		this.shooter = ((CraftLivingEntity) shooter).getHandle();
		this.name = name;
		this.lastTick = MinecraftServer.currentTick;
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

	public OrbProjectile(String name, LivingEntity shooter, float power) {
		super(((CraftLivingEntity) shooter).getHandle().world, shooter.getLocation().getX(), shooter.getLocation().getX(), shooter.getLocation().getX(), 0);
		this.shooter = ((CraftLivingEntity) shooter).getHandle();
		this.name = name;
		this.lastTick = MinecraftServer.currentTick;
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
		return EntityType.EXPERIENCE_ORB;
	}

	public org.bukkit.entity.Entity getEntity() {
		return getBukkitEntity();
	}

	public LivingEntity getShooter() {
		return (LivingEntity) this.shooter.getBukkitEntity();
	}

	public String getProjectileName() {
		return this.name;
	}

	public void h() {
		C();
		int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
		this.age += elapsedTicks;
		this.lastTick = MinecraftServer.currentTick;

		this.lastX = this.locX;
		this.lastY = this.locY;
		this.lastZ = this.locZ;
		this.motY -= 0.03999999910593033D;
		this.X = j(this.locX, (this.boundingBox.b + this.boundingBox.e) / 2.0D, this.locZ);
		move(this.motX, this.motY, this.motZ);

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
				event = new CustomProjectileHitEvent(this, this.world.getWorld().getBlockAt(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d), CraftBlock.notchToBlockFace(movingobjectposition.face));
				Bukkit.getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
					die();
				}
			} else if ((movingobjectposition.entity != null) && ((movingobjectposition.entity instanceof EntityLiving))) {
				LivingEntity living = (LivingEntity) movingobjectposition.entity.getBukkitEntity();
				event = new CustomProjectileHitEvent(this, living);
				Bukkit.getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
					die();
				}
			}
		} else if (this.onGround) {
			event = new CustomProjectileHitEvent(this, getBukkitEntity().getLocation().getBlock().getRelative(BlockFace.DOWN), BlockFace.UP);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				die();
			}
		}
		if (isAlive()) {
			for (Runnable r : this.runnables) {
				r.run();
			}
			for (TypedRunnable<OrbProjectile> r : this.typedRunnables) {
				r.run(this);
			}
		}
	}

	public void b_(EntityHuman entityhuman) {
		if ((entityhuman == this.shooter) && (this.age <= 3)) {
			return;
		}
		LivingEntity living = entityhuman.getBukkitEntity();
		CustomProjectileHitEvent event = new CustomProjectileHitEvent(this, living);
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			die();
		}
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

	public void addTypedRunnable(TypedRunnable<? extends CustomProjectile> r) {
		this.typedRunnables.add((TypedRunnable<OrbProjectile>) r);
	}

	public void removeTypedRunnable(TypedRunnable<? extends CustomProjectile> r) {
		this.typedRunnables.remove(r);
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
