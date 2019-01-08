//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.voxel.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import xyz.chunkstories.api.item.inventory.BasicInventory;
import xyz.chunkstories.api.item.inventory.InventoryHolder;
import xyz.chunkstories.api.item.inventory.ItemPile;
import xyz.chunkstories.api.net.packets.PacketInventoryPartialUpdate;
import xyz.chunkstories.api.server.RemotePlayer;
import xyz.chunkstories.api.world.WorldUser;
import xyz.chunkstories.api.world.cell.CellComponents;
import xyz.chunkstories.api.world.serialization.StreamSource;
import xyz.chunkstories.api.world.serialization.StreamTarget;

import javax.annotation.Nullable;

public class VoxelInventoryComponent extends VoxelComponent implements InventoryHolder {

	private BasicInventory inventory;

	public VoxelInventoryComponent(CellComponents cell, int width, int height) {
		super(cell);
		this.inventory = new BasicInventory(width, height) {

			@Override
			public InventoryHolder getHolder() {
				return VoxelInventoryComponent.this;
			}

			@Override
			public void refreshCompleteInventory() {
				VoxelInventoryComponent.this.pushComponentEveryone();
			}

			@Override
			public void refreshItemSlot(int x, int y, @Nullable ItemPile pileChanged) {
				for (WorldUser user : cell.getChunk().holder().getUsers()) {
					if (user instanceof RemotePlayer) {
						PacketInventoryPartialUpdate packet = new PacketInventoryPartialUpdate(cell.getWorld(), this, x, y, pileChanged);
						RemotePlayer player = (RemotePlayer) user;
						player.pushPacket(packet);
					}
				}
			}

		};
	}

	@Override
	public void push(StreamTarget destinator, DataOutputStream dos) throws IOException {
		inventory.pushInventory(destinator, dos, getHolder().getWorld().getContentTranslator());
	}

	@Override
	public void pull(StreamSource from, DataInputStream dis) throws IOException {
		inventory.pullInventory(from, dis, getHolder().getWorld().getContentTranslator());
	}

	public BasicInventory getInventory() {
		return inventory;
	}
}
