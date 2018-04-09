//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.generator;

import io.xol.chunkstories.api.content.Content.WorldGenerators.WorldGeneratorDefinition;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.api.world.generator.environment.DefaultWorldEnvironment;
import io.xol.chunkstories.api.world.generator.environment.WorldEnvironment;

public class BlankWorldGenerator extends WorldGenerator {
	DefaultWorldEnvironment worldEnv;

	public BlankWorldGenerator(WorldGeneratorDefinition type, World world) {
		super(type, world);
		worldEnv = new DefaultWorldEnvironment(world);
	}

	/*@Override
	public Chunk generateChunk(Chunk c) {
		return c;
	}

	@Override
	public int getTopDataAt(int x, int y) {
		return 0;
	}

	@Override
	public int getHeightAt(int x, int z) {
		return 0;
	}*/

	@Override
	public WorldEnvironment getEnvironment() {
		return worldEnv;
	}

	@Override
	public void generateChunk(Chunk chunk) {
		
	}
}
