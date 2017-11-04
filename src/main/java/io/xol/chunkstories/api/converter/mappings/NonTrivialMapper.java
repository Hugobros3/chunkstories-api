package io.xol.chunkstories.api.converter.mappings;

import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.world.World;
import io.xol.enklume.MinecraftRegion;

/** For blocks that have some fancy properties we need to consider, like doors or signs. */
public abstract class NonTrivialMapper extends Mapper {
	
	public NonTrivialMapper(Voxel voxel) {
		super(voxel);
	}

	/** We don't use this method for Non-trivial mappers */
	public final int output(int mcId, byte mcMeta) {
		throw new UnsupportedOperationException();
	}
	
	/** Responsible of re-building the input block in chunkstories-space. */
	public abstract void output(World csWorld, int csX, int csY, int csZ, int minecraftBlockId, int minecraftMetaData, MinecraftRegion region, int minecraftCuurrentChunkXinsideRegion, int minecraftCuurrentChunkZinsideRegion, int x, int y, int z);
}
