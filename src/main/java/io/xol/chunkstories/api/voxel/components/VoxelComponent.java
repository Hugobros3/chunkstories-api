package io.xol.chunkstories.api.voxel.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.net.packets.PacketVoxelUpdate;
import io.xol.chunkstories.api.server.RemotePlayer;
import io.xol.chunkstories.api.world.cell.CellComponents;
import io.xol.chunkstories.api.world.chunk.WorldUser;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

public abstract class VoxelComponent {
	private final CellComponents holder;
	
	public VoxelComponent(CellComponents holder) {
		this.holder = holder;
	}
	
	public final CellComponents holder() {
		return holder;
	}
	
	public final String name() {
		return holder.name(this);
	}
	
	/** Pushes the component to every client subscribed to the chunk owning this voxel */
	public void pushComponentEveryone() {
		for(WorldUser user : holder.users()) {
			if(user instanceof RemotePlayer) {
				pushComponent((RemotePlayer)user);
			}
		}
	}
	
	/** Pushes the component to a specific player */
	public void pushComponent(RemotePlayer player) {
		PacketVoxelUpdate packet = new PacketVoxelUpdate(holder.cell(), this);
		player.pushPacket(packet);
	}
	
	public abstract void push(StreamTarget destinator, DataOutputStream dos) throws IOException;

	public abstract void pull(StreamSource from, DataInputStream dis) throws IOException;
}
