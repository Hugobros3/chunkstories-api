//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item

import org.joml.Matrix4f
import org.joml.Vector3i
import org.joml.Vector4f
import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.json.asString
import xyz.chunkstories.api.entity.Controller
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.traits.TraitSight
import xyz.chunkstories.api.entity.traits.serializable.TraitCreativeMode
import xyz.chunkstories.api.graphics.MeshMaterial
import xyz.chunkstories.api.graphics.representation.ModelInstance
import xyz.chunkstories.api.graphics.representation.ModelPosition
import xyz.chunkstories.api.graphics.representation.PointLight
import xyz.chunkstories.api.graphics.systems.dispatching.RepresentationsGobbler
import xyz.chunkstories.api.input.Input
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.physics.RayQuery
import xyz.chunkstories.api.physics.RayResult
import xyz.chunkstories.api.physics.trace
import xyz.chunkstories.api.sound.SoundSource.Mode
import xyz.chunkstories.api.util.kotlin.toVec3d
import xyz.chunkstories.api.util.kotlin.toVec3f
import xyz.chunkstories.api.util.kotlin.toVec3i
import xyz.chunkstories.api.block.BlockType
import xyz.chunkstories.api.block.BlockSide
import xyz.chunkstories.api.events.voxel.PlayerPlaceBlockEvent
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.world.WorldMaster
import xyz.chunkstories.api.world.cell.*

/** An item that contains voxels  */
open class ItemBlock(definition: ItemDefinition) : Item(definition) {
    private val store: Content.BlockTypes = definition.store.parent.blockTypes
    val blockType: BlockType

    init {
        blockType = store[definition.properties["block"].asString ?: throw Exception("ItemBlock '${definition.name}' missing a 'block' definition !")]!!
    }

    override fun getTextureName(): String {
        return "voxels/textures/" + blockType.getTexture(PodCell(0, 0, 0, PodCellData(blockType, 0, 0, 15)), BlockSide.FRONT).name + ".png"
    }

    override fun buildRepresentation(worldPosition: Matrix4f, representationsGobbler: RepresentationsGobbler) {
        //val customMaterial = MeshMaterial("cubeMaterial", mapOf("albedoTexture" to getTextureName(pile)))
        val customMaterials = BlockSide.values().associate { side ->
            val textureName = "voxels/textures/" + blockType.getTexture(PodCell(0, 0, 0, PodCellData(blockType, 0, 0, 15)), side).name + ".png"
            val material = MeshMaterial("cubeMaterial$side", mapOf("albedoTexture" to textureName))
            Pair(side.ordinal, material)
        }

        val representation = ModelInstance(store.content.models["voxels/blockmodels/cube/cube.dae"], ModelPosition(worldPosition).apply {
            matrix.scale(0.35f)
            matrix.translate(-0.5f, -0.0f, -0.5f)
        }, customMaterials)
        representationsGobbler.acceptRepresentation(representation)

        val position = Vector4f(0f, 0f, 0f, 1f)
        worldPosition.transform(position)
        if (blockType.emittedLightLevel > 0) {
            val light = PointLight(position.toVec3f().toVec3d().add(0.0, 0.5, 0.0), blockType.textures[0].averagedColor.toVec3f().toVec3d().mul(blockType.emittedLightLevel.toDouble()))
            representationsGobbler.acceptRepresentation(light)
        }
    }

    override fun onControllerInput(entity: Entity, itemPile: ItemPile, input: Input, controller: Controller): Boolean {
        if (entity.world is WorldMaster && input.name == "mouse.right") {
            val isEntityCreativeMode = entity.traits[TraitCreativeMode::class]?.enabled ?: false

            val hit: RayResult.Hit.VoxelHit = entity.traits[TraitSight::class]?.let {
                val reach = 5.0
                val notMe = { other: Entity -> other != entity }
                val query = RayQuery(it.headLocation, it.lookingAt, 0.0, reach, { it.blockType.solid }, notMe)
                when (val hit = query.trace()) {
                    is RayResult.Hit.VoxelHit -> {
                        return@let hit as RayResult.Hit.VoxelHit // Kotlin is buggy: if I remove this cast it fails
                    }
                }

                return@let null
            } ?: return false

            val pointedCell = hit.cell

            val normali = hit.normal.toVec3i()
            val x = hit.cell.x + normali.x
            val y = hit.cell.y + normali.y
            val z = hit.cell.z + normali.z
            val side = when (normali) {
                Vector3i(1, 0, 0) -> BlockSide.RIGHT
                Vector3i(-1, 0, 0) -> BlockSide.LEFT
                Vector3i(0, 1, 0) -> BlockSide.TOP
                Vector3i(0, -1, 0) -> BlockSide.BOTTOM
                Vector3i(0, 0, 1) -> BlockSide.FRONT
                Vector3i(0, 0, -1) -> BlockSide.BACK
                else -> throw Exception("How did we get here ?")
            }

            val targetCell = entity.world.getCellMut(x, y, z) ?: return true

            // TODO handle overwriting blocks likes grass and such
            if (targetCell.data.blockType.isAir) {
                var data: CellData = prepareNewBlockData(pointedCell, side, entity, hit) ?: return true

                // Player events mod
                if (controller is Player) {
                    val event = PlayerPlaceBlockEvent(controller, targetCell, data)

                    // Anyone has objections ?
                    entity.world.gameInstance.pluginManager.fireEvent(event)

                    if (event.isCancelled)
                        return true

                    data = event.newData
                    entity.world.soundManager.playSoundEffect("sounds/gameplay/voxel_place.ogg", Mode.NORMAL, targetCell.location, 1.0f, 1.0f)
                }

                entity.world.setCellData(x, y, z, data)

                if (!isEntityCreativeMode) {
                    itemPile.amount--
                }
            } else {
                // No space found :/
                return true
            }
        }

        return false
    }

    open fun prepareNewBlockData(adjacentCell: Cell, adjacentCellSide: BlockSide, placingEntity: Entity, hit: RayResult.Hit.VoxelHit): PodCellData? {
        var data = PodCellData(blockType, extraData = 0)

        // Glowy stuff should glow
        data = data.copy(blocklightLevel = blockType.getEmittedLightLevel(data))

        return data
    }
}
