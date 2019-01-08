//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

import xyz.chunkstories.api.entity.Controller;
import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.entity.Subscriber;
import xyz.chunkstories.api.entity.traits.Trait;
import xyz.chunkstories.api.net.packets.PacketEntity;
import xyz.chunkstories.api.util.Generalized;
import xyz.chunkstories.api.util.SerializedName;
import xyz.chunkstories.api.world.serialization.OfflineSerializedData;
import xyz.chunkstories.api.world.serialization.StreamSource;
import xyz.chunkstories.api.world.serialization.StreamTarget;

@Generalized
public abstract class TraitSerializable extends Trait {
	/** Reflects the name declared in the @SerializedName annotation, or the the top
	 * level class name if none is declared */
	public final String name;

	public TraitSerializable(Entity entity) {
		super(entity);

		SerializedName[] a = this.getClass().getAnnotationsByType(SerializedName.class);
		if (a.length > 0)
			name = a[0].name();
		else
			name = this.getClass().getName();
	}

	/** Push will tell all subscribers of the entity about a change of this
	 * component only */
	public void pushComponentEveryone() {
		entity.subscribers.forEach(this::pushComponent);
	}

	/** Push the component to the controller, if such one exists */
	public void pushComponentController() {
		this.entity.traits.with(TraitControllable.class, e -> {
			Controller controller = e.getController();
			if (controller != null)
				pushComponent(controller);
		});
	}

	/** Push the component to everyone but the controller, if such one exists */
	public void pushComponentEveryoneButController() {
		Iterator<Subscriber> iterator = entity.subscribers.iterator();

		Controller controller = entity.traits.tryWith(TraitControllable.class, TraitControllable::getController);

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
		// You may check that subscriber has subscribed to said entity ?
		// A: nope because we send the EntityExistence (hint: false) component to [just]
		// unsubscribed guys so it wouldn't work

		// TODO rework that assumption now

		PacketEntity packet = new PacketEntity(entity, this);
		// this.pushComponentInStream(subscriber, packet.getSynchPacketOutputStream());
		subscriber.pushPacket(packet);
	}

	public void pushComponentInStream(StreamTarget to, DataOutputStream dos) throws IOException {
		// Offline saves will have version discrepancies, so we use a symbolic name
		// instead
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
}
