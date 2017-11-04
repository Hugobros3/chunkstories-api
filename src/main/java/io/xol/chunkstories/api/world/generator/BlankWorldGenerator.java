package io.xol.chunkstories.api.world.generator;

import io.xol.chunkstories.api.content.Content.WorldGenerators.WorldGeneratorType;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.api.world.generator.environment.DefaultWorldEnvironment;
import io.xol.chunkstories.api.world.generator.environment.WorldEnvironment;

//(c) 2015-2017 XolioWare Interactive
// http://chunkstories.xyz
// http://xol.io

public class BlankWorldGenerator extends WorldGenerator
{
	DefaultWorldEnvironment worldEnv;
	
	public BlankWorldGenerator(WorldGeneratorType type, World world)
	{
		super(type, world);
		worldEnv = new DefaultWorldEnvironment(world);
	}

	@Override
	public Chunk generateChunk(Chunk c)
	{
		return c;
	}

	@Override
	public int getTopDataAt(int x, int y)
	{
		return 0;
	}

	@Override
	public int getHeightAt(int x, int z)
	{
		return 0;
	}

	@Override
	public WorldEnvironment getEnvironment() {
		return worldEnv;
	}
}
