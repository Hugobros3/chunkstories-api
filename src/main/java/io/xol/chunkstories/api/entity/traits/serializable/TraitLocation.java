//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.traits.serializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import org.joml.Vector3dc;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.events.entity.EntityTeleportEvent;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.WorldMaster;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

import javax.annotation.Nullable;

/** Holds the information about an entity whereabouts and a flag to mark it as
 * unspawned */
public class TraitLocation extends TraitSerializable {

	public TraitLocation(Entity entity, Location location) {
		super(entity);

		this.pos = location;
		this.world = location.getWorld();
	}

	private World world;
	private final Location pos;

	private final ReentrantLock lock = new ReentrantLock();

	@Nullable
	private Chunk chunk;

	private boolean spawned = false;
	private boolean removed = false;

	public void set(Location location) {
		if (location.world != this.world)
			throw new RuntimeException("Entities can't teleport between worlds directly.");

		set((Vector3dc) location);
	}

	public void set(Vector3dc position) {
		set(position.x(), position.y(), position.z());
	}

	public void set(double x, double y, double z) {
		EntityTeleportEvent event = new EntityTeleportEvent(entity, new Location(world, x, y, z));
		entity.world.getGameContext().getPluginManager().fireEvent(event);

		try {
			lock.lock();
			this.pos.x = (x);
			this.pos.y = (y);
			this.pos.z = (z);

			sanitize();
		} finally {
			lock.unlock();
		}

		// Push updates to everyone subscribed to this
		// In client mode it means that the controlled entity has the server subscribed
		// so it will update it's status to the server

		// In server/master mode any drastic location change is told to everyone, as
		// setLocation() is not called when the server receives updates
		// from the controller but only when an external event changes the location.
		this.pushComponentEveryone();
	}

	public void move(double mx, double my, double mz) {
		try {
			lock.lock();
			pos.x = (pos.x() + mx);
			pos.y = (pos.y() + my);
			pos.z = (pos.z() + mz);

			sanitize();
		} finally {
			lock.unlock();
		}

		this.pushComponentEveryone();
	}

	public void move(Vector3dc delta) {
		move(delta.x(), delta.y(), delta.z());
	}

	/** Copies the location and returns it. The actual location is never mutated
	 * outside of set(). */
	public Location get() {
		Location pos = this.pos;
		return new Location(pos.getWorld(), pos);
	}

	@Nullable
	public Chunk getChunk() {
		return chunk;
	}

	@Override
	public void push(StreamTarget to, DataOutputStream dos) throws IOException {
		dos.writeDouble(pos.x());
		dos.writeDouble(pos.y());
		dos.writeDouble(pos.z());
	}

	@Override
	public void pull(StreamSource from, DataInputStream dis) throws IOException {
		// pos = new Location(world, 0, 0, 0);

		double x = dis.readDouble();
		double y = dis.readDouble();
		double z = dis.readDouble();

		try {
			lock.lock();
			this.pos.x = x;
			this.pos.y = y;
			this.pos.z = z;

			sanitize();
		} finally {
			lock.unlock();
		}

		// Position updates received by the server should be told to everyone but the
		// controller
		if (world instanceof WorldMaster)
			pushComponentEveryoneButController();
	}

	/** Prevents entities from going outside the world area and updates the
	 * parentHolder reference */
	protected final boolean sanitize() {
		double worldSize = world.getWorldSize();

		pos.x = pos.x() % worldSize;
		pos.z = pos.z() % worldSize;

		// Loop arround the world
		if (pos.x() < 0)
			pos.x = pos.x() + worldSize;
		else if (pos.x() > worldSize)
			pos.x = pos.x() % worldSize;

		if (pos.z() < 0)
			pos.z = pos.z() + worldSize;
		else if (pos.z() > worldSize)
			pos.z = pos.z() % worldSize;

		if (pos.y < 0)
			pos.y = 0;

		if (pos.y > world.getMaxHeight())
			pos.y = world.getMaxHeight();

		// Get local chunk co-ordinate
		int chunkX = ((int) pos.x()) >> 5;
		int chunkY = ((int) pos.y()) >> 5;
		int chunkZ = ((int) pos.z()) >> 5;

		// Don't touch updates once the entity was removed
		if (removed /* !entity.exists() */)
			return false;

		// Entities not in the world should never be added to it
		if (!spawned)
			return false;

		if (chunk != null && chunk.getChunkX() == chunkX && chunk.getChunkY() == chunkY && chunk.getChunkZ() == chunkZ) {
			return false; // Nothing to do !
		} else {
			if (chunk != null)
				chunk.removeEntity(entity);

			chunk = world.getChunk(chunkX, chunkY, chunkZ);
			// When the region is loaded, add this entity to it.
			if (chunk != null)// && regionWithin.isDiskDataLoaded())
				chunk.addEntity(entity);

			return true;
		}
	}

	public void onRemoval() {
		try {
			lock.lock();

			if (chunk != null)
				chunk.removeEntity(entity);

			removed = true;
		} finally {
			lock.unlock();
		}

		this.pushComponentEveryone();

		// Tell anyone still subscribed to this entity to sod off
		entity.subscribers.all().forEach(subscriber -> {
			subscriber.unsubscribe(entity);
		});
	}

	public boolean wasRemoved() {
		return removed;
	}

	public void onSpawn() {
		try {
			lock.lock();
			spawned = true;

			sanitize();
		} finally {
			lock.unlock();
		}
	}

	public boolean hasSpawned() {
		return spawned;
	}
}
