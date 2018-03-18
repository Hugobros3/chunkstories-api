//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.voxel.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.item.inventory.BasicInventory;
import io.xol.chunkstories.api.item.inventory.InventoryHolder;
import io.xol.chunkstories.api.item.inventory.ItemPile;
import io.xol.chunkstories.api.net.packets.PacketInventoryPartialUpdate;
import io.xol.chunkstories.api.server.RemotePlayer;
import io.xol.chunkstories.api.world.WorldUser;
import io.xol.chunkstories.api.world.cell.CellComponents;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

public class VoxelInventoryComponent extends VoxelComponent implements InventoryHolder {

	private BasicInventory inventory;
	
	public VoxelInventoryComponent(CellComponents holder, int width, int height) {
		super(holder);
		this.inventory = new BasicInventory(holder.getWorld(), width, height) {

			@Override
			public InventoryHolder getHolder() {
				return VoxelInventoryComponent.this;
			}

			@Override
			public void refreshCompleteInventory() {
				VoxelInventoryComponent.this.pushComponentEveryone();
			}

			@Override
			public void refreshItemSlot(int x, int y, ItemPile pileChanged) {
				for(WorldUser user : holder.users()) {
					if(user instanceof RemotePlayer) {
						PacketInventoryPartialUpdate packet = new PacketInventoryPartialUpdate(this.world, this, x, y, pileChanged);
						RemotePlayer player = (RemotePlayer)user;
						player.pushPacket(packet);
					}
				}
			}
			
			
		};
	}

	@Override
	public void push(StreamTarget destinator, DataOutputStream dos) throws IOException {
		inventory.pushInventory(destinator, dos);
	}

	@Override
	public void pull(StreamSource from, DataInputStream dis) throws IOException {
		inventory.pullInventory(from, dis, holder().getWorld().getGameContext().getContent());
	}

	public BasicInventory getInventory() {
		return inventory;
	}
}
