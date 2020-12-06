//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.animation

import org.joml.Matrix4fc

/** Used for renderable meshes with a skeletal structure, only provides details
 * relevant to displaying the model, rather than intrinsic details on the
 * animation structure.  */
interface Animator {
    /** Used to draw the debug bone armature  */
    fun getBoneHierarchyTransformationMatrix(nameOfEndBone: String, animationTime: Float): Matrix4fc

    /** Used to draw deformed mesh parts  */
    fun getBoneHierarchyTransformationMatrixWithOffset(nameOfEndBone: String, animationTime: Float): Matrix4fc
}
