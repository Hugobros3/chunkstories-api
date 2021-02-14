//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.block

import org.joml.Matrix4f
import org.joml.Vector3d
import org.joml.Vector4fc
import xyz.chunkstories.api.Location
import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.json.*
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.EntityDroppedItem
import xyz.chunkstories.api.entity.traits.serializable.TraitCreativeMode
import xyz.chunkstories.api.events.voxel.PlayerMineBlockEvent
import xyz.chunkstories.api.input.Input
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.ItemDefinition
import xyz.chunkstories.api.item.ItemBlock
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.graphics.MeshMaterial
import xyz.chunkstories.api.graphics.representation.Model
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.sound.Sound
import xyz.chunkstories.api.util.kotlin.initOnce
import xyz.chunkstories.api.world.MutableWorldCell
import xyz.chunkstories.api.world.cell.Cell
import xyz.chunkstories.api.world.cell.CellData
import xyz.chunkstories.api.world.cell.MutableCellData
import xyz.chunkstories.api.world.chunk.ChunkCell
import java.io.IOException

typealias BlockID = Int
typealias LightLevel = Int

@Suppress("LeakingThis")
open class BlockType(val name: String, val definition: Json.Dict, val content: Content) {
    var assignedId : BlockID by initOnce()
    open var solid = definition["solid"].asBoolean ?: true
    open var liquid = definition["liquid"].asBoolean ?: false
    /** Does this block completely hides adjacent ones (and can we just skip rendering those hidden faces) ? */
    open var opaque = definition["opaque"].asBoolean ?: true
    /** Assuming non-opacity, should adjacent copies of this block have the faces in between them rendered ? */
    open var selfOpaque = definition["selfOpaque"].asBoolean ?: false
    /** The light level the block emits */
    open var emittedLightLevel: LightLevel = definition["emittedLightLevel"].asInt?.coerceIn(0..16) ?: 0
    /** How much, on top of the normal attenuation, does the light level of light passing through this block is reduced ? */
    open var shadingLightLevel: LightLevel = definition["shadingLightLevel"].asInt?.coerceIn(0..16) ?: 0

    open val lootTable: BlockLootTable by lazy { createLootTable() }

    val textures: Array<BlockTexture> = Array(6) { content.blockTypes.getTexture(name) ?: content.blockTypes.defaultTexture }
    val representation: BlockRepresentation

    //TODO remove variants from here and plainly assign them different ids?
    val variants: List<ItemDefinition>

    val isAir get() = content.blockTypes.air.sameKind(this)

    init {
        representation = loadRepresentation(definition)

        /*/** Sets a custom voxel material */
        (definition["material"]?.asString ?: name).let {
            voxelMaterial = store.materials.getVoxelMaterial(it) ?: store.materials.defaultMaterial
        }*/

        variants = enumerateVariants(content.items)
    }

    /** Called after a voxel was successfully placed. Use to initialize additional data.
     * If you want to customize the behavior when attempting to place the block, please subclass ItemVoxel */
    open fun whenPlaced(cell: ChunkCell) {
        // Do nothing
    }

    /** Called when removing a block from the world or replacing it with something different. Return false to prevent. */
    open fun onRemove(cell: ChunkCell): Boolean {
        return true
    }

    /** Called when any of the metadata, block_light or sun_light values of the cell are being touched. Return false to prevent. */
    open fun onModification(cell: ChunkCell, newData: MutableCellData): Boolean {
        return true
    }

    open fun tick(cell: ChunkCell) {
        // Do nothing
    }

    /** Return 'true' to stop this input propagating */
    open fun onInteraction(entity: Entity, cell: ChunkCell, input: Input): Boolean {
        return false
    }

    /** Must be in [0, 15] */
    open fun getEmittedLightLevel(cellData: CellData): LightLevel {
        // By default the light output is the one defined in the type, you can change it depending on the provided data
        // Context-sensitive light levels (ie depending on adjacent blocks) are not supported
        return emittedLightLevel
    }

    @Deprecated("idk about this fam")
    open fun getTexture(cell: Cell, side: BlockSide): BlockTexture {
        // By default we don't care about taskInstance, we give the same texture to everyone
        return textures[side.ordinal]
    }

    open fun getLightLevelModifier(cellData: CellData, neighborData: CellData, side: BlockSide): LightLevel {
        return if (opaque) 15 else shadingLightLevel
    }

    open fun isFaceOpaque(cell: Cell, side: BlockSide): Boolean {
        return opaque
    }

    fun getTranslatedCollisionBoxes(cell: Cell): Array<Box> {
        val boxes = getCollisionBoxes(cell)
        for (b in boxes)
            b.translate(cell.x.toDouble(), cell.y.toDouble(), cell.z.toDouble())
        return boxes
    }

    open fun getCollisionBoxes(cell: Cell): Array<Box> = arrayOf(Box.fromExtents(Vector3d(1.0)))

    open fun sameKind(other: BlockType): Boolean {
        return this == other
    }

    protected open fun enumerateVariants(itemStore: Content.ItemsDefinitions): List<ItemDefinition> {
        val map = mutableMapOf<String, Json>(
                "voxel" to Json.Value.Text(name),
                "class" to Json.Value.Text(ItemBlock::class.java.canonicalName!!)
        )

        val additionalItems = definition["itemProperties"].asDict?.elements
        if (additionalItems != null)
            map.putAll(additionalItems)

        val definition = ItemDefinition(itemStore, name, Json.Dict(map))

        return listOf(definition)
    }

    /** Returns the variant that best matches the cell */
    open fun getVariant(data: CellData): ItemDefinition {
        return variants[0]
    }

    fun enumerateItemsForBuilding(): List<ItemBlock> {
        return variants.map { it.newItem<Item>() }.filterIsInstance<ItemBlock>()
    }

    // TODO move this elsewhere
    fun breakBlock(cell: MutableWorldCell, player: Player?, tool: MiningTool?) {
        if (tool != null) assert(player != null)

        val location = cell.location
        val world = location.world

        if (player != null) {
            val event = PlayerMineBlockEvent(player, cell, tool!!)
            world.gameInstance.pluginManager.fireEvent(event)
            if (event.isCancelled)
                return

            val shouldDropLoot = tool != TraitCreativeMode.CREATIVE_MODE_MINING_TOOL
            if (shouldDropLoot) {
                for ((item, amount) in getLoot(cell, tool)) {
                    val velocity = Vector3d(Math.random() * 0.125 - 0.0625, 0.1, Math.random() * 0.125 - 0.0625)
                    val lootLocation = Location(cell.location)
                    lootLocation.add(0.5, 0.5, 0.5)
                    EntityDroppedItem.spawn(item, amount, lootLocation, velocity)
                }
            }
        }

        cell.data.additionalData.clear()
        cell.data.extraData = 0
        cell.data.blocklightLevel = 0
        cell.data.sunlightLevel = 0
        cell.data.blockType = world.gameInstance.content.blockTypes.air

        //TODO spawnBlockDestructionParticles(location, world)
    }

    /** Returns what's dropped when a cell using this voxel type is destroyed  */
    open fun getLoot(cell: Cell, tool: MiningTool): List<Pair<Item, Int>> {
        if (isAir)
            return emptyList()
        return lootTable.spawn(tool)
    }

    open fun createLootTable(): BlockLootTable {
        val default = variants.getOrNull(0)?.let { Pair(it, 1) }
        return fromJson(definition["drops"] ?: Json.Value.Bool(true), content, default)
    }

    override fun toString(): String {
        return "[Voxel name:$name]"
    }

    val mineUsing: String = definition["mineUsing"].asString ?: "nothing_in_particular"
    val miningDifficulty: Double = definition["miningDifficulty"].asDouble ?: 1.0

    /*data class Material_(val name: String, properties: Json.Dict) {
        val walkingSounds: String = properties["walkingSounds"].asString ?: "error"
        val jumpingSounds: String = properties["jumpingSounds"].asString ?: walkingSounds
        val runningSounds: String = properties["runningSounds"].asString ?: walkingSounds
        val landingSounds: String = properties["landingSounds"].asString ?: walkingSounds
        val miningSounds: String = properties["miningSounds"].asString ?: runningSounds
        val minedSounds: String = properties["minedSounds"].asString ?: landingSounds

    }*/
}

abstract class BlockAdditionalData(val cell: ChunkCell) {
    val name: String = javaClass.simpleName
    open val serializationName = name

    /** Pushes the component to every client subscribed to the chunk owning this voxel  */
    /*fun pushComponentEveryone() {
        for (user in cell.chunk.holder.users) {
            if (user is RemotePlayer) {
                pushComponent(user)
            }
        }
    }

    /** Pushes the component to a specific player  */
    fun pushComponent(player: RemotePlayer) {
        val packet = PacketVoxelUpdate(holder.cell, this)
        player.pushPacket(packet)
    }*/

    @Throws(IOException::class)
    abstract fun serialize(): Json?

    @Throws(IOException::class)
    abstract fun deserialize(json: Json)
}

enum class BlockSide constructor(val dx: Int, val dy: Int, val dz: Int) {
    LEFT(-1, 0, 0),
    FRONT(0, 0, 1),
    RIGHT(1, 0, 0),
    BACK(0, 0, -1),
    TOP(0, 1, 0),
    BOTTOM(0, -1, 0);

    val oppositeSide: BlockSide
        get() = BlockSide.Companion.oppositeSide[this.ordinal]

    companion object {
        private val oppositeSide = arrayOf(RIGHT, BACK, LEFT, FRONT, BOTTOM, TOP)
    }
}

data class BlockTexture(
        val name: String,
        val averagedColor: Vector4fc,
        /** How many blocks does this texture span  */
        val textureScale: Int,
        /** Set N for N frames of animation, otherwise zero */
        val animationFrames: Int
)

fun BlockType.loadRepresentation(definition: Json.Dict): BlockRepresentation {
    definition["model"].asString?.let {
        val model = content.models[it]

        return BlockRepresentation.Custom {
            addModel(model)
        }
    }
    /** Sets all 6 sides of the voxel with one texture */
    definition["texture"]?.asString?.let { val tex = content.blockTypes.getTexture(it) ?: return@let ; textures.fill(tex) }
    val textures_ = definition["textures"]?.asDict
    if (textures_ != null) {
        /** Sets all 4 horizontal sides of the voxel with the same texture */
        textures_["sides"]?.asString?.let { val tex = content.blockTypes.getTexture(it) ?: return@let ; textures.fill(tex, 0, 4) }

        /** Code for setting each side */
        textures_["top"]?.asString?.let    { val tex = content.blockTypes.getTexture(it) ?: return@let ; textures[BlockSide.TOP.ordinal]    = tex }
        textures_["left"]?.asString?.let   { val tex = content.blockTypes.getTexture(it) ?: return@let ; textures[BlockSide.LEFT.ordinal]   = tex }
        textures_["right"]?.asString?.let  { val tex = content.blockTypes.getTexture(it) ?: return@let ; textures[BlockSide.RIGHT.ordinal]  = tex }
        textures_["front"]?.asString?.let  { val tex = content.blockTypes.getTexture(it) ?: return@let ; textures[BlockSide.FRONT.ordinal]  = tex }
        textures_["back"]?.asString?.let   { val tex = content.blockTypes.getTexture(it) ?: return@let ; textures[BlockSide.BACK.ordinal]   = tex }
        textures_["bottom"]?.asString?.let { val tex = content.blockTypes.getTexture(it) ?: return@let ; textures[BlockSide.BOTTOM.ordinal] = tex }
    }
    return BlockRepresentation.Cube
}

sealed class BlockRepresentation {
    object Cube : BlockRepresentation()
    class Custom(val drawRoutine: (RenderInterface.(Cell) -> Unit)? = null) : BlockRepresentation() {
        interface RenderInterface {
            fun addModel(model: Model, transformation: Matrix4f? = null, materialsOverrides: Map<Int, MeshMaterial> = emptyMap())
        }
    }
}

data class BlockSounds(
        val walkingSound: Sound,
        val jumpingSound: Sound,
        val runningSound: Sound,
        val landingSound: Sound,
        val miningSound: Sound,
        val minedSound: Sound
)

interface MiningTool {
    val miningEfficiency: Float
    val toolTypeName: String
}