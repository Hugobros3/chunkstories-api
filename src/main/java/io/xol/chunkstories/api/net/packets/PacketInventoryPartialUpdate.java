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

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class PacketInventoryPartialUpdate extends PacketWorld
{
	private Inventory inventory;
	private int slotx, sloty;
	private ItemPile itemPile;

	public PacketInventoryPartialUpdate(World world)
	{
		super(world);
	}

	public PacketInventoryPartialUpdate(World world, Inventory inventory, int slotx, int sloty, ItemPile newItemPile)
	{
		super(world);
		this.inventory = inventory;
		this.slotx = slotx;
		this.sloty = sloty;
		this.itemPile = newItemPile;
	}

	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context) throws IOException
	{
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
	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext processor) throws IOException, PacketProcessingException
	{
		inventory = InventoryTranslator.obtainInventoryHandle(in, processor);

		int slotx = in.readInt();
		int sloty = in.readInt();

		try
		{
			itemPile = ItemPile.obtainItemPileFromStream(processor.getWorld().getContentTranslator(), in);
		}
		catch (NullItemException e)
		{
			//This is fine.
			itemPile = null;
		}
		catch (UndefinedItemTypeException e)
		{
			//This is slightly more problematic.
			
			processor.logger().error("Undefined item: ", e);
			//processor.getContext().logger().log(e.getMessage(), LogLevel.WARN);
			//e.printStackTrace(processor.getContext().logger().getPrintWriter());
			//e.printStackTrace();
		}
		
		if (inventory != null)
			inventory.setItemPileAt(slotx, sloty, itemPile);
	}

}
