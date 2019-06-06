//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets;

import xyz.chunkstories.api.client.IngameClient;
import xyz.chunkstories.api.client.net.ClientPacketsProcessor;
import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.entity.traits.serializable.TraitInventory;
import xyz.chunkstories.api.exceptions.PacketProcessingException;
import xyz.chunkstories.api.item.inventory.Inventory;
import xyz.chunkstories.api.item.inventory.InventoryTranslatorKt;
import xyz.chunkstories.api.net.*;
import xyz.chunkstories.api.world.World;

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
		InventoryTranslatorKt.writeInventoryHandle(out, inventory);
	}

	@Override
	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext processor) throws IOException, PacketProcessingException {
		inventory = InventoryTranslatorKt.obtainInventoryByHandle(in, processor);

		if (processor instanceof ClientPacketsProcessor) {
			IngameClient client = ((ClientPacketsProcessor) processor).getContext();
			Entity currentControlledEntity = client.getPlayer().getControlledEntity();

			Inventory ownInventory = currentControlledEntity != null
					? currentControlledEntity.traits.tryWith(TraitInventory.class, TraitInventory::getInventory)
					: null;

			if (ownInventory != null)
				client.getGui().openInventories(ownInventory, inventory);
			else
				client.getGui().openInventories(inventory);
		}
	}

}
