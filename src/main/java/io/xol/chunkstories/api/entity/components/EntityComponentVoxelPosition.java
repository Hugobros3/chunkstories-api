package io.xol.chunkstories.api.entity.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.serialization.StreamSource;
import io.xol.chunkstories.api.serialization.StreamTarget;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkVoxelContext;

public class EntityComponentVoxelPosition extends EntityComponent {

	private Chunk chunk;
	private int x, y, z;
	
	public EntityComponentVoxelPosition(Entity entity, ChunkVoxelContext context) {
		super(entity);
		
		this.chunk = context.getChunk();
		this.x = context.getX();
		this.y = context.getY();
		this.z = context.getZ();
	}

	public Chunk getChunk() {
		return chunk;
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

	@Override
	protected void push(StreamTarget destinator, DataOutputStream dos) throws IOException {
		
	}

	@Override
	protected void pull(StreamSource from, DataInputStream dis) throws IOException {
		
	}
	
}
