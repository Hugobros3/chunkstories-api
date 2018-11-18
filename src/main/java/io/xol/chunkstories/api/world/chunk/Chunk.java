//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.chunk;

import org.joml.Vector3dc;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.events.voxel.WorldModificationCause;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.util.concurrency.Fence;
import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.voxel.VoxelFormat;
import io.xol.chunkstories.api.voxel.components.VoxelComponent;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.World.WorldCell;
import io.xol.chunkstories.api.world.cell.CellComponents;
import io.xol.chunkstories.api.world.region.Region;

import javax.annotation.Nullable;

/** Contains 32x32x32 voxels worth of data */
public interface Chunk {
	public World getWorld();

	public Region getRegion();

	public ChunkHolder holder();

	public int getChunkX();

	public int getChunkY();

	public int getChunkZ();

	/** Get the data contained in this chunk as full 32-bit data format ( see
	 * {@link VoxelFormat}) The coordinates are internally modified to map to the
	 * chunk, meaning you can access it both with world coordinates or 0-31 in-chunk
	 * coordinates Just don't give it negatives
	 * 
	 * @return the data contained in this chunk as full 32-bit data format ( see
	 *         {@link VoxelFormat})
	 * @throws WorldException if it couldn't peek the world at the specified
	 *             location for some reason */
	public ChunkCell peek(int x, int y, int z);

	/** Convenient overload of peek() to take a Vector3dc derivative ( ie: a
	 * Location object ) */
	public ChunkCell peek(Vector3dc location);

	/** Alternative to peek() that does not newEntity any VoxelContext object<br/>
	 * <b>Does not throw exceptions</b>, instead safely returns zero upon
	 * failure. */
	public Voxel peekSimple(int x, int y, int z);

	/** Peek the raw data of the chunk */
	public int peekRaw(int x, int y, int z);

	/** Poke new information in a voxel getCell.
	 * 
	 * If 'voxel' is null the voxel bits will not be updated. If 'sunlight' is -1
	 * the sunlight bits will not be updated. If 'blocklight' is -1 the blocklight
	 * bits will not be updated. If 'metadata' is -1 the metadata bits will not be
	 * updated.
	 * 
	 * It will also trigger lightning and such updates
	 * 
	 * @throws WorldException if it couldn't poke the world at the specified
	 *             location for some reason */
	public ChunkCell poke(int x, int y, int z, @Nullable Voxel voxel, int sunlight, int blocklight, int metadata, @Nullable WorldModificationCause cause) throws WorldException;

	/** Poke new information in a voxel getCell.
	 * 
	 * If 'voxel' is null the voxel bits will not be updated. If 'sunlight' is -1
	 * the sunlight bits will not be updated. If 'blocklight' is -1 the blocklight
	 * bits will not be updated. If 'metadata' is -1 the metadata bits will not be
	 * updated.
	 * 
	 * It will also trigger lightning and such updates */
	public void pokeSimple(int x, int y, int z, @Nullable Voxel voxel, int sunlight, int blocklight, int metadata);

	/** Poke new information in a voxel getCell.
	 * 
	 * If 'voxel' is null the voxel bits will not be updated. If 'sunlight' is -1
	 * the sunlight bits will not be updated. If 'blocklight' is -1 the blocklight
	 * bits will not be updated. If 'metadata' is -1 the metadata bits will not be
	 * updated. */
	public void pokeSimpleSilently(int x, int y, int z, @Nullable Voxel voxel, int sunlight, int blocklight, int metadata);

	/** Does the same as {@link #poke(x,y,z,d)} but without creating any
	 * VoxelContext object or triggering any updates<br/>
	 * <b>Does not throw exceptions</b>, instead fails silently. */
	public void pokeRaw(int x, int y, int z, int newVoxelData);

	/** Does the same as {@link #poke(x,y,z,d)} but without creating any
	 * VoxelContext object or triggering any updates<br/>
	 * <b>Does not throw exceptions</b>, instead fails silently. */
	public void pokeRawSilently(int x, int y, int z, int newVoxelData);

	public ChunkOcclusionManager occlusion();

	public interface ChunkOcclusionManager {
		/** Increments the needed updates counter but doesn't spawn a task */
		public void incrementPendingUpdates();

		/** Increments the needed updates counter, spawns a task if none exists or is
		 * pending execution */
		public Fence requestUpdate();

		/** Spawns a if there are unbaked modifications and no task is pending
		 * execution */
		public void spawnUpdateTaskIfNeeded();

		/** Returns how many updates have yet to be done */
		public int pendingUpdates();
	}

	/** Returns the interface responsible of updating the voxel light of this
	 * chunk */
	public ChunkLightUpdater lightBaker();

	public ChunkMesh mesh();

	public interface ChunkMesh {
		/** Increments the needed updates counter but doesn't spawn a task */
		public void incrementPendingUpdates();

		// renderer should figure that on it's own

		///** Increments the needed updates counter, spawns a task if none exists or is
		// * pending execution */
		//public Fence requestUpdate();

		///** Spawns a if there are unbaked modifications and no task is pending
		// * execution */
		//public void spawnUpdateTaskIfNeeded();

		/** Returns how many updates have yet to be done */
		public int pendingUpdates();
	}

	public boolean isAirChunk();

	/** Obtains the EntityVoxel saved at the given location */
	public CellComponents components(int worldX, int worldY, int worldZ);

	/** Add some entity to the chunk's confines */
	public void addEntity(Entity entity);

	/** Remove some entity from the chunk's confines */
	public void removeEntity(Entity entity);

	public IterableIterator<Entity> getEntitiesWithinChunk();

	public interface ChunkCell extends WorldCell {
		public Chunk getChunk();

		public CellComponents components();

		@Deprecated
		/** Accesses the raw data in that getCell. Reserved for internal engine purposes! */
		public int getData();

		public void refreshRepresentation();
	}

	public interface FreshChunkCell extends ChunkCell {
		public void registerComponent(String name, VoxelComponent component);
	}

	public void destroy();

}