package io.xol.chunkstories.api.net.packets;

import io.xol.chunkstories.api.client.net.ClientPacketsProcessor;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.net.PacketDestinator;
import io.xol.chunkstories.api.net.PacketReceptionContext;
import io.xol.chunkstories.api.voxel.components.VoxelComponent;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkVoxelContext;
import io.xol.chunkstories.api.net.PacketSender;
import io.xol.chunkstories.api.net.PacketSendingContext;
import io.xol.chunkstories.api.net.PacketWorld;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/**
 * Describes a voxel change, including to the VoxelComponents
 */
public class PacketVoxelUpdate extends PacketWorld
{
	public PacketVoxelUpdate(World world) {
		super(world);
	}
	
	public PacketVoxelUpdate(ChunkVoxelContext context) {
		super(context.getWorld());
		this.context = context;
	}
	
	public PacketVoxelUpdate(ChunkVoxelContext context, VoxelComponent componentToUpdate) {
		super(context.getWorld());
		this.context = context;
		this.componentToUpdate = componentToUpdate;
	}
	
	private ChunkVoxelContext context;
	
	private VoxelComponent componentToUpdate;
	
	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext sendingContext) throws IOException
	{
		out.writeInt(context.getX());
		out.writeInt(context.getY());
		out.writeInt(context.getZ());
		out.writeInt(context.getData());
		
		if(componentToUpdate == null) {
			for(Entry<String, VoxelComponent> entry : context.components().all()) {
				out.writeByte((byte)0x01);
				out.writeUTF(entry.getKey());
				entry.getValue().push(destinator, out);
			}
		}
		else {
			out.writeByte((byte)0x01);
			out.writeUTF(componentToUpdate.name());
			componentToUpdate.push(destinator, out);
		}
		
		//No further information
		out.writeByte((byte)0x00);
	}

	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext processor) throws IOException
	{
		if(processor instanceof ClientPacketsProcessor)
		{
			ClientPacketsProcessor cpp = (ClientPacketsProcessor)processor;
			
			int x = in.readInt();
			int y = in.readInt();
			int z = in.readInt();
			int data = in.readInt();
			byte osef = in.readByte();
			

			try {
				ChunkVoxelContext context = cpp.getWorld().getChunkWorldCoordinates(x, y, z).poke(x, y, z, data, null);
				
				while(osef != 0) {
					String componentName = in.readUTF();
					context.components().get(componentName).pull(sender, in);
					osef = in.readByte();
				}
	
			} catch (WorldException e) {
				// Maybe the world wasn't ready ?
				// Edge case: what happens we receive an update for a chunk we haven't received the data yet ?
				// The best option would be to delay the process but this is complicated for a rare instance
				// Maybe one day
			}
		}
		else {
			//Fail hard
		}
	}
}
