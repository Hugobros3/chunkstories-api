//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

//package io.xol.chunkstories.api.entity;
//
//import org.joml.Vector3dc;
//
//import io.xol.chunkstories.api.entity.interfaces.EntityRotateable;
//import io.xol.chunkstories.api.entity.traits.TraitAnimated;
//import io.xol.chunkstories.api.rendering.RenderingInterface;
//import io.xol.chunkstories.api.rendering.entity.EntityRenderable;
//
//public interface EntityLiving extends Entity, EntityRenderable, EntityRotateable, TraitAnimated, DamageCause
//{
//	public float getMaxHealth();
//	
//	public float getStartHealth();
//	
//	public void setHealth(float health);
//	
//	public float getHealth();
//	
//	/**
//	 * Damages the entity. Overriding this method may allow the entity to resist better to certain types of damages
//	 * @param cause
//	 * @param damage
//	 * @return Damage effectivly taken
//	 */
//	public float damage(DamageCause damageCause, float damage);
//	
//	public float damage(DamageCause damageCause, HitBox damageZone, float damage);
//	
//	/**
//	 * Returns null if this entity was never hurt, the last offender if it did
//	 */
//	public DamageCause getLastDamageCause();
//	
//	/**
//	 * Returns true if the entity is dead
//	 * If an entity is dead it can't be interacted with anymore
//	 * @return
//	 */
//	public boolean isDead();
//	
//}
