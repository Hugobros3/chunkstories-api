//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import xyz.chunkstories.api.client.LocalPlayer
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.traits.TraitCollidable
import xyz.chunkstories.api.entity.traits.generic.TraitSerializableBoolean

/** Keeps track of the flying flag and performs the flying movement logic  */
class TraitFlyingMode(entity: Entity) : TraitSerializableBoolean(entity) {

    var flySpeed = 0.125f
    var isNoclip = true

    fun tick(controller: LocalPlayer) {
        if (!controller.hasFocus())
            return

        // Flying resets the entity velocity, if it has one
        entity.traits[TraitVelocity::class]?.setVelocity(0.0, 0.0, 0.0)

        val entityRotation = entity.traits[TraitRotation::class.java] ?: return
        // you must be able to rotate to fly

        val entityCollisions = entity.traits[TraitCollidable::class.java]
        val ignoreCollisions = (entityCollisions == null) or this.isNoclip

        var cameraSpeed = flySpeed
        if (controller.inputsManager.getInputByName("flyReallyFast").isPressed)
            cameraSpeed *= 8 * 5f
        else if (controller.inputsManager.getInputByName("flyFast").isPressed)
            cameraSpeed *= 8f

        if (controller.inputsManager.getInputByName("back").isPressed) {
            val horizRotRad = ((entityRotation.horizontalRotation + 180f) / 180f * Math.PI).toFloat()
            val vertRotRad = (-entityRotation.verticalRotation / 180f * Math.PI).toFloat()
            if (ignoreCollisions)
                entity.traitLocation.move(Math.sin(horizRotRad.toDouble()) * cameraSpeed.toDouble() * Math.cos(vertRotRad.toDouble()), Math.sin(vertRotRad.toDouble()) * cameraSpeed,
                        Math.cos(horizRotRad.toDouble()) * cameraSpeed.toDouble() * Math.cos(vertRotRad.toDouble()))
            else
                entityCollisions!!.moveWithCollisionRestrain(Math.sin(horizRotRad.toDouble()) * cameraSpeed.toDouble() * Math.cos(vertRotRad.toDouble()), Math.sin(vertRotRad.toDouble()) * cameraSpeed,
                        Math.cos(horizRotRad.toDouble()) * cameraSpeed.toDouble() * Math.cos(vertRotRad.toDouble()))
        }
        if (controller.inputsManager.getInputByName("forward").isPressed) {
            val horizRotRad = (entityRotation.horizontalRotation / 180f * Math.PI).toFloat()
            val vertRotRad = (entityRotation.verticalRotation / 180f * Math.PI).toFloat()
            if (ignoreCollisions)
                entity.traitLocation.move(Math.sin(horizRotRad.toDouble()) * cameraSpeed.toDouble() * Math.cos(vertRotRad.toDouble()), Math.sin(vertRotRad.toDouble()) * cameraSpeed,
                        Math.cos(horizRotRad.toDouble()) * cameraSpeed.toDouble() * Math.cos(vertRotRad.toDouble()))
            else
                entityCollisions!!.moveWithCollisionRestrain(Math.sin(horizRotRad.toDouble()) * cameraSpeed.toDouble() * Math.cos(vertRotRad.toDouble()), Math.sin(vertRotRad.toDouble()) * cameraSpeed,
                        Math.cos(horizRotRad.toDouble()) * cameraSpeed.toDouble() * Math.cos(vertRotRad.toDouble()))
        }
        if (controller.inputsManager.getInputByName("right").isPressed) {
            val horizRot = ((entityRotation.horizontalRotation + 90) / 180f * Math.PI).toFloat()
            if (ignoreCollisions)
                entity.traitLocation.move(-Math.sin(horizRot.toDouble()) * cameraSpeed, 0.0, -Math.cos(horizRot.toDouble()) * cameraSpeed)
            else
                entityCollisions!!.moveWithCollisionRestrain(-Math.sin(horizRot.toDouble()) * cameraSpeed, 0.0, -Math.cos(horizRot.toDouble()) * cameraSpeed)
        }
        if (controller.inputsManager.getInputByName("left").isPressed) {
            val horizRot = ((entityRotation.horizontalRotation - 90) / 180f * Math.PI).toFloat()
            if (ignoreCollisions)
                entity.traitLocation.move(-Math.sin(horizRot.toDouble()) * cameraSpeed, 0.0, -Math.cos(horizRot.toDouble()) * cameraSpeed)
            else
                entityCollisions!!.moveWithCollisionRestrain(-Math.sin(horizRot.toDouble()) * cameraSpeed, 0.0, -Math.cos(horizRot.toDouble()) * cameraSpeed)
        }
    }
}
