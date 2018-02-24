//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity;

import org.joml.Vector3dc;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.entity.components.EntityComponent;
import io.xol.chunkstories.api.entity.components.Subscriber;
import io.xol.chunkstories.api.input.Input;
import io.xol.chunkstories.api.physics.CollisionBox;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

public interface Entity
{
	/** Return the object representing the declaration of this entity in a .entities file */
	public EntityDefinition getDefinition();
	
	/** @return the entity's world */
	public World getWorld();
	
	/** @return the location of the entity */
	public Location getLocation();
	
	/** Sets the location of the entity */
	public void setLocation(Location loc);
	
	/** @return the entity's current chunk. Might return null ! */
	public Chunk getChunk();

	/** Updates the entity, called at a rate of 60Hz by default */
	public void tick();

	/** Called when trying to interact with an entity. Returning 'true' will stop the interaction chain and won't allow anything further to interact with it. */
	public boolean handleInteraction(Entity entity, Input input);

	/** Moves the entity by 'delta', ignoring any collision information. */
	public void moveWithoutCollisionRestrain(Vector3dc delta);
	
	/** Overload for {@link #moveWithoutCollisionRestrain(Vector3dc delta)} */
	public void moveWithoutCollisionRestrain(double dx, double dy, double dz);
	
	/** Tries to move the entity by 'delta', will slide arround obstacles as best as it can and then return (the initial delta - the distance actually accomplished) */
	public Vector3dc moveWithCollisionRestrain(Vector3dc delta);

	/** Overload for {@link #moveWithCollisionRestrain(Vector3dc delta)} */
	public Vector3dc moveWithCollisionRestrain(double dx, double dy, double dz);
	
	/** Same as {@link #moveWithCollisionRestrain(Vector3dc delta)} but doesn't actually move anything, just checks */
	public Vector3dc canMoveWithCollisionRestrain(Vector3dc delta);

	/** Overload for {@link #canMoveWithCollisionRestrain(Vector3dc delta)} */
	public Vector3dc canMoveWithCollisionRestrain(double dx, double dy, double dz);
	
	/** Checks wether or not the entity can slide one block down */
	public boolean isOnGround();
	
	/**
	 * Called when controlling/viewing an entity
	 * @param renderer
	 */
	public void setupCamera(RenderingInterface renderer);

	/** @return the UUID of this entity. */
	public long getUUID();
	
	/** Sets the UUID of the entity. Reserved for internals, trying to set/change the UUID after it's been results in an exception. */
	public void setUUID(long uuid);
	
	/**
	 * @return true unless it should be invisible to some players or all
	 * Exemple : dead/removed entity, invisible admin
	 */
	public boolean shouldBeTrackedBy(Player player);

	/** @return false once the entity has been removed from the world */
	public boolean exists();

	/** @return true once the entity has been added into the world */
	public boolean hasSpawned();
	
	/** @return An iterator for all the subscribers that track the changes to this entity */
	public IterableIterator<Subscriber> getAllSubscribers();

	public boolean isSubscribed(StreamTarget to);
	
	/* Internal stuff, not really something you'd have to mess with
	public boolean subscribe(Subscriber subscriber);
	public boolean unsubscribe(Subscriber subscriber);*/
	
	/** Return the first component registered ( you can iterate on them since they are a linked list structure ). */
	public EntityComponent getComponents();

	/** @return The actual list of collision (=>solid) boxes for this entity. Can return null or an empty array. */
	public CollisionBox[] getCollisionBoxes();
	
	/** @return the entity AABB translated to its position */
	public CollisionBox getTranslatedBoundingBox();
	
	/** @return the entity bounding box ( for rendering, presence detection etc, but not collision ! ) */
	public CollisionBox getBoundingBox();
}
