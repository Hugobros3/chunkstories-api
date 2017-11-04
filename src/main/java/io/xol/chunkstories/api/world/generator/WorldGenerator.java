package io.xol.chunkstories.api.world.generator;

import io.xol.chunkstories.api.content.Content.WorldGenerators.WorldGeneratorType;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.api.world.generator.environment.WorldEnvironment;

//(c) 2015-2017 XolioWare Interactive
// http://chunkstories.xyz
// http://xol.io

/** The job of a WorldGenerator is to create (voxel) data and to populate the world with content.
 *  It also has duties of providing some rendering hints on the world */
public abstract class WorldGenerator
{
	protected final World world;
	protected final WorldGeneratorType type;

	public WorldGenerator(WorldGeneratorType type, World world)
	{
		this.world = world;
		this.type = type;
	}
	
	public WorldGeneratorType getType()
	{
		return type;
	}

	/** Fills a chunk with content */
	public abstract Chunk generateChunk(Chunk chunk);

	/** Returns the initial data {@link VoxelFormat} for summary generation (how it expects the topmostblock to be) */
	public abstract int getTopDataAt(int x, int z);

	/** Returns the initial height for summary generation (how high it expects the topmost block to be) */
	public abstract int getHeightAt(int x, int z);
	
	public abstract WorldEnvironment getEnvironment();
}
