//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.world.WorldMaster;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

public class EntityRotation extends EntityComponent {
	private float rotationHorizontal = 0f;
	private float rotationVertical = 0f;

	private Vector2f rotationImpulse = new Vector2f();

	public EntityRotation(Entity entity) {
		super(entity);
	}

	public float getHorizontalRotation() {
		return rotationHorizontal;
	}

	public float getVerticalRotation() {
		return rotationVertical;
	}

	/**
	 * @return A vector3d for the direction
	 */
	public Vector3dc getDirectionLookingAt() {
		Vector3d direction = new Vector3d();

		double a = ((-getHorizontalRotation()) / 360f * 2 * Math.PI);
		double b = ((getVerticalRotation()) / 360f * 2 * Math.PI);
		direction.x = (-Math.sin(a) * Math.cos(b));
		direction.y = (-Math.sin(b));
		direction.z = (-Math.cos(a) * Math.cos(b));

		return direction.normalize();
	}

	public void setRotation(double horizontalAngle, double verticalAngle) {
		this.rotationHorizontal = (float) (360 + horizontalAngle) % 360;
		this.rotationVertical = (float) verticalAngle;

		if (rotationVertical > 90)
			rotationVertical = 90;
		if (rotationVertical < -90)
			rotationVertical = -90;

		this.pushComponentEveryone();
	}

	public void addRotation(double d, double e) {
		setRotation(rotationHorizontal + d, rotationVertical + e);
	}

	@Override
	protected void push(StreamTarget destinator, DataOutputStream dos) throws IOException {
		dos.writeFloat(rotationHorizontal);
		dos.writeFloat(rotationVertical);
	}

	@Override
	protected void pull(StreamSource from, DataInputStream dis) throws IOException {
		rotationHorizontal = dis.readFloat();
		rotationVertical = dis.readFloat();

		// Position updates received by the server should be told to everyone but the
		// controller
		if (entity.getWorld() instanceof WorldMaster)
			this.pushComponentEveryoneButController();
	}

	/**
	 * Sends the view flying about
	 */
	public void applyInpulse(double inpulseHorizontal, double inpulseVertical) {
		rotationImpulse.add(new Vector2f((float) inpulseHorizontal, (float) inpulseVertical));
	}

	/**
	 * Reduces the acceleration and returns it
	 */
	public Vector2f tickInpulse() {
		rotationImpulse.mul(0.50f);
		if (rotationImpulse.length() < 0.05)
			rotationImpulse.set(0.0f, 0.0f);
		return rotationImpulse;
	}

}
