//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.components;

import java.util.Iterator;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.net.Packet;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

/**
 * A subscriber wants to be kept up-to-date with the latest changes in his environment
 */
public interface Subscriber extends StreamTarget
{
	/**
	 * @return An unique ID to discriminate it against all others
	 *         -1 is reserved for server
	 *         all positive ids are hashmaps of the client username
	 */
	public long getUUID();
	
	/**
	 * @return All Entities it is subscribed to
	 */
	public Iterator<Entity> getSubscribedToList();
	
	public boolean subscribe(Entity entity);
	
	public boolean unsubscribe(Entity entity);
	
	public void unsubscribeAll();

	public void pushPacket(Packet packet);

	//public boolean isSubscribedTo(Entity entity);
}
