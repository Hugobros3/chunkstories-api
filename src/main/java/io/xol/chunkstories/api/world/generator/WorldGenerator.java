//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.generator;

import io.xol.chunkstories.api.content.Content.WorldGenerators.WorldGeneratorDefinition;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.api.world.generator.environment.WorldEnvironment;

/** The job of a WorldGenerator is to create (voxel) data and to populate the world with content.
 *  It also has duties of providing some rendering hints on the world */
public abstract class WorldGenerator {
	protected final World world;
	protected final WorldGeneratorDefinition definition;

	public WorldGenerator(WorldGeneratorDefinition type, World world) {
		this.world = world;
		this.definition = type;
	}

	public WorldGeneratorDefinition getDefinition() {
		return definition;
	}

	/** Fills a chunk with content */
	public abstract void generateChunk(Chunk chunk);

	///** Returns the initial data {@link VoxelFormat} for summary generation (how it expects the topmostblock to be) */
	//public abstract int getTopDataAt(int x, int z);

	///** Returns the initial height for summary generation (how high it expects the topmost block to be) */
	//public abstract int getHeightAt(int x, int z);
	
	public abstract WorldEnvironment getEnvironment();
}
