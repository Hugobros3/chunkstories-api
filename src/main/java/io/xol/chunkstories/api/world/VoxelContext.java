package io.xol.chunkstories.api.world;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.physics.CollisionBox;
import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.voxel.VoxelFormat;
import io.xol.chunkstories.api.voxel.VoxelSides;
import io.xol.chunkstories.api.voxel.models.VoxelRenderer;
import io.xol.chunkstories.api.voxel.textures.VoxelTexture;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** The result of a peek/poke command, contains formatted information about a voxel, as well as a few helper methods */
public interface VoxelContext
{
	public int getData();
	
	public World getWorld();
	
	public int getX();
	
	public int getY();
	
	public int getZ();
	
	public default Location getLocation() {
		//Just here so dummy classes are shorter, actual voxel contexts have a field for this
		return new Location(getWorld(), getX(), getY(), getZ());
	}
	
	// Neightbour block helper
	
	/** Might automatically cache the data, or might not depending on context... */
	public int getNeightborData(int side);

	public default int getSideId(int side) {
		return VoxelFormat.id(getNeightborData(side));
	}

	// Helpers for rendering

	public default VoxelRenderer getVoxelRenderer() {
		Voxel voxel = getVoxel();
		return voxel != null ? voxel.getVoxelRenderer(this) : null;
	}

	public default VoxelTexture getTexture(VoxelSides side) {
		Voxel voxel = getVoxel();
		return voxel != null ? voxel.getVoxelTexture(getData(), side, this) : null;
	}

	// Data accessing helpers 
	
	public Voxel getVoxel();
	
	public default int getId() {
		return VoxelFormat.id(getData());
	}
	
	public default int getMetaData() {
		return VoxelFormat.meta(getData());
	}
	
	public default int getSunlight() {
		return VoxelFormat.sunlight(getData());
	}
	
	public default int getBlocklight() {
		return VoxelFormat.blocklight(getData());
	}
	
	/** Returns an array (possibly 0-sized) of collision boxes translated to the actual position of the voxel */
	public default CollisionBox[] getTranslatedCollisionBoxes() {
		Voxel voxel = getVoxel();
		return voxel != null ? voxel.getTranslatedCollisionBoxes(getWorld(), getX(), getY(), getZ()) : new CollisionBox[] {};
	}
}