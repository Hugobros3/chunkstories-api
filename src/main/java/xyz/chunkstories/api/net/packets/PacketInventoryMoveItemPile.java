//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import xyz.chunkstories.api.Location;
import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.entity.traits.serializable.TraitCreativeMode;
import xyz.chunkstories.api.events.item.EventItemDroppedToWorld;
import xyz.chunkstories.api.events.player.PlayerMoveItemEvent;
import xyz.chunkstories.api.exceptions.NullItemException;
import xyz.chunkstories.api.exceptions.UndefinedItemTypeException;
import xyz.chunkstories.api.item.inventory.Inventory;
import xyz.chunkstories.api.item.inventory.InventoryTranslator;
import xyz.chunkstories.api.item.inventory.ItemPile;
import xyz.chunkstories.api.net.PacketDestinator;
import xyz.chunkstories.api.net.PacketReceptionContext;
import xyz.chunkstories.api.net.PacketSender;
import xyz.chunkstories.api.net.PacketSendingContext;
import xyz.chunkstories.api.net.PacketWorld;
import xyz.chunkstories.api.player.Player;
import xyz.chunkstories.api.server.ServerPacketsProcessor.ServerPlayerPacketsProcessor;
import xyz.chunkstories.api.world.World;

import javax.annotation.Nullable;

public class PacketInventoryMoveItemPile extends PacketWorld {
	public ItemPile itemPile;
	@Nullable
	public Inventory sourceInventory, destinationInventory;
	public int sourceX, sourceY, destX, destY;
	public int amount;

	public PacketInventoryMoveItemPile(World world) {
		super(world);
	}

	public PacketInventoryMoveItemPile(World world, ItemPile itemPile, Inventory from, Inventory to, int oldX, int oldY, int newX, int newY, int amount) {
		super(world);
		this.itemPile = itemPile;
		this.sourceInventory = from;
		this.destinationInventory = to;
		this.sourceX = oldX;
		this.sourceY = oldY;
		this.destX = newX;
		this.destY = newY;
		this.amount = amount;
	}

	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context) throws IOException {
		// Describe the move
		out.writeInt(sourceX);
		out.writeInt(sourceY);
		out.writeInt(destX);
		out.writeInt(destY);

		out.writeInt(amount);

		// Describe the inventories
		InventoryTranslator.writeInventoryHandle(out, sourceInventory);
		InventoryTranslator.writeInventoryHandle(out, destinationInventory);

		// Describe the itemPile if we are trying to spawn an item from nowhere
		if (sourceInventory == null || sourceInventory.getHolder() == null) {
			itemPile.saveIntoStream(context.getWorld().getContentTranslator(), out);
		}
	}

	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext processor) throws IOException {
		if (!(processor instanceof ServerPlayerPacketsProcessor)) {
			processor.logger().warn(
					"Received a " + this.getClass().getSimpleName() + " but this GameContext isn't providen with a packet processor made to deal with it");
			return;
		}

		ServerPlayerPacketsProcessor sppc = (ServerPlayerPacketsProcessor) processor;
		Player player = sppc.getPlayer();
		Entity playerEntity = player.getControlledEntity();

		sourceX = in.readInt();
		sourceY = in.readInt();
		destX = in.readInt();
		destY = in.readInt();

		amount = in.readInt();

		sourceInventory = InventoryTranslator.obtainInventoryHandle(in, processor);
		destinationInventory = InventoryTranslator.obtainInventoryHandle(in, processor);

		// If this pile is spawned from the void
		if (sourceInventory == null)// || from == InventoryTranslator.INVENTORY_CREATIVE_TRASH)
		{
			try {
				itemPile = ItemPile.obtainItemPileFromStream(player.getWorld().getContentTranslator(), in);
			} catch (NullItemException e) {
				// This ... isn't supposed to happen
				processor.logger().info("User " + sender + " is trying to spawn a null ItemPile for some reason.");
			} catch (UndefinedItemTypeException e) {
				// This is slightly more problematic
				processor.logger().warn(e.getMessage());
				// e.printStackTrace(processor.getLogger().getPrintWriter());
			}
		} else {
			itemPile = sourceInventory.getItemPileAt(sourceX, sourceY);
		}

		// Check access
		if (sourceInventory != null) {
			if (!sourceInventory.isAccessibleTo(playerEntity)) {
				player.sendMessage("You don't have access to the source inventory.");
				return;
			}
		}

		if (destinationInventory != null) {
			if (!destinationInventory.isAccessibleTo(playerEntity)) {
				player.sendMessage("You don't have access to the destination inventory.");
				return;
			}
		}

		// Check using event
		PlayerMoveItemEvent moveItemEvent = new PlayerMoveItemEvent(player, itemPile, sourceInventory, destinationInventory, sourceX, sourceY, destX, destY,
				amount);
		player.getContext().getPluginManager().fireEvent(moveItemEvent);

		if (!moveItemEvent.isCancelled()) {
			// Restrict item spawning
			if (sourceInventory == null) {
				// player.sendMessage("Notice : dragging stuff from /dev/null to your inventory
				// should be limited by permission.");

				if (player.hasPermission("items.spawn") || playerEntity.traits.tryWithBoolean(TraitCreativeMode.class, ecm -> ecm.get())) {
					// Let it happen when in creative mode or owns items.spawn perm
				} else {
					player.sendMessage("#C00000You are neither in creative mode nor have the items.spawn permission.");
					return;
				}
			}

			// If target inventory is null, this means the item was dropped
			if (destinationInventory == null) {
				// TODO this really needs some kind of permissions system
				// TODO or not ? Maybe the cancellable event deal can prevent this

				if (playerEntity == null) {
					System.out.println("Dropping items isn't possible if the player doesn't control any entity.");
					return;
				}

				// If we're pulling this out of an inventory ( and not /dev/null ), we need to
				// remove it from that
				Inventory sourceInventory = itemPile.getInventory();

				Location loc = playerEntity.getLocation();
				EventItemDroppedToWorld dropItemEvent = new EventItemDroppedToWorld(loc, sourceInventory, itemPile);
				player.getContext().getPluginManager().fireEvent(dropItemEvent);

				if (!dropItemEvent.isCancelled()) {

					if (sourceInventory != null)
						sourceInventory.setItemPileAt(itemPile.getX(), itemPile.getY(), null);

					if (dropItemEvent.getItemEntity() != null)
						loc.getWorld().addEntity(dropItemEvent.getItemEntity());
				}

				return;
			}

			itemPile.moveItemPileTo(destinationInventory, destX, destY, amount);
		}
	}
}
