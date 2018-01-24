package io.xol.chunkstories.api.net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.joml.Vector3d;
import org.joml.Vector3dc;

import io.xol.chunkstories.api.client.net.ClientPacketsProcessor;
import io.xol.chunkstories.api.entity.interfaces.EntityControllable;
import io.xol.chunkstories.api.entity.interfaces.EntityWithVelocity;
import io.xol.chunkstories.api.exceptions.PacketProcessingException;
import io.xol.chunkstories.api.net.PacketDestinator;
import io.xol.chunkstories.api.net.PacketSender;
import io.xol.chunkstories.api.net.PacketSendingContext;
import io.xol.chunkstories.api.net.PacketWorld;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.net.PacketReceptionContext;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/**
 * When adding momentum to a controlled entity (ie the player's current entity), doing so by sending the client it's new velocity directly would
 * could result in weird, laggy behavior when the client is rapidly changing direction ( it's new velocity would be based on his own, RTT-seconds ago ).
 * To avoid this we use a dedicated packet which entire purpose is to tell a player to offset it's velocity by N
 */
public class PacketVelocityDelta extends PacketWorld
{
	public PacketVelocityDelta(World world) {
		super(world);
	}
	
	public PacketVelocityDelta(World world, Vector3dc delta)
	{
		super(world);
		this.delta = delta;
	}
	
	private Vector3dc delta;
	
	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context) throws IOException
	{
		out.writeDouble(delta.x());
		out.writeDouble(delta.y());
		out.writeDouble(delta.z());
	}

	@Override
	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext processor) throws IOException, PacketProcessingException
	{
		Vector3d delta = new Vector3d(in.readDouble(), in.readDouble(), in.readDouble());
		
		EntityControllable entity = ((ClientPacketsProcessor)processor).getContext().getPlayer().getControlledEntity();
		if(entity != null && entity instanceof EntityWithVelocity)
		{
			System.out.println("Debug: received velocity delta "+delta);
			((EntityWithVelocity) entity).getVelocityComponent().addVelocity(delta);
		}
	}

}
