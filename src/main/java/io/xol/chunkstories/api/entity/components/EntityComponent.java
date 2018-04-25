//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

import io.xol.chunkstories.api.entity.Controller;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.net.packets.PacketEntity;
import io.xol.chunkstories.api.world.serialization.OfflineSerializedData;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

@SerializedName(name="carotte")
public abstract class EntityComponent {
	public final Entity entity;
	private int id;
	
	/** Reflects the name declared in the @SerializedName annotation, or the the top level class name if none is declared */
	public final String name;
	
	/*private TouchedCallback[] callbacks = new TouchedCallback[0];*/

	public EntityComponent(Entity entity) {
		this.entity = entity;
		entity.components.registerComponent(this);
		
		SerializedName[] a = this.getClass().getAnnotationsByType(SerializedName.class);
		if(a.length > 0)
			name = a[0].name();
		else
			name = this.getClass().getName();
	}

	/** Push will tell all subscribers of the entity about a change of this component
	 * only */
	public void pushComponentEveryone() {
		entity.subscribers.all().forEach(s -> this.pushComponent(s));
	}

	/** Push the component to the controller, if such one exists */
	public void pushComponentController() {
		this.entity.components.with(EntityController.class, e -> {
			if(e.controller != null)
				pushComponent(e.controller);
		});
	}

	/**
	 * Push the component to everyone but the controller, if such one exists
	 */
	public void pushComponentEveryoneButController() {
		Iterator<Subscriber> iterator = entity.subscribers.all().iterator();

		Controller controller = entity.components.tryWith(EntityController.class, ecc -> ecc.getController());
		
		while (iterator.hasNext()) {
			Subscriber subscriber = iterator.next();

			// Don't push the update to the controller.
			if (controller != null && subscriber.equals(controller))
				continue;

			this.pushComponent(subscriber);
		}
	}

	/** Push the component to someone in particular */
	public void pushComponent(Subscriber subscriber) {
		//callbacks();
		// You may check that subscriber has subscribed to said entity ?
		// A: nope because we send the EntityExistence (hint: false) component to [just]
		// unsubscribed guys so it wouldn't work
		
		//TODO rework that assumption now

		PacketEntity packet = new PacketEntity(entity, this);
		// this.pushComponentInStream(subscriber, packet.getSynchPacketOutputStream());
		subscriber.pushPacket(packet);
	}

	public void pushComponentInStream(StreamTarget to, DataOutputStream dos) throws IOException {
		// Offline saves will have version discrepancies, so we use a symbolic name instead
		if (to instanceof OfflineSerializedData) {
			dos.writeInt(-1);
			dos.writeUTF(this.name);
		} else {
			dos.writeInt(this.id);
		}
		
		// Push actual component data
		push(to, dos);
	}
	
	public final void tryPull(StreamSource from, DataInputStream dis) throws IOException {
		pull(from, dis);
	}

	protected abstract void push(StreamTarget destinator, DataOutputStream dos) throws IOException;

	protected abstract void pull(StreamSource from, DataInputStream dis) throws IOException;
	
	public int id() {
		return id;
	}
	
	/*private void callbacks() {
		for(TouchedCallback c : callbacks)
			c.touched();
	}
	
	public void registerCallback(TouchedCallback lol) {
		//Thread-safe for writes because it's only ever touched by the entity constructor
		//(and that's not multithreaded!)
		TouchedCallback[] callbacks = new TouchedCallback[this.callbacks.length + 1];
		for(int i = 0; i < this.callbacks.length; i++)
			callbacks[i] = this.callbacks[i];
		callbacks[callbacks.length - 1] = lol;
		this.callbacks = callbacks; // thread-safe for reads because it's atomic
	}
	
	public interface TouchedCallback {
		public void touched();
	}*/
}
