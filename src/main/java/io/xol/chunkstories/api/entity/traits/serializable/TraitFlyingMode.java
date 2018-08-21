//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.traits.serializable;

import io.xol.chunkstories.api.client.LocalPlayer;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.traits.TraitCollidable;
import io.xol.chunkstories.api.entity.traits.generic.TraitSerializableBoolean;

/** Keeps track of the flying flag and performs the flying movement logic */
public class TraitFlyingMode extends TraitSerializableBoolean {
	public TraitFlyingMode(Entity entity) {
		super(entity);
	}

	public float flySpeed = 0.125f;
	public boolean noclip = true;

	public void tick(LocalPlayer controller) {
		if (!controller.hasFocus())
			return;

		// Flying resets the entity velocity, if it has one
		entity.traits.with(TraitVelocity.class, ev -> ev.setVelocity(0, 0, 0));

		TraitRotation entityRotation = entity.traits.get(TraitRotation.class);
		if (entityRotation == null)
			return; // you must be able to rotate to fly

		TraitCollidable entityCollisions = entity.traits.get(TraitCollidable.class);
		boolean noclip = entityCollisions == null | this.noclip; // not having collision on an entity means it's always
																	// in noclip mode

		float camspeed = flySpeed;
		if (controller.getInputsManager().getInputByName("flyReallyFast").isPressed())
			camspeed *= 8 * 5f;
		else if (controller.getInputsManager().getInputByName("flyFast").isPressed())
			camspeed *= 8f;

		if (controller.getInputsManager().getInputByName("back").isPressed()) {
			float a = (float) ((-entityRotation.getHorizontalRotation()) / 180f * Math.PI);
			float b = (float) ((entityRotation.getVerticalRotation()) / 180f * Math.PI);
			if (noclip)
				entity.entityLocation.move(Math.sin(a) * camspeed * Math.cos(b), Math.sin(b) * camspeed, Math.cos(a) * camspeed * Math.cos(b));
			else
				entityCollisions.moveWithCollisionRestrain(Math.sin(a) * camspeed * Math.cos(b), Math.sin(b) * camspeed, Math.cos(a) * camspeed * Math.cos(b));
		}
		if (controller.getInputsManager().getInputByName("forward").isPressed()) {
			float a = (float) ((180 - entityRotation.getHorizontalRotation()) / 180f * Math.PI);
			float b = (float) ((-entityRotation.getVerticalRotation()) / 180f * Math.PI);
			if (noclip)
				entity.entityLocation.move(Math.sin(a) * camspeed * Math.cos(b), Math.sin(b) * camspeed, Math.cos(a) * camspeed * Math.cos(b));
			else
				entityCollisions.moveWithCollisionRestrain(Math.sin(a) * camspeed * Math.cos(b), Math.sin(b) * camspeed, Math.cos(a) * camspeed * Math.cos(b));
		}
		if (controller.getInputsManager().getInputByName("right").isPressed()) {
			float a = (float) ((-entityRotation.getHorizontalRotation() - 90) / 180f * Math.PI);
			if (noclip)
				entity.entityLocation.move(-Math.sin(a) * camspeed, 0, -Math.cos(a) * camspeed);
			else
				entityCollisions.moveWithCollisionRestrain(-Math.sin(a) * camspeed, 0, -Math.cos(a) * camspeed);
		}
		if (controller.getInputsManager().getInputByName("left").isPressed()) {
			float a = (float) ((-entityRotation.getHorizontalRotation() + 90) / 180f * Math.PI);
			if (noclip)
				entity.entityLocation.move(-Math.sin(a) * camspeed, 0, -Math.cos(a) * camspeed);
			else
				entityCollisions.moveWithCollisionRestrain(-Math.sin(a) * camspeed, 0, -Math.cos(a) * camspeed);
		}
	}
}
