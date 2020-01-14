//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.animation

import org.joml.Matrix4fc

/** Describes a full animation, loaded from Content.AnimationsLibrary  */
interface Animation : Animator {
    /** How is that bone offset to the center of the model  */
    fun getOffsetMatrix(boneName: String): Matrix4fc

    fun getBone(boneName: String): SkeletonBone?

    interface SkeletonBone {
        val name: String

        val parent: SkeletonBone?

        /** The transformation matrix for this bone alone  */
        fun getTransformationMatrix(animationTime: Double): Matrix4fc
    }
}
