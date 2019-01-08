//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.generic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.entity.traits.serializable.TraitSerializable;
import xyz.chunkstories.api.util.Generalized;
import xyz.chunkstories.api.world.serialization.StreamSource;
import xyz.chunkstories.api.world.serialization.StreamTarget;

@Generalized
public abstract class TraitSerializableBoolean extends TraitSerializable {
	private boolean value = false;

	public boolean get() {
		return value;
	}

	public void set(boolean newValue) {
		if (this.value != newValue) {
			this.value = newValue;
			this.pushComponentEveryone();
		}
	}

	public TraitSerializableBoolean(Entity entity) {
		super(entity);
	}

	@Override
	protected void push(StreamTarget destinator, DataOutputStream dos) throws IOException {
		dos.writeBoolean(value);
	}

	@Override
	protected void pull(StreamSource from, DataInputStream dis) throws IOException {
		value = dis.readBoolean();
	}

}
