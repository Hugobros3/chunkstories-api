//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.block
/*
import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.Definition
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asString
import xyz.chunkstories.api.item.ItemDefinition
import xyz.chunkstories.api.util.kotlin.initOnce
import java.lang.reflect.Constructor

class BlockDefinition(val store: Content.Voxels, name: String, properties: Json.Dict) : Definition(name, properties) {
    /** When added to the game content, either by being loaded explicitly or programatically, will be set to an integer
     * value between 1 and 65535. Attempting to manually override/set this identifier yourself will result in a house fire. */
    var assignedId : Int by initOnce()

    /** Shorthand for Java accesses */
    fun store() = store

    val clazz: Class<BlockType>
    private val constructor: Constructor<BlockType>

    val voxel: BlockType
    val variants: List<ItemDefinition>

    init {
        clazz = this["class"].asString?.let {
            store.parent.modsManager.getClassByName(it)?.let {
                if(BlockType::class.java.isAssignableFrom(it))
                    it as Class<BlockType>
                else
                    throw Exception("The custom class has to extend the Voxel class !")
            }
        }  ?: BlockType::class.java

        constructor = try {
            clazz.getConstructor(BlockDefinition::class.java)
        } catch (e: NoSuchMethodException) {
            throw Exception("Your custom class, $clazz, lacks the correct Voxel(VoxelDefinition) constructor.")
        }

        voxel = create()
        variants = voxel.enumerateVariants_(store.parent.items)
    }

    private fun <V : BlockType> create() = constructor.newInstance(this) as V

    override fun toString(): String {
        return "VoxelDefinition($name, $properties)"
    }
}*/