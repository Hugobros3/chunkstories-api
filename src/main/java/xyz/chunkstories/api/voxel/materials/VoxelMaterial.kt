//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.voxel.materials

import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.Definition

/** A Voxel Material contains more generic stats about a class of voxel, for example the sounds they make
 * when interacted with, or how they react to being mined with certain tools. */
class VoxelMaterial(val store: Content.Voxels.VoxelMaterials, name: String, properties: Map<String, String>) : Definition(name, properties) {

    /** Shorthand for Java accesses */
    fun store() = store

    override fun toString(): String {
        return "VoxelMaterial($name, $allProperties)"
    }
}