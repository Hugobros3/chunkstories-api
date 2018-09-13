//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.generator;

import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.chunk.Chunk;

public class BlankWorldGenerator extends WorldGenerator {
	public BlankWorldGenerator(WorldGeneratorDefinition type, World world) {
		super(type, world);
	}

	@Override
	public void generateChunk(Chunk chunk) {

	}
}
