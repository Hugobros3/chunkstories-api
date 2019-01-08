//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.converter.mappings;

import xyz.chunkstories.api.voxel.Voxel;
import xyz.chunkstories.api.world.World;
import xyz.chunkstories.api.world.cell.FutureCell;
import io.xol.enklume.MinecraftRegion;

/** For blocks that have some fancy properties we need to consider, like doors
 * or signs. */
public abstract class NonTrivialMapper extends Mapper {

	public NonTrivialMapper(Voxel voxel) {
		super(voxel);
	}

	/** We don't use this method for Non-trivial mappers */
	@Override
	public final void output(int mcId, byte mcMeta, FutureCell fvc) {
		throw new UnsupportedOperationException();
	}

	/** Responsible of re-building the input block in chunkstories-space. */
	public abstract void output(World csWorld, int csX, int csY, int csZ, int minecraftBlockId, int minecraftMetaData, MinecraftRegion region, int minecraftCuurrentChunkXinsideRegion, int minecraftCuurrentChunkZinsideRegion, int x, int y, int z);
}
