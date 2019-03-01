//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable;

import xyz.chunkstories.api.client.LocalPlayer;
import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.entity.traits.TraitCollidable;
import xyz.chunkstories.api.entity.traits.generic.TraitSerializableBoolean;

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
		getEntity().traits.with(TraitVelocity.class, ev -> ev.setVelocity(0, 0, 0));

		TraitRotation entityRotation = getEntity().traits.get(TraitRotation.class);
		if (entityRotation == null)
			return; // you must be able to rotate to fly

		TraitCollidable entityCollisions = getEntity().traits.get(TraitCollidable.class);
		boolean ignoreCollisions = entityCollisions == null | this.noclip;

		float cameraSpeed = flySpeed;
		if (controller.getInputsManager().getInputByName("flyReallyFast").isPressed())
			cameraSpeed *= 8 * 5f;
		else if (controller.getInputsManager().getInputByName("flyFast").isPressed())
			cameraSpeed *= 8f;

		if (controller.getInputsManager().getInputByName("back").isPressed()) {
			float horizRotRad = (float) ((entityRotation.getHorizontalRotation() + 180f) / 180f * Math.PI);
			float vertRotRad = (float) ((-entityRotation.getVerticalRotation()) / 180f * Math.PI);
			if (ignoreCollisions)
				getEntity().traitLocation.move(Math.sin(horizRotRad) * cameraSpeed * Math.cos(vertRotRad), Math.sin(vertRotRad) * cameraSpeed,
						Math.cos(horizRotRad) * cameraSpeed * Math.cos(vertRotRad));
			else
				entityCollisions.moveWithCollisionRestrain(Math.sin(horizRotRad) * cameraSpeed * Math.cos(vertRotRad), Math.sin(vertRotRad) * cameraSpeed,
						Math.cos(horizRotRad) * cameraSpeed * Math.cos(vertRotRad));
		}
		if (controller.getInputsManager().getInputByName("forward").isPressed()) {
			float horizRotRad = (float) ((entityRotation.getHorizontalRotation()) / 180f * Math.PI);
			float vertRotRad = (float) ((entityRotation.getVerticalRotation()) / 180f * Math.PI);
			if (ignoreCollisions)
				getEntity().traitLocation.move(Math.sin(horizRotRad) * cameraSpeed * Math.cos(vertRotRad), Math.sin(vertRotRad) * cameraSpeed,
						Math.cos(horizRotRad) * cameraSpeed * Math.cos(vertRotRad));
			else
				entityCollisions.moveWithCollisionRestrain(Math.sin(horizRotRad) * cameraSpeed * Math.cos(vertRotRad), Math.sin(vertRotRad) * cameraSpeed,
						Math.cos(horizRotRad) * cameraSpeed * Math.cos(vertRotRad));
		}
		if (controller.getInputsManager().getInputByName("right").isPressed()) {
			float horizRot = (float) ((entityRotation.getHorizontalRotation() + 90) / 180f * Math.PI);
			if (ignoreCollisions)
				getEntity().traitLocation.move(-Math.sin(horizRot) * cameraSpeed, 0, -Math.cos(horizRot) * cameraSpeed);
			else
				entityCollisions.moveWithCollisionRestrain(-Math.sin(horizRot) * cameraSpeed, 0, -Math.cos(horizRot) * cameraSpeed);
		}
		if (controller.getInputsManager().getInputByName("left").isPressed()) {
			float horizRot = (float) ((entityRotation.getHorizontalRotation() - 90) / 180f * Math.PI);
			if (ignoreCollisions)
				getEntity().traitLocation.move(-Math.sin(horizRot) * cameraSpeed, 0, -Math.cos(horizRot) * cameraSpeed);
			else
				entityCollisions.moveWithCollisionRestrain(-Math.sin(horizRot) * cameraSpeed, 0, -Math.cos(horizRot) * cameraSpeed);
		}
	}
}
