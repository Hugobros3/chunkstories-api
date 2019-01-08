//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.generator;

import xyz.chunkstories.api.world.World;
import xyz.chunkstories.api.world.chunk.Chunk;

/** The job of a WorldGenerator is to newEntity (voxel) data and to populate the
 * world with content. It also has duties of providing some rendering hints on
 * the world */
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

	public void generateWorldSlice(Chunk chunks[]) {
		for (int chunkY = 0; chunkY < chunks.length; chunkY++) {
			generateChunk(chunks[chunkY]);
		}
	}

	/** Fills a chunk with content */
	public abstract void generateChunk(Chunk chunk);
}
