//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.content

import xyz.chunkstories.api.block.BlockID
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.EntityDefinition
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.ItemDefinition
import xyz.chunkstories.api.block.BlockType

/** This interface describes the world-specific mapping from named content definitions to integer IDs.  */
interface ContentTranslator {
    /** Returns a Collection containing the internalNames of the mods that were used with this world.  */
    val requiredMods: Collection<String>

    val content: Content

    /** Return the assignated ID for this declaration or -1 if it isn't a part of the current content  */
    fun getIdForVoxel(voxel: BlockType): BlockID
    /** Return the Voxel associated with that ID or null if the ID was outside of bounds  */
    fun getVoxelForId(id: BlockID): BlockType?
    val BlockType.assignedId: BlockID
        get() = getIdForVoxel(this)

    /** Return the assignated ID for this declaration or -1 if it isn't a part of
     * the current content  */
    fun getIdForItem(definition: ItemDefinition): Int

    /** Shortcut to getIdForItem(item.getDeclaration())  */
    fun getIdForItem(item: Item?): Int

    /** Return the ItemDefinition associated with that ID or null if the ID was
     * outside of bounds  */
    fun getItemForId(id: Int): ItemDefinition?

    /** Return the assignated ID for this declaration or -1 if it isn't a part of
     * the current content  */
    fun getIdForEntity(definition: EntityDefinition): Int

    /** Shortcut to getIdForItem(entity.getDeclaration()) */
    fun getIdForEntity(entity: Entity): Int

    /** Return the EntityDefinition associated with that ID or null if the ID was
     * outside of bounds */
    fun getEntityForId(id: Int): EntityDefinition?
}
