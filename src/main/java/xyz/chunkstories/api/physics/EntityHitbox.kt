//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.physics

import org.joml.Matrix4f
import org.joml.Vector3d
import org.joml.Vector3dc
import org.joml.Vector3f
import org.joml.Vector4f

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.traits.TraitAnimated

class EntityHitbox(internal val entity: Entity, internal val box: Box, val name: String) {
    internal val animationTrait: TraitAnimated?

    init {
        this.animationTrait = entity.traits[TraitAnimated::class.java]
    }

    /** Tricky maths; transforms the inbound ray so the hitbox would be at 0.0.0 and
     * axis-aligned  */
    fun lineIntersection(lineStart: Vector3dc, lineDirection: Vector3dc): Vector3dc? {
        val fromAABBToWorld = Matrix4f()
        if (this.animationTrait != null)
            fromAABBToWorld.set(animationTrait.animatedSkeleton.getBoneHierarchyTransformationMatrix(name, (System.currentTimeMillis() % 1000000).toDouble()))

        val worldPositionTransformation = Matrix4f()

        val entityLoc = entity.location

        val pos = Vector3f(entityLoc.x.toFloat(), entityLoc.y.toFloat(), entityLoc.z.toFloat())
        worldPositionTransformation.translate(pos)

        // Creates from AABB space to worldspace
        worldPositionTransformation.mul(fromAABBToWorld, fromAABBToWorld)

        // Invert it.
        val fromWorldToAABB = Matrix4f()
        fromAABBToWorld.invert(fromWorldToAABB)

        // Transform line start into AABB space
        val lineStart4 = Vector4f(lineStart.x().toFloat(), lineStart.y().toFloat(), lineStart.z().toFloat(), 1.0f)
        val lineDirection4 = Vector4f(lineDirection.x().toFloat(), lineDirection.y().toFloat(), lineDirection.z().toFloat(), 0.0f)

        fromWorldToAABB.transform(lineStart4)
        fromWorldToAABB.transform(lineDirection4)

        val lineStartTransformed = Vector3d(lineStart4.x().toDouble(), lineStart4.y().toDouble(), lineStart4.z().toDouble())
        val lineDirectionTransformed = Vector3d(lineDirection4.x().toDouble(), lineDirection4.y().toDouble(), lineDirection4.z().toDouble())

        // Actual computation
        val hitPoint = box.lineIntersection(lineStartTransformed, lineDirectionTransformed) ?: return null

        // Transform hitPoint back into world
        val hitPoint4 = Vector4f(hitPoint.x().toFloat(), hitPoint.y().toFloat(), hitPoint.z().toFloat(), 1.0f)
        fromAABBToWorld.transform(hitPoint4)

        hitPoint4.mul(1.0f / hitPoint4.w())

        return Vector3d(hitPoint4.x().toDouble(), hitPoint4.y().toDouble(), hitPoint4.z().toDouble())
    }

    override fun toString(): String {
        return "EntityHitbox(entity=$entity, box=$box, name='$name')"
    }
}