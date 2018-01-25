package io.xol.chunkstories.api.converter.mappings;

import io.xol.chunkstories.api.content.ContentTranslator;
import io.xol.chunkstories.api.voxel.Voxel;

/** 
 * Used by the map importer/converter. 
 */
public abstract class Mapper {
	protected final Voxel voxel;
	protected final int voxelID;
	
	public Mapper(Voxel voxel, ContentTranslator translator) {
		this.voxel = voxel;
		voxelID = translator.getIdForVoxel(voxel);
	}
	
	/** Translates the method's parameters into something in {@link io.xol.chunkstories.api.voxel.VoxelFormat}. */
	public abstract int output(int minecraftId, byte minecraftMeta);
}