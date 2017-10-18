package io.xol.chunkstories.api.world.chunk;

import org.joml.Vector3dc;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.events.voxel.WorldModificationCause;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.exceptions.world.voxel.IllegalBlockModificationException;
import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.voxel.components.VoxelComponents;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.dummy.DummyContext;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** A fake chunk, for all the non-reality fans 
 * Also, might be usefull when dealing with blocks all by themselves. */
public class DummyChunk implements Chunk
{
	@Override
	public World getWorld()
	{
		return null;
	}

	@Override
	public Region getRegion()
	{
		return null;
	}

	@Override
	public int getChunkX()
	{
		return 0;
	}

	@Override
	public int getChunkY()
	{
		return 0;
	}

	@Override
	public int getChunkZ()
	{
		return 0;
	}

	@Override
	public boolean isAirChunk()
	{
		return true;
	}

	@Override
	public void destroy()
	{
	}

	@Override
	public IterableIterator<Entity> getEntitiesWithinChunk()
	{
		return new IterableIterator<Entity>()
		{

			@Override
			public boolean hasNext()
			{
				return false;
			}

			@Override
			public Entity next()
			{
				return null;
			}

		};
	}

	@Override
	public ChunkVoxelContext peek(Vector3dc location)
	{
		return peek((int)location.x(), (int)location.y(), (int)location.z());
	}

	@Override
	public ChunkVoxelContext peek(int x, int y, int z)
	{
		return new DummyContext(this, x, y, z, peekSimple(x, y, z));
	}

	@Override
	public int peekSimple(int x, int y, int z) {
		
		//Return zero by default
		return 0;
	}

	@Override
	public ChunkVoxelContext poke(int x, int y, int z, int newVoxelData, WorldModificationCause cause)
			throws WorldException {
		throw new IllegalBlockModificationException(peek(x,y,z), "This is a dummy chunk, you can't edit any block.");
	}

	@Override
	public ChunkVoxelContext pokeSilently(int x, int y, int z, int newVoxelData) throws WorldException {
		return poke(x, y, z, newVoxelData, null);
	}

	@Override
	public void pokeSimple(int x, int y, int z, int newVoxelData) {
		//Do nothing
	}

	@Override
	public void pokeSimpleSilently(int x, int y, int z, int newVoxelData) {
		//Do nothing
	}

	@Override
	public VoxelComponents components(int worldX, int worldY, int worldZ) {
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
}
