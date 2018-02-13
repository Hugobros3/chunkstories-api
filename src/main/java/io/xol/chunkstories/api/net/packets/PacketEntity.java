package io.xol.chunkstories.api.net.packets;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.client.net.ClientPacketsProcessor;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.components.EntityComponent;
import io.xol.chunkstories.api.entity.components.EntityComponentExistence;
import io.xol.chunkstories.api.exceptions.UnknownComponentException;
import io.xol.chunkstories.api.net.PacketDestinator;
import io.xol.chunkstories.api.net.PacketSender;
import io.xol.chunkstories.api.net.PacketSendingContext;
import io.xol.chunkstories.api.net.PacketWorld;
import io.xol.chunkstories.api.net.PacketReceptionContext;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.server.RemotePlayer;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.WorldMaster;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//(c) 2015-2017 XolioWare Interactive
// http://chunkstories.xyz
// http://xol.io

public class PacketEntity extends PacketWorld
{
	private Entity entity;
	private EntityComponent updateSpecificComponent;

	public PacketEntity(World world) {
		super(world);
	}
	
	public PacketEntity(Entity entityToUpdate)
	{
		super(entityToUpdate.getWorld());
		this.entity = entityToUpdate;
	}
	
	public PacketEntity(Entity entity, EntityComponent component)
	{
		this(entity);
		this.updateSpecificComponent = component;
	}

	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context) throws IOException
	{
		long entityUUID = entity.getUUID();
		short entityTypeID = (short) entity.getWorld().getContentTranslator().getIdForEntity(entity);
		
		out.writeLong(entityUUID);
		out.writeShort(entityTypeID);
		
		if(updateSpecificComponent == null) {
			// No specific component specified ? Update all of them.
			entity.getComponents().pushAllComponentsInStream(destinator, out);
		} else { 
			updateSpecificComponent.pushComponentInStream(destinator, out);
			
			//If the entity no longer exists, we make sure we tell the player so he doesn't spawn it again by pushing the existence component
			if(!entity.exists() && !(updateSpecificComponent instanceof EntityComponentExistence))
				entity.getComponents().pushComponentInStream(destinator, out);
		}
		
		//Write a 0 to mark the end of the components updates
		out.writeInt(0);
	}

	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext processor) throws IOException, UnknownComponentException
	{
		long entityUUID = in.readLong();
		short entityTypeID = in.readShort();
		
		if(entityTypeID == -1)
			return;
		
		World world = processor.getWorld();
		if(world == null)
			return;
		
		Entity entity = world.getEntityByUUID(entityUUID);
		
		boolean addToWorld = false;
		
		//TODO this should be done explicitely by dedicated packet/packet flags
		//Create an entity if the servers tells you to do so
		if(entity == null)
		{
			if(world instanceof WorldMaster && sender instanceof RemotePlayer) {
				((Player) sender).sendMessage("You are sending packets to the server about a removed entity. Ignoring those.");
				return;
			} else {
				entity = processor.getWorld().getContentTranslator().
						getEntityForId(entityTypeID).
						create(new Location(world, 0, 0, 0)); // This is technically wrong
				
				entity.setUUID(entityUUID);
				
				addToWorld = true;
			}
		}
		
		int componentId = in.readInt();
		//Loop throught all components
		while(componentId != 0)
		{
			try {
				entity.getComponents().tryPullComponentInStream(componentId, sender, in);
			}
			catch(UnknownComponentException e) {
				
				processor.logger().warn(e.getMessage());
			}
			componentId = in.readInt();
		}
		
		//Add to world if it was missing and we didn't receive the despawn flag
		if(addToWorld && entity.exists())
		{
			//Only the WorldMaster is allowed to spawn new entities in the world
			if(processor instanceof ClientPacketsProcessor)
				processor.getWorld().addEntity(entity);
		}
	}
}
