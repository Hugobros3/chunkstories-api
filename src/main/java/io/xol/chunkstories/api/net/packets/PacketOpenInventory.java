//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.net.packets;

import io.xol.chunkstories.api.client.IngameClient;
import io.xol.chunkstories.api.client.net.ClientPacketsProcessor;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.traits.serializable.TraitInventory;
import io.xol.chunkstories.api.exceptions.PacketProcessingException;
import io.xol.chunkstories.api.item.inventory.Inventory;
import io.xol.chunkstories.api.item.inventory.InventoryTranslator;
import io.xol.chunkstories.api.net.*;
import io.xol.chunkstories.api.world.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketOpenInventory extends PacketWorld {
	protected Inventory inventory;

	public PacketOpenInventory(World world) {
		super(world);
	}

	public PacketOpenInventory(World world, Inventory inventory) {
		super(world);
		this.inventory = inventory;
	}

	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context) throws IOException {
		InventoryTranslator.writeInventoryHandle(out, inventory);
	}

	@Override
	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext processor) throws IOException, PacketProcessingException {
		inventory = InventoryTranslator.obtainInventoryHandle(in, processor);

		if (processor instanceof ClientPacketsProcessor) {
			IngameClient client = ((ClientPacketsProcessor) processor).getContext();
			Entity currentControlledEntity = client.getPlayer().getControlledEntity();

			Inventory ownInventory = currentControlledEntity != null ? currentControlledEntity.traits.tryWith(TraitInventory.class, ei -> ei) : null;

			if (ownInventory != null)
				client.getGui().openInventories(ownInventory, inventory);
			else
				client.getGui().openInventories(inventory);
		}
	}

}
