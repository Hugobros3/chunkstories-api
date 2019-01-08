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

/** Generic class for not duplacting boring code everywhere Remember: you still
 * have to declare the actual components classes in .components files ! */
@Generalized
public class TraitSerializableFloat extends TraitSerializable {
	protected float value;

	public TraitSerializableFloat(Entity entity, float defaultValue) {
		super(entity);
		this.value = defaultValue;
	}

	public float getValue() {
		return value;
	}

	public boolean setValue(float newValue) {
		this.value = newValue;

		this.pushComponentEveryone();
		return true;
	}

	@Override
	protected void push(StreamTarget destinator, DataOutputStream dos) throws IOException {
		dos.writeFloat(value);
	}

	@Override
	protected void pull(StreamSource from, DataInputStream dis) throws IOException {
		value = dis.readFloat();
	}

}