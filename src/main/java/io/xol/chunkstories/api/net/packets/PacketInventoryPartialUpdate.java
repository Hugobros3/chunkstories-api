//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.exceptions.NullItemException;
import io.xol.chunkstories.api.exceptions.PacketProcessingException;
import io.xol.chunkstories.api.exceptions.UndefinedItemTypeException;
import io.xol.chunkstories.api.item.inventory.Inventory;
import io.xol.chunkstories.api.item.inventory.InventoryTranslator;
import io.xol.chunkstories.api.item.inventory.ItemPile;
import io.xol.chunkstories.api.net.PacketDestinator;
import io.xol.chunkstories.api.net.PacketSender;
import io.xol.chunkstories.api.net.PacketSendingContext;
import io.xol.chunkstories.api.net.PacketWorld;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.net.PacketReceptionContext;

import javax.annotation.Nullable;

public class PacketInventoryPartialUpdate extends PacketWorld {
	@Nullable
	private Inventory inventory;
	private int slotx, sloty;
	@Nullable
	private ItemPile itemPile;

	public PacketInventoryPartialUpdate(World world) {
		super(world);
	}

	public PacketInventoryPartialUpdate(World world, @Nullable Inventory inventory, int slotx, int sloty, @Nullable ItemPile newItemPile) {
		super(world);
		this.inventory = inventory;
		this.slotx = slotx;
		this.sloty = sloty;
		this.itemPile = newItemPile;
	}

	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context)
			throws IOException {
		InventoryTranslator.writeInventoryHandle(out, inventory);

		out.writeInt(slotx);
		out.writeInt(sloty);

		if (itemPile == null)
			out.writeInt(0);
		else {
			itemPile.saveIntoStream(world.getContentTranslator(), out);
		}
	}

	@Override
	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext processor)
			throws IOException, PacketProcessingException {
		inventory = InventoryTranslator.obtainInventoryHandle(in, processor);

		int slotx = in.readInt();
		int sloty = in.readInt();

		try {
			itemPile = ItemPile.obtainItemPileFromStream(processor.getWorld().getContentTranslator(), in);
		} catch (NullItemException e) {
			// This is sane behavior !
			itemPile = null;
		} catch (UndefinedItemTypeException e) {
			// This is slightly more problematic.
			processor.logger().error("Undefined item: ", e);
		}

		if (inventory != null)
			inventory.setItemPileAt(slotx, sloty, itemPile);
	}

}
