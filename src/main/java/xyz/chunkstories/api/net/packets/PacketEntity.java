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
import xyz.chunkstories.api.client.net.ClientPacketsProcessor;
import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.entity.Subscriber;
import xyz.chunkstories.api.entity.traits.Trait;
import xyz.chunkstories.api.entity.traits.serializable.TraitSerializable;
import xyz.chunkstories.api.exceptions.UnknownComponentException;
import xyz.chunkstories.api.net.PacketDestinator;
import xyz.chunkstories.api.net.PacketReceptionContext;
import xyz.chunkstories.api.net.PacketSender;
import xyz.chunkstories.api.net.PacketSendingContext;
import xyz.chunkstories.api.net.PacketWorld;
import xyz.chunkstories.api.player.Player;
import xyz.chunkstories.api.server.RemotePlayer;
import xyz.chunkstories.api.world.World;
import xyz.chunkstories.api.world.WorldMaster;

import javax.annotation.Nullable;

public class PacketEntity extends PacketWorld {
	private Entity entity;
	@Nullable
	private TraitSerializable updateSpecificComponent;

	public PacketEntity(World world) {
		super(world);
	}

	public PacketEntity(Entity entityToUpdate) {
		super(entityToUpdate.getWorld());
		this.entity = entityToUpdate;
	}

	public PacketEntity(Entity entity, @Nullable TraitSerializable component) {
		this(entity);
		this.updateSpecificComponent = component;
	}

	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context) throws IOException {
		long entityUUID = entity.getUUID();
		short entityTypeID = (short) entity.getWorld().getContentTranslator().getIdForEntity(entity);

		boolean hideEntity = entity.traitLocation.wasRemoved();
		if (destinator instanceof Subscriber)
			hideEntity |= !entity.subscribers.contains(destinator);

		// System.out.println("telling "+destinator+" about "+entity +"
		// (hide:"+hideEntity+", reg="+entity.subscribers.isRegistered(destinator)+")");

		out.writeLong(entityUUID);
		out.writeShort(entityTypeID);

		out.writeBoolean(hideEntity);

		if (!hideEntity) { // don't push components when all we want is to hide the entity from the
							// player's view
			if (updateSpecificComponent == null) {
				// No specific component specified ? Update all of them.

				// can't use shorter method because of exceptions handling >:(
				// entity.components.all().forEach(c -> c.pushComponentInStream(destinator,
				// out));
				for (Trait trait : entity.traits.all()) {
					if (trait instanceof TraitSerializable) {
						((TraitSerializable) trait).pushComponentInStream(destinator, out);
					}
				}

			} else {
				updateSpecificComponent.pushComponentInStream(destinator, out);
			}
		}

		// Write a -1 to mark the end of the components updates
		out.writeInt(-1);
	}

	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext processor) throws IOException, UnknownComponentException {
		long entityUUID = in.readLong();
		short entityTypeID = in.readShort();

		boolean hideEntity = in.readBoolean();

		if (entityTypeID == -1)
			return;

		World world = processor.getWorld();
		if (world == null)
			return;

		// System.out.println("received packet entity");
		Entity entity = world.getEntityByUUID(entityUUID);

		boolean addToWorld = false;
		// TODO this should be done explicitely by dedicated packet/packet flags
		// Create an entity if the servers tells you to do so
		if (entity == null) {
			if (world instanceof WorldMaster && sender instanceof RemotePlayer) {
				((Player) sender).sendMessage("You are sending packets to the server about a removed entity. Ignoring those.");
				return;
			} else if (!hideEntity) {
				entity = world.getContentTranslator().getEntityForId(entityTypeID).newEntity(world); // This is technically

				entity.setUUID(entityUUID);
				addToWorld = true;
			}
		}

		int componentId = in.readInt();
		// Loop throught all components
		while (componentId >= 0) {
			Trait trait = entity.traits.byId()[componentId];
			if (trait instanceof TraitSerializable) {
				((TraitSerializable) trait).tryPull(sender, in);
			}
			componentId = in.readInt();
		}

		// Add to world if it was missing and we didn't receive the despawn flag
		if (addToWorld && !hideEntity) {
			// Only the WorldMaster is allowed to spawn new entities in the world
			if (processor instanceof ClientPacketsProcessor)
				processor.getWorld().addEntity(entity);
		}

		if (hideEntity && entity != null) {
			world.removeEntity(entity);
		}
	}
}
