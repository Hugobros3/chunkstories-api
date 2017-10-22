package io.xol.chunkstories.api.voxel.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.serialization.StreamSource;
import io.xol.chunkstories.api.serialization.StreamTarget;

public abstract class VoxelComponent {
	private final VoxelComponents holder;
	
	public VoxelComponent(VoxelComponents holder) {
		this.holder = holder;
	}
	
	public final VoxelComponents holder() {
		return holder;
	}
	
	/** Pushes the component to every client subscribed to the chunk owning this voxel */
	public void pushComponentEveryone() {
		throw new UnsupportedOperationException("pushComponentEveryone()");
	}
	
	/** Pushes the component to a specific player */
	public void pushComponent(Player player) {
		throw new UnsupportedOperationException("pushComponent(player)");
	}
	
	public abstract void push(StreamTarget destinator, DataOutputStream dos) throws IOException;

	public abstract void pull(StreamSource from, DataInputStream dis) throws IOException;
}
