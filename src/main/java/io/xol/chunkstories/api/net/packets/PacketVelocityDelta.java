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
import io.xol.chunkstories.api.net.PacketSynchPrepared;
import io.xol.chunkstories.api.net.PacketsProcessor;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/**
 * We don't want controlled entities to lag-out when the master-side applies momentum to them, thus we use a special packet for this corner case.
 */
public class PacketVelocityDelta extends PacketSynchPrepared
{
	public PacketVelocityDelta()
	{
		
	}
	
	public PacketVelocityDelta(Vector3dc delta)
	{
		this.delta = delta;
	}
	
	private Vector3dc delta;
	
	@Override
	public void sendIntoBuffer(PacketDestinator destinator, DataOutputStream out) throws IOException
	{
		out.writeDouble(delta.x());
		out.writeDouble(delta.y());
		out.writeDouble(delta.z());
	}

	@Override
	public void process(PacketSender sender, DataInputStream in, PacketsProcessor processor) throws IOException, PacketProcessingException
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
