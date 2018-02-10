package io.xol.chunkstories.api.item.inventory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.components.EntityComponent;
import io.xol.chunkstories.api.entity.components.EntityComponentInventory;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.net.PacketReceptionContext;
import io.xol.chunkstories.api.voxel.components.VoxelComponent;
import io.xol.chunkstories.api.voxel.components.VoxelInventoryComponent;
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkCell;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/**
 * Helper class to translate inventories accross hosts.
 * The remote server has no concept of an object reference from *your* heap, so you have to put a little more work to describe
 * what inventory you are talking about in terms the both of you can understand.
 * 
 * For null inventories, you send a magic byte
 * For Entity-relative inventories, you send the entity UUID & the component Id
 * For Voxel-relative inventories, you send the xyz position & the component id
 * For other snowflake inventories made out of pixie dust, you go fuck yourself
 */
public class InventoryTranslator
{
	public static void writeInventoryHandle(DataOutputStream out, Inventory inventory) throws IOException
	{
		/*if(inventory instanceof InventoryLocalCreativeMenu)
			out.writeByte(0x02); else */
		if(inventory == null || inventory.getHolder() == null)
			out.writeByte(0x00);
		else if(inventory instanceof EntityComponentInventory.EntityInventory) {
			EntityComponentInventory.EntityInventory entityInventory = (EntityComponentInventory.EntityInventory)inventory;
			
			out.writeByte(0x01);
			out.writeLong(((Entity)inventory.getHolder()).getUUID());
			out.writeShort(entityInventory.asComponent().getEntityComponentId());
		}
		else if(inventory.getHolder() instanceof VoxelInventoryComponent) {
			VoxelInventoryComponent component = (VoxelInventoryComponent)inventory.getHolder();
			
			out.writeByte(0x03);
			out.writeInt(component.holder().getX());
			out.writeInt(component.holder().getY());
			out.writeInt(component.holder().getZ());
			out.writeUTF(component.holder().name(component));
		}
		else
			out.writeByte(0x00);
		//	throw new RuntimeException("Untranslatable and Unknown Inventory : "+inventory+", can't describe it in outgoing packets");
	}
	
	public static Inventory obtainInventoryHandle(DataInputStream in, PacketReceptionContext context) throws IOException
	{
		byte holderType = in.readByte();
		if(holderType == 0x01) {
			long uuid = in.readLong();
			short componentId = in.readShort();
			
			Entity entity = context.getWorld().getEntityByUUID(uuid);
			EntityComponent cpn = entity.getComponents().getComponentById(componentId);
			if(cpn != null && cpn instanceof EntityComponentInventory) {
				return ((EntityComponentInventory) cpn).getInventory();
			}
		} else if(holderType == 0x03) {
			int x = in.readInt();
			int y = in.readInt();
			int z = in.readInt();
			
			String componentName = in.readUTF();
			
			try {
				ChunkCell voxelContext = context.getWorld().peek(x, y, z);
				VoxelComponent com = voxelContext.components().get(componentName);
				if(com != null && com instanceof VoxelInventoryComponent) {
					VoxelInventoryComponent comp = (VoxelInventoryComponent)com;
					return comp.getInventory();
				}
			} catch (WorldException e) {
				//TODO log as warning
				//Ignore and return null
			}
		}
		//else if(holderType == 0x02)
		//	return INVENTORY_CREATIVE_TRASH;
		
		return null;
	}
}
