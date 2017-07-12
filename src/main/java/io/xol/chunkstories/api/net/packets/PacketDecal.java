package io.xol.chunkstories.api.net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.joml.Vector3d;

import io.xol.chunkstories.api.client.net.ClientPacketsProcessor;
import io.xol.chunkstories.api.exceptions.PacketProcessingException;
import io.xol.chunkstories.api.net.PacketDestinator;
import io.xol.chunkstories.api.net.PacketSynchPrepared;
import io.xol.chunkstories.api.net.PacketsProcessor;
import io.xol.chunkstories.api.net.PacketSender;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Simply sends a decal to the client to be drawn */
public class PacketDecal extends PacketSynchPrepared
{
	public String decalName;
	public Vector3d position;
	public Vector3d orientation;
	public Vector3d size;

	@Override
	public void sendIntoBuffer(PacketDestinator destinator, DataOutputStream out) throws IOException
	{
		out.writeUTF(decalName);
		out.writeDouble(position.x());
		out.writeDouble(position.y());
		out.writeDouble(position.z());
		out.writeDouble(orientation.x());
		out.writeDouble(orientation.y());
		out.writeDouble(orientation.z());
		out.writeDouble(size.x());
		out.writeDouble(size.y());
		out.writeDouble(size.z());
	}

	@Override
	public void process(PacketSender sender, DataInputStream in, PacketsProcessor processor) throws IOException, PacketProcessingException
	{
		decalName = in.readUTF();
		position = new Vector3d();
		position.x = (in.readDouble());
		position.y = (in.readDouble());
		position.z = (in.readDouble());

		orientation = new Vector3d();
		orientation.x = (in.readDouble());
		orientation.y = (in.readDouble());
		orientation.z = (in.readDouble());

		size = new Vector3d();
		size.x = (in.readDouble());
		size.y = (in.readDouble());
		size.z = (in.readDouble());
		
		if(processor instanceof ClientPacketsProcessor)
		{
			ClientPacketsProcessor cpp = (ClientPacketsProcessor)processor;
			cpp.getContext().getDecalsManager().drawDecal(position, orientation, size, decalName);
		}
	}

}
