//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.voxel.materials

import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.Definition
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asDouble
import xyz.chunkstories.api.content.json.asString

/** A Voxel Material contains more generic stats about a class of voxel, for example the sounds they make
 * when interacted with, or how they react to being mined with certain tools. */
class VoxelMaterial(val store: Content.Voxels.VoxelMaterials, name: String, properties: Json.Dict) : Definition(name, properties) {

    val walkingSounds: String = this["walkingSounds"].asString ?: "error"
    val jumpingSounds: String = this["jumpingSounds"].asString ?: walkingSounds
    val runningSounds: String = this["runningSounds"].asString ?: walkingSounds
    val landingSounds: String = this["landingSounds"].asString ?: walkingSounds
    val miningSounds: String = this["miningSounds"].asString ?: runningSounds
    val minedSounds: String = this["minedSounds"].asString ?: landingSounds

    val mineUsing: String = this["mineUsing"].asString ?: "nothing_in_particular"
    val miningDifficulty: Double = this["miningDifficulty"].asDouble ?: 1.0

    /** Shorthand for Java accesses */
    fun store() = store

    override fun toString(): String {
        return "VoxelMaterial($name, $properties)"
    }
}