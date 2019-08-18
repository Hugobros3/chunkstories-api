//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.voxel.structures;

import xyz.chunkstories.api.voxel.Voxel;
import xyz.chunkstories.api.world.World;
import xyz.chunkstories.api.world.cell.AbstractCell;
import xyz.chunkstories.api.world.cell.Cell;
import xyz.chunkstories.api.world.cell.FutureCell;
import xyz.chunkstories.api.world.chunk.Chunk;
import org.joml.Vector3i;

import javax.annotation.Nullable;

public abstract class Structure {
	protected AbstractCell[] data;
	protected Vector3i size;

	protected Vector3i offset = new Vector3i(0);

	public Structure(int width, int height, int length) {
		this.size = new Vector3i(width, height, length);
	}

	public int getWidth() {
		return size.x;
	}

	public int getHeight() {
		return size.y;
	}

	public int getLength() {
		return size.z;
	}

	public Vector3i getSize() {
		return size;
	}

	public final static int FLAG_DONT_OVERWRITE_AIR = 0x1;
	public final static int FLAG_USE_OFFSET = 0x2;

	public void paste(World world, Vector3i position, int flags) {
		Vector3i actualPosition = new Vector3i(position);
		if ((flags & FLAG_USE_OFFSET) != 0)
			actualPosition.add(offset);

		Voxel air = world.getGameContext().getContent().getVoxels().getAir();

		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				for (int z = 0; z < size.z; z++) {
					FutureCell future = new FutureCell(world, actualPosition.x + x, actualPosition.y + y, actualPosition.z + z, air);
					future.setVoxel(data[x + y * size.x + z * size.x * size.y].getVoxel());
					// world.poke(future, null);

					if (!future.getVoxel().isAir() || (flags & FLAG_DONT_OVERWRITE_AIR) == 0)
						world.pokeSimple(future);
				}
			}
		}
	}

	public void paste(Chunk chunk, Vector3i position, int flags) {
		Vector3i actualPosition = new Vector3i(position);
		if ((flags & FLAG_USE_OFFSET) != 0)
			actualPosition.add(offset);

		Voxel air = chunk.getWorld().getGameContext().getContent().getVoxels().getAir();

		int initX = Math.max(0, chunk.getChunkX() * 32 - actualPosition.x);
		int boundX = Math.min(size.x, (chunk.getChunkX() + 1) * 32 - actualPosition.x);

		int initY = Math.max(0, chunk.getChunkY() * 32 - actualPosition.y);
		int boundY = Math.min(size.y, (chunk.getChunkY() + 1) * 32 - actualPosition.y);

		int initZ = Math.max(0, chunk.getChunkZ() * 32 - actualPosition.z);
		int boundZ = Math.min(size.z, (chunk.getChunkZ() + 1) * 32 - actualPosition.z);

		for (int x = initX; x < boundX; x++) {
			for (int y = initY; y < boundY; y++) {
				for (int z = initZ; z < boundZ; z++) {
					FutureCell future = new FutureCell(chunk.getWorld(), actualPosition.x + x, actualPosition.y + y, actualPosition.z + z, air);
					future.setVoxel(data[x + y * size.x + z * size.x * size.y].getVoxel());

					if (!future.getVoxel().isAir() || (flags & FLAG_DONT_OVERWRITE_AIR) == 0)
						chunk.pokeSimpleSilently(actualPosition.x + x, actualPosition.y + y, actualPosition.z + z, future.getVoxel(), future.getMetaData(), 0,
								0);
				}
			}
		}

		chunk.getLightBaker().requestUpdate();
		chunk.getOcclusion().requestUpdate();
		chunk.getMesh().requestUpdate();
	}

	public class StructureCell extends AbstractCell {

		public StructureCell(int x, int y, int z, @Nullable Voxel voxel, int meta, int blocklight, int sunlight) {
			super(x, y, z, voxel, meta, blocklight, sunlight);
		}

		@Override
		public World getWorld() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Cell getNeightbor(int side) {
			// TODO try
			throw new UnsupportedOperationException();
		}

	}
}
