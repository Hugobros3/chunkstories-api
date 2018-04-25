//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.joml.Vector3d;
import org.joml.Vector3dc;

import io.xol.chunkstories.api.entity.Controller;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.net.packets.PacketVelocityDelta;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

public class EntityVelocity extends EntityComponent {
	public EntityVelocity(Entity entity) {
		super(entity);
	}

	private Vector3d velocity = new Vector3d();

	public Vector3d getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3dc velocity) {
		this.velocity.x = (velocity.x());
		this.velocity.y = (velocity.y());
		this.velocity.z = (velocity.z());

		this.pushComponentEveryone();
	}

	public void setVelocity(double x, double y, double z) {
		this.velocity.x = (x);
		this.velocity.y = (y);
		this.velocity.z = (z);

		this.pushComponentEveryone();
	}

	public void setVelocityX(double x) {
		this.velocity.x = (x);

		this.pushComponentEveryone();
	}

	public void setVelocityY(double y) {
		this.velocity.y = (y);

		this.pushComponentEveryone();
	}

	public void setVelocityZ(double z) {
		this.velocity.z = (z);

		this.pushComponentEveryone();
	}

	public void addVelocity(Vector3dc delta) {
		this.velocity.add(delta);

		this.pushComponentEveryoneButController();
		
		// Notify the controller in a special way so they don't experience lag
		// due to being set back at a previous velocity
		entity.components.with(EntityController.class, ecc -> {
			Controller controller = ecc.getController();
			if (controller != null) {
				PacketVelocityDelta packet = new PacketVelocityDelta(entity.getWorld(), delta);
				controller.pushPacket(packet);
			}
		});
	}

	public void addVelocity(double x, double y, double z) {
		this.addVelocity(new Vector3d(x, y, z));
	}

	@Override
	protected void push(StreamTarget destinator, DataOutputStream dos) throws IOException {
		dos.writeDouble(velocity.x());
		dos.writeDouble(velocity.y());
		dos.writeDouble(velocity.z());
	}

	@Override
	protected void pull(StreamSource from, DataInputStream dis) throws IOException {
		velocity.x = (dis.readDouble());
		velocity.y = (dis.readDouble());
		velocity.z = (dis.readDouble());

		this.pushComponentEveryoneButController();
	}
}
