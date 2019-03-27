//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.voxel.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.item.Item;
import xyz.chunkstories.api.item.inventory.Inventory;
import xyz.chunkstories.api.item.inventory.InventoryCallbacks;
import xyz.chunkstories.api.item.inventory.InventoryOwner;
import xyz.chunkstories.api.item.inventory.ItemPile;
import xyz.chunkstories.api.net.packets.PacketInventoryPartialUpdate;
import xyz.chunkstories.api.server.RemotePlayer;
import xyz.chunkstories.api.world.WorldUser;
import xyz.chunkstories.api.world.cell.CellComponents;
import xyz.chunkstories.api.world.serialization.StreamSource;
import xyz.chunkstories.api.world.serialization.StreamTarget;

import javax.annotation.Nullable;

public class VoxelInventoryComponent extends VoxelComponent implements InventoryOwner, InventoryCallbacks {

	private Inventory inventory;

	public VoxelInventoryComponent(CellComponents cell, int width, int height) {
		super(cell);
		this.inventory = new Inventory(width, height, this, this);
	}

	@Override
	public void refreshCompleteInventory() {
		VoxelInventoryComponent.this.pushComponentEveryone();
	}

	@Override
	public void refreshItemSlot(int x, int y, @Nullable ItemPile pileChanged) {
		for (WorldUser user : getHolder().getCell().getChunk().getHolder().getUsers()) {
			if (user instanceof RemotePlayer) {
				PacketInventoryPartialUpdate packet = new PacketInventoryPartialUpdate(getHolder().getCell().getWorld(), inventory, x, y, pileChanged);
				RemotePlayer player = (RemotePlayer) user;
				player.pushPacket(packet);
			}
		}
	}

	@Override
	public void push(@NotNull StreamTarget destinator, @NotNull DataOutputStream dos) throws IOException {
		inventory.saveToStream(dos, getHolder().getWorld().getContentTranslator());
		//inventory.pushInventory(destinator, dos, getHolder().getWorld().getContentTranslator());
	}

	@Override
	public void pull(@NotNull StreamSource from, @NotNull DataInputStream dis) throws IOException {
		inventory.loadFromStream(dis, getHolder().getWorld().getContentTranslator());
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override public boolean isAccessibleTo(@NotNull Entity entity) {
		//TODO compute distance ?
		return true;
	}

	@NotNull @Override public String getInventoryName() {
		return getHolder().getCell().getVoxel().getName();
	}

	@Override public boolean isItemAccepted(@NotNull Item item) {
		return true;
	}
}
