//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.chunk;

import org.joml.Vector3dc;

import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.events.voxel.WorldModificationCause;
import xyz.chunkstories.api.exceptions.world.WorldException;
import xyz.chunkstories.api.util.IterableIterator;
import xyz.chunkstories.api.voxel.Voxel;
import xyz.chunkstories.api.world.World;
import xyz.chunkstories.api.world.cell.CellComponents;
import xyz.chunkstories.api.world.cell.DummyCell;
import xyz.chunkstories.api.world.region.Region;

/** A fake chunk, for all the non-reality fans Also, might be usefull when
 * dealing with blocks all by themselves. */
public class DummyChunk implements Chunk {
	@Override
	public World getWorld() {
		return null;
	}

	@Override
	public Region getRegion() {
		return null;
	}

	@Override
	public int getChunkX() {
		return 0;
	}

	@Override
	public int getChunkY() {
		return 0;
	}

	@Override
	public int getChunkZ() {
		return 0;
	}

	@Override
	public boolean isAirChunk() {
		return true;
	}

	@Override
	public void destroy() {
	}

	@Override
	public IterableIterator<Entity> getEntitiesWithinChunk() {
		return new IterableIterator<Entity>() {

			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public Entity next() {
				return null;
			}

		};
	}

	@Override
	public ChunkCell peek(Vector3dc location) {
		return peek((int) location.x(), (int) location.y(), (int) location.z());
	}

	@Override
	public ChunkCell peek(int x, int y, int z) {
		return new DummyChunkCell(this, x, y, z);
	}

	class DummyChunkCell extends DummyCell implements ChunkCell {

		public DummyChunkCell(DummyChunk dummyChunk, int x, int y, int z) {
			super(x, y, z, null, 0, 0, 0);
		}

		@Override
		public Chunk getChunk() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CellComponents components() {
			throw new UnsupportedOperationException("components()");
		}

		@Override
		public void refreshRepresentation() {

		}
	}

	@Override
	public Voxel peekSimple(int x, int y, int z) {

		// Return zero by default
		return null;
	}

	@Override
	public CellComponents components(int worldX, int worldY, int worldZ) {
		throw new UnsupportedOperationException("components()");
	}

	@Override
	public ChunkHolder holder() {
		throw new UnsupportedOperationException("holder()");
	}

	@Override
	public void addEntity(Entity entity) {
		throw new UnsupportedOperationException("addEntity()");
	}

	@Override
	public void removeEntity(Entity entity) {
		throw new UnsupportedOperationException("removeEntity()");
	}

	@Override
	public ChunkLightUpdater lightBaker() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChunkMesh mesh() {
		return null;
	}

	@Override
	public int peekRaw(int x, int y, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ChunkCell poke(int x, int y, int z, Voxel voxel, int sunlight, int blocklight, int metadata, WorldModificationCause cause) throws WorldException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pokeSimple(int x, int y, int z, Voxel voxel, int sunlight, int blocklight, int metadata) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pokeSimpleSilently(int x, int y, int z, Voxel voxel, int sunlight, int blocklight, int metadata) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pokeRaw(int x, int y, int z, int newVoxelData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pokeRawSilently(int x, int y, int z, int newVoxelData) {
		// TODO Auto-generated method stub

	}

	@Override
	public ChunkOcclusionManager occlusion() {
		// TODO Auto-generated method stub
		return null;
	}
}
