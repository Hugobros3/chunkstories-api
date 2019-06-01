//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item

import org.joml.Matrix4f
import org.joml.Vector4f
import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.entity.Controller
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.traits.TraitVoxelSelection
import xyz.chunkstories.api.entity.traits.serializable.TraitCreativeMode
import xyz.chunkstories.api.events.player.voxel.PlayerVoxelModificationEvent
import xyz.chunkstories.api.events.voxel.WorldModificationCause
import xyz.chunkstories.api.exceptions.world.WorldException
import xyz.chunkstories.api.graphics.MeshMaterial
import xyz.chunkstories.api.graphics.representation.ModelInstance
import xyz.chunkstories.api.graphics.representation.ModelPosition
import xyz.chunkstories.api.graphics.representation.PointLight
import xyz.chunkstories.api.graphics.systems.dispatching.RepresentationsGobbler
import xyz.chunkstories.api.input.Input
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.sound.SoundSource.Mode
import xyz.chunkstories.api.util.kotlin.toVec3d
import xyz.chunkstories.api.util.kotlin.toVec3f
import xyz.chunkstories.api.voxel.Voxel
import xyz.chunkstories.api.voxel.VoxelSide
import xyz.chunkstories.api.world.WorldMaster
import xyz.chunkstories.api.world.cell.DummyCell
import xyz.chunkstories.api.world.cell.FutureCell

/** An item that contains voxels  */
open class ItemVoxel(definition: ItemDefinition) : Item(definition), WorldModificationCause {
    private val store: Content.Voxels = definition.store().parent().voxels()

    val voxel: Voxel

    init {
        voxel = store.getVoxel(definition["voxel"]!!)!!
    }

    override fun getTextureName(pile: ItemPile): String {
        return "voxels/textures/" + voxel.getVoxelTexture(DummyCell(0, 0, 0, voxel, 0, 0, 15), VoxelSide.FRONT).name + ".png"
    }

    override fun buildRepresentation(pile: ItemPile, worldPosition: Matrix4f, representationsGobbler: RepresentationsGobbler) {
        //val customMaterial = MeshMaterial("cubeMaterial", mapOf("albedoTexture" to getTextureName(pile)))
        val customMaterials = VoxelSide.values().map { side ->
            val textureName = getTextureName(pile)//"voxels/textures/" + voxel.getVoxelTexture(DummyCell(0, 0, 0, voxel, voxelMeta, 0, 15), side).name + ".png"
            //println("$side -> $textureName")
            val material = MeshMaterial("cubeMaterial$side", mapOf("albedoTexture" to textureName))
            Pair(side.ordinal, material)
        }.toMap()
        val representation = ModelInstance(store.parent().models["voxels/blockmodels/cube/cube.dae"], ModelPosition(worldPosition).apply {
            matrix.scale(0.35f)
            matrix.translate(-0.5f, -0.0f, -0.5f)
        }, customMaterials)
        representationsGobbler.acceptRepresentation(representation, -1)

        val position = Vector4f(0f, 0f, 0f, 1f)
        worldPosition.transform(position)
        if (voxel.emittedLightLevel > 0) {
            val light = PointLight(position.toVec3f().toVec3d().add(0.0, 0.5, 0.0), voxel.voxelTextures[0].color.toVec3f().toVec3d().mul(voxel.emittedLightLevel.toDouble()))
            representationsGobbler.acceptRepresentation(light)
            //println(light)
        }
    }

    open fun changeBlockData(cell: FutureCell, placingEntity: Entity): Boolean {
        cell.voxel = voxel
        //cell.metaData = voxelMeta
        cell.metaData = 0

        // Opaque blocks overwrite the original light with zero.
        if (voxel.opaque) {
            cell.blocklight = 0
            cell.sunlight = 0
        }

        // Glowy stuff should glow
        cell.blocklight = voxel.getEmittedLightLevel(cell)

        return true
    }

    override fun onControllerInput(entity: Entity, pile: ItemPile, input: Input, controller: Controller): Boolean {
        try {
            if (entity.world is WorldMaster && input.name == "mouse.right") {
                // Require entities to be of the right kind
                if (entity !is WorldModificationCause) {
                    return true
                }

                val modifierEntity = entity as WorldModificationCause

                val isEntityCreativeMode = entity.traits[TraitCreativeMode::class]?.get() ?: false

                val blockLocation = entity.traits[TraitVoxelSelection::class]?.getBlockLookingAt(false, true)

                if (blockLocation != null) {
                    val futureCell = FutureCell(entity.world.peekSafely(blockLocation))

                    // Let's stop if this returns false
                    if (!changeBlockData(futureCell, entity))
                        return true

                    // Player events mod
                    if (controller is Player) {
                        val ctx = entity.world.peek(blockLocation)
                        val event = PlayerVoxelModificationEvent(ctx, futureCell,
                                if (isEntityCreativeMode) TraitCreativeMode.CREATIVE_MODE else this, controller)

                        // Anyone has objections ?
                        entity.world.gameContext.pluginManager.fireEvent(event)

                        if (event.isCancelled)
                            return true

                        entity.world.soundManager.playSoundEffect("sounds/gameplay/voxel_place.ogg", Mode.NORMAL, futureCell.location, 1.0f, 1.0f)
                    }

                    entity.world.poke(futureCell, modifierEntity)

                    // Decrease stack size
                    if (!isEntityCreativeMode) {
                        pile.amount--
                        //var currentAmount = pile.amount
                        //currentAmount--
                        //pile.amount = currentAmount
                    }
                } else {
                    // No space found :/
                    return true
                }
            }

        } catch (e: WorldException) {

        }

        return false

    }

    /*

    @Throws(IOException::class)
    override fun load(stream: DataInputStream) {
        voxel = store.getVoxel(stream.readUTF()) ?: store.air()
        voxelMeta = stream.readByte().toInt()
    }

    @Throws(IOException::class)
    override fun save(stream: DataOutputStream) {
        stream.writeUTF(voxel.name)
        stream.writeByte(voxelMeta)
    }

    /** Two ItemVoxel can merge if they represent the same voxel & they share the
     * same 8 bits of metadata  */
    override fun canStackWith(item: Item): Boolean {
        return if (item is ItemVoxel) {
            super.canStackWith(item) && item.voxel.sameKind(voxel) && item.voxelMeta == this.voxelMeta
        } else false
    }*/
}
