package me.bugsyftw.heroes.projectiles;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public abstract interface CustomProjectile
{
public abstract EntityType getEntityType();

public abstract Entity getEntity();

public abstract LivingEntity getShooter();

public abstract String getProjectileName();

public abstract void setInvulnerable(boolean paramBoolean);

public abstract boolean isInvulnerable();

public abstract Vector getBoundingBoxSize();

public abstract void addRunnable(Runnable paramRunnable);

public abstract void removeRunnable(Runnable paramRunnable);

public abstract void addTypedRunnable(TypedRunnable<? extends CustomProjectile> paramTypedRunnable);

public abstract void removeTypedRunnable(TypedRunnable<? extends CustomProjectile> paramTypedRunnable);

public abstract boolean isIgnoringSomeBlocks();

public abstract void setIgnoreSomeBlocks(boolean paramBoolean);
}
