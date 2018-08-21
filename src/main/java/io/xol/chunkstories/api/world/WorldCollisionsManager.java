//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world;

import java.util.Iterator;

import org.joml.Vector3dc;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.entity.Entity;

import javax.annotation.Nullable;

public interface WorldCollisionsManager {

	/** Raytraces throught the world to find a solid block
	 * 
	 * @param limit Between 0 and a finite number
	 * @return The exact location of the intersection or null if it didn't found
	 *         one */
	@Nullable
	public Location raytraceSolid(Vector3dc initialPosition, Vector3dc direction, double limit);

	/** Raytraces throught the world to find a solid block
	 * 
	 * @param limit Between 0 and a finite number
	 * @return The exact location of the step just before the intersection ( as to
	 *         get the adjacent block ) or null if it didn't found one */
	@Nullable
	public Location raytraceSolidOuter(Vector3dc initialPosition, Vector3dc direction, double limit);

	/** Raytraces throught the world to find a solid or selectable block
	 * 
	 * @param limit Between 0 and a finite number
	 * @return The exact location of the intersection or null if it didn't found
	 *         one */
	@Nullable
	public Location raytraceSelectable(Location initialPosition, Vector3dc direction, double limit);

	/** Takes into account the voxel terrain and will stop at a solid block,
	 * <b>warning</b> limit can't be == -1 !
	 * 
	 * @param limit Between 0 and a finite number
	 * @return Returns all entities that intersects with the ray within the limit,
	 *         ordered nearest to furthest */
	public Iterator<Entity> rayTraceEntities(Vector3dc initialPosition, Vector3dc direction, double limit);

	/** Ignores any terrain
	 * 
	 * @param limit Either -1 or between 0 and a finite number
	 * @return Returns all entities that intersects with the ray within the limit,
	 *         ordered nearest to furthest */
	public Iterator<Entity> raytraceEntitiesIgnoringVoxels(Vector3dc initialPosition, Vector3dc direction, double limit);

	/** Does a complicated check to see how far the entity can go using the delta
	 * direction, from the 'start' position. Does not actually move anything Returns
	 * the remaining distance in each dimension if it got stuck ( with vec3(0.0,
	 * 0.0, 0.0) meaning it can safely move without colliding with anything ) */
	public Vector3dc runEntityAgainstWorldVoxels(Entity entity, Vector3dc from, Vector3dc delta);

	public Vector3dc runEntityAgainstWorldVoxelsAndEntities(Entity entity, Vector3dc from, Vector3dc delta);

	/** Tries to move the entity and returns the remaining untravelled distance,
	 * will collide with other entities! */
	public Vector3dc tryMovingEntityWithCollisions(Entity entity, Vector3dc from, Vector3dc delta);
}