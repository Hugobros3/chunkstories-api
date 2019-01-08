//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.world.WorldMaster;
import xyz.chunkstories.api.world.serialization.StreamSource;
import xyz.chunkstories.api.world.serialization.StreamTarget;

public class TraitRotation extends TraitSerializable {
	private float rotationHorizontal = 0f;
	private float rotationVertical = 0f;

	private Vector2f rotationImpulse = new Vector2f();

	public TraitRotation(Entity entity) {
		super(entity);
	}

	public float getHorizontalRotation() {
		return rotationHorizontal;
	}

	public float getVerticalRotation() {
		return rotationVertical;
	}

	/** @return A vector3d for the direction */
	public Vector3dc getDirectionLookingAt() {
		Vector3d direction = new Vector3d();

		double horizontalRotationRad = ((getHorizontalRotation()) / 360f * 2 * Math.PI);
		double verticalRotationRad = ((getVerticalRotation()) / 360f * 2 * Math.PI);
		direction.x = (Math.sin(horizontalRotationRad) * Math.cos(verticalRotationRad));
		direction.y = (Math.sin(verticalRotationRad));
		direction.z = (Math.cos(horizontalRotationRad) * Math.cos(verticalRotationRad));

		return direction.normalize();
	}

	public Vector3dc getUpDirection() {
		Vector3d direction = new Vector3d(0.0, 1.0, 0.0);

		double horizontalRotationRad = ((getHorizontalRotation()) / 360f * 2 * Math.PI);
		double verticalRotationRad = ((getVerticalRotation() + 90f) / 360f * 2 * Math.PI);
		direction.x = (Math.sin(horizontalRotationRad) * Math.cos(verticalRotationRad));
		direction.y = (Math.sin(verticalRotationRad));
		direction.z = (Math.cos(horizontalRotationRad) * Math.cos(verticalRotationRad));

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

	/** Sends the view flying about */
	public void applyInpulse(double inpulseHorizontal, double inpulseVertical) {
		rotationImpulse.add(new Vector2f((float) inpulseHorizontal, (float) inpulseVertical));
	}

	/** Reduces the acceleration and returns it */
	public Vector2f tickInpulse() {
		rotationImpulse.mul(0.50f);
		if (rotationImpulse.length() < 0.05)
			rotationImpulse.set(0.0f, 0.0f);
		return rotationImpulse;
	}

}