package io.xol.chunkstories.api.content;

import java.util.Collection;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.EntityDefinition;
import io.xol.chunkstories.api.item.Item;
import io.xol.chunkstories.api.item.ItemDefinition;
import io.xol.chunkstories.api.net.Packet;
import io.xol.chunkstories.api.net.PacketDefinition;
import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.voxel.VoxelDefinition;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** 
 * This interface describes the world-specific mapping from named content definitions to integer IDs.
 */
public interface ContentTranslator {
	/** Returns a Collection containing the internalNames of the mods that were used with this world. */
	public Collection<String> getRequiredMods();
	
	public int getIdForVoxel(VoxelDefinition definition);
	public int getIdForVoxel(Voxel voxel);
	public Voxel getVoxelForId(int id);
	
	public int getIdForItem(ItemDefinition definition);
	public int getIdForItem(Item item);
	public ItemDefinition getItemForId(int id);
	
	public int getIdForEntity(EntityDefinition definition);
	public int getIdForEntity(Entity entity);
	public EntityDefinition getEntityForId(int id);
	
	public int getIdForPacket(PacketDefinition definition);
	public int getIdForPacket(Packet packet);
	public PacketDefinition getPacketForId(int id);
	
}
