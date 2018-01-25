package io.xol.chunkstories.api.world;

import io.xol.chunkstories.api.events.voxel.WorldModificationCause;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.voxel.Voxel;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Extends the notion of voxel context to add in the editability */
public interface EditableVoxelContext extends VoxelContext {
	
	public EditableVoxelContext poke(Voxel voxel, int sunlight, int blocklight, int metadata, WorldModificationCause cause) throws WorldException;
	
	public void pokeSimple(Voxel voxel, int sunlight, int blocklight, int metadata);
	
	public void pokeSimpleSilently(Voxel voxel, int sunlight, int blocklight, int metadata);
	
	public void pokeRaw(int raw_data_bits);
	
	public void pokeRawSilently(int raw_data_bits);
}
