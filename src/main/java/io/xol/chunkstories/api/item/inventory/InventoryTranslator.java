//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.item.inventory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.traits.Trait;
import io.xol.chunkstories.api.entity.traits.serializable.TraitInventory;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.net.PacketReceptionContext;
import io.xol.chunkstories.api.voxel.components.VoxelComponent;
import io.xol.chunkstories.api.voxel.components.VoxelInventoryComponent;
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkCell;

import javax.annotation.Nullable;

/**
 * Helper class to translate inventories accross hosts. The remote server has no
 * concept of an object reference from *your* heap, so you have to put a little
 * more work to describe what inventory you are talking about in terms the both
 * of you can understand.
 * 
 * <p>
 * For null inventories, you send a magic byte <br/>
 * For Entity-relative inventories you send the entity UUID & the component
 * Id<br/>
 * For Voxel-relative inventories, you send the xyz position & the component id
 * <br/>
 * For other snowflake inventories made out of pixie dust, you go fuck yourself
 * </p>
 */
public class InventoryTranslator {
	public static void writeInventoryHandle(DataOutputStream out, @Nullable Inventory inventory) throws IOException {
		/*
		 * if(inventory instanceof InventoryLocalCreativeMenu) out.writeByte(0x02); else
		 */
		if (inventory == null || inventory.getHolder() == null)
			out.writeByte(0x00);
		else if (inventory instanceof TraitInventory) {
			TraitInventory entityInventory = (TraitInventory) inventory;

			out.writeByte(0x01);
			out.writeLong(((Entity) inventory.getHolder()).getUUID());
			out.writeShort(entityInventory.id());
		} else if (inventory.getHolder() instanceof VoxelInventoryComponent) {
			VoxelInventoryComponent component = (VoxelInventoryComponent) inventory.getHolder();

			out.writeByte(0x03);
			out.writeInt(component.holder().getX());
			out.writeInt(component.holder().getY());
			out.writeInt(component.holder().getZ());
			out.writeUTF(component.holder().name(component));
		} else
			out.writeByte(0x00);
		// throw new RuntimeException("Untranslatable and Unknown Inventory :
		// "+inventory+", can't describe it in outgoing packets");
	}

	@Nullable
	public static Inventory obtainInventoryHandle(DataInputStream in, PacketReceptionContext context)
			throws IOException {
		byte holderType = in.readByte();
		if (holderType == 0x01) {
			long uuid = in.readLong();
			short traitId = in.readShort();

			Entity entity = context.getWorld().getEntityByUUID(uuid);

			Trait trait = entity.traits.byId()[traitId];
			if (trait != null && trait instanceof TraitInventory) {
				return ((TraitInventory) trait);
			}
		} else if (holderType == 0x03) {
			int x = in.readInt();
			int y = in.readInt();
			int z = in.readInt();

			String traitName = in.readUTF();

			try {
				ChunkCell voxelContext = context.getWorld().peek(x, y, z);
				VoxelComponent com = voxelContext.components().get(traitName);
				if (com != null && com instanceof VoxelInventoryComponent) {
					VoxelInventoryComponent comp = (VoxelInventoryComponent) com;
					return comp.getInventory();
				}
			} catch (WorldException e) {
				// TODO log as warning
				// Ignore and return null
			}
		}

		return null;
	}
}
