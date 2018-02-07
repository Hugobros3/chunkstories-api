package io.xol.chunkstories.api.world.dummy;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.events.voxel.WorldModificationCause;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.exceptions.world.voxel.IllegalBlockModificationException;
import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.voxel.components.VoxelComponents;
import io.xol.chunkstories.api.world.EditableVoxelContext;
import io.xol.chunkstories.api.world.VoxelContext;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkVoxelContext;
import io.xol.chunkstories.api.world.chunk.DummyChunk;

public class DummyContext implements ChunkVoxelContext {

	DummyChunk c;
	Location loc;
	int data;
	int x,y,z;
	
	public DummyContext(DummyChunk c, int x, int y, int z, int data) {
		this.c = c;
		loc = new Location(getWorld(), x, y, z);
		this.data = data;
		this.x = x;
		this.y = y;
		this.z = z;

	}

	@Override
	public World getWorld() {
		return DummyWorld.get();
	}

	@Override
	public Location getLocation() {
		return loc;
	}

	@Override
	public int getData() {
		return data;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public int getNeightborData(int side) {
		return 0;
	}

	@Override
	public Voxel getVoxel() {
		//What to do for this ???
		return null;
	}

	@Override
	public Chunk getChunk() {
		// TODO Auto-generated method stub
		return c;
	}

	

	@Override
	public VoxelComponents components() {
		throw new UnsupportedOperationException();
	}


	@Override
	public VoxelContext getNeightbor(int side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVoxel(Voxel voxel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMetaData(int metadata) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSunlight(int sunlight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlocklight(int blocklight) {
		// TODO Auto-generated method stub
		
	}

	public void setRawData(int raw_data_bits) {
		// TODO Auto-generated method stub
		
	}
}
