package io.xol.chunkstories.api.voxel.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.item.inventory.BasicInventory;
import io.xol.chunkstories.api.item.inventory.InventoryHolder;
import io.xol.chunkstories.api.serialization.StreamSource;
import io.xol.chunkstories.api.serialization.StreamTarget;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class VoxelInventoryComponent extends VoxelComponent implements InventoryHolder {

	private BasicInventory inventory;
	
	public VoxelInventoryComponent(VoxelComponents holder,  int width, int height) {
		super(holder);
		this.inventory = new BasicInventory(width, height) {

			@Override
			public InventoryHolder getHolder() {
				return VoxelInventoryComponent.this;
			}
			
		};
	}

	@Override
	public void push(StreamTarget destinator, DataOutputStream dos) throws IOException {
		inventory.pushInventory(destinator, dos);
	}

	@Override
	public void pull(StreamSource from, DataInputStream dis) throws IOException {
		inventory.pullInventory(from, dis, holder().getChunk().getWorld().getGameContext().getContent());
	}

	public BasicInventory getInventory() {
		return inventory;
	}
}
