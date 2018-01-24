package io.xol.chunkstories.api.net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.net.PacketDestinator;
import io.xol.chunkstories.api.net.PacketSender;
import io.xol.chunkstories.api.net.PacketSendingContext;
import io.xol.chunkstories.api.net.PacketWorld;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.net.PacketReceptionContext;

/** Packet the client sends to the server to tell him what he requests it to load in.
 *  Server may answer with an UNREGISTER_CHUNK_... packet if we requested a chunk that is too far away for us to be allowed to request it */
public class PacketWorldUser extends PacketWorld {

	public static PacketWorldUser registerChunkPacket(World world, int chunkX, int chunkY, int chunkZ) {
		return new PacketWorldUser(world, Type.REGISTER_CHUNK, chunkX, chunkY, chunkZ);
	}
	
	public static PacketWorldUser unregisterChunkPacket(World world, int chunkX, int chunkY, int chunkZ) {
		return new PacketWorldUser(world, Type.UNREGISTER_CHUNK, chunkX, chunkY, chunkZ);
	}
	
	public static PacketWorldUser registerSummary(World world, int regionX, int regionY) {
		return new PacketWorldUser(world, Type.REGISTER_SUMMARY, regionX, 0, regionY);
	}
	
	public static PacketWorldUser unregisterSummary(World world, int regionX, int regionY) {
		return new PacketWorldUser(world, Type.UNREGISTER_SUMMARY, regionX, 0, regionY);
	}
	
	protected Type type;
	protected int x,y,z;

	public enum Type {
		REGISTER_CHUNK,
		UNREGISTER_CHUNK,
		REGISTER_SUMMARY,
		UNREGISTER_SUMMARY
	}
	
	public PacketWorldUser(World world) {
		super(world);
	}
	
	private PacketWorldUser(World world, Type type, int x, int y, int z) {
		super(world);
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context) throws IOException {
		out.writeByte(type.ordinal());
		if(type == Type.REGISTER_SUMMARY || type == Type.UNREGISTER_SUMMARY) {
			out.writeInt(x);
			out.writeInt(z);
		} else {
			out.writeInt(x);
			out.writeInt(y);
			out.writeInt(z);
		}
	}

	@Override
	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext processor) throws IOException {
		type = Type.values()[in.readByte()];
		
		if(type == Type.REGISTER_SUMMARY || type == Type.UNREGISTER_SUMMARY) {
			x = in.readInt();
			z = in.readInt();
		} else {
			x = in.readInt();
			y = in.readInt();
			z = in.readInt();
		}
	}

	public Type getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
}
