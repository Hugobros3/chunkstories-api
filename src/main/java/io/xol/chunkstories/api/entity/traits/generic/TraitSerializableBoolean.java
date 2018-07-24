//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.traits.generic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.traits.serializable.TraitSerializable;
import io.xol.chunkstories.api.util.Generalized;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

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
