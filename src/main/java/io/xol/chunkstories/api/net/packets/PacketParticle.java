package io.xol.chunkstories.api.net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.joml.Vector3d;
import org.joml.Vector3dc;

import io.xol.chunkstories.api.client.net.ClientPacketsProcessor;
import io.xol.chunkstories.api.exceptions.PacketProcessingException;
import io.xol.chunkstories.api.net.PacketDestinator;
import io.xol.chunkstories.api.net.PacketSynchPrepared;
import io.xol.chunkstories.api.net.PacketsProcessor;
import io.xol.chunkstories.api.net.PacketSender;
//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class PacketParticle extends PacketSynchPrepared
{
	private String particleName = "";
	private Vector3dc position;
	private Vector3dc velocity;
	
	public PacketParticle() {
		
	}

	public PacketParticle(String particleName, Vector3dc position, Vector3dc velocity) {
		super();
		this.particleName = particleName;
		this.position = position;
		this.velocity = velocity;
	}

	@Override
	public void fillInternalBuffer(PacketDestinator destinator, DataOutputStream out) throws IOException
	{
		out.writeUTF(particleName);
		out.writeDouble(position.x());
		out.writeDouble(position.y());
		out.writeDouble(position.z());
		out.writeBoolean(velocity != null);
		if (velocity != null)
		{
			out.writeDouble(velocity.x());
			out.writeDouble(velocity.y());
			out.writeDouble(velocity.z());
		}
	}

	@Override
	public void process(PacketSender sender, DataInputStream in, PacketsProcessor processor) throws IOException, PacketProcessingException
	{
		particleName = in.readUTF();
		Vector3d position = new Vector3d();
		position.x = (in.readDouble());
		position.y = (in.readDouble());
		position.z = (in.readDouble());
		
		Vector3d velocity = new Vector3d();
		if(in.readBoolean())
		{
			velocity.x = (in.readDouble());
			velocity.y = (in.readDouble());
			velocity.z = (in.readDouble());
		}
		
		if(processor instanceof ClientPacketsProcessor)
		{
			ClientPacketsProcessor cpp = (ClientPacketsProcessor)processor;
			cpp.getContext().getParticlesManager().spawnParticleAtPositionWithVelocity(particleName, position, velocity);
		}
	}

}
