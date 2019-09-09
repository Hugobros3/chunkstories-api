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
import xyz.chunkstories.api.physics.RayQuery
import xyz.chunkstories.api.physics.RayResult
import xyz.chunkstories.api.physics.trace
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.sound.SoundSource.Mode
import xyz.chunkstories.api.util.kotlin.toVec3d
import xyz.chunkstories.api.util.kotlin.toVec3f
import xyz.chunkstories.api.util.kotlin.toVec3i
import xyz.chunkstories.api.voxel.Voxel
import xyz.chunkstories.api.voxel.VoxelSide
import xyz.chunkstories.api.world.WorldMaster
import xyz.chunkstories.api.world.cell.Cell
import xyz.chunkstories.api.world.cell.DummyCell
import xyz.chunkstories.api.world.cell.FutureCell

/** An item that contains voxels  */
open class ItemVoxel(definition: ItemDefinition) : Item(definition), WorldModificationCause {
    private val store: Content.Voxels = definition.store.parent.voxels

    val voxel: Voxel

    init {
        voxel = store.getVoxel(definition["voxel"].asString!!)!!
    }

    override fun getTextureName(): String {
        return "voxels/textures/" + voxel.getVoxelTexture(DummyCell(0, 0, 0, voxel, 0, 0, 15), VoxelSide.FRONT).name + ".png"
    }

    override fun buildRepresentation(worldPosition: Matrix4f, representationsGobbler: RepresentationsGobbler) {
        //val customMaterial = MeshMaterial("cubeMaterial", mapOf("albedoTexture" to getTextureName(pile)))
        val customMaterials = VoxelSide.values().map { side ->
            val textureName = "voxels/textures/" + voxel.getVoxelTexture(DummyCell(0, 0, 0, voxel, 0, 0, 15), side).name + ".png"
            val material = MeshMaterial("cubeMaterial$side", mapOf("albedoTexture" to textureName))
            Pair(side.ordinal, material)
        }.toMap()


        val representation = ModelInstance(store.parent.models["voxels/blockmodels/cube/cube.dae"], ModelPosition(worldPosition).apply {
            matrix.scale(0.35f)
            matrix.translate(-0.5f, -0.0f, -0.5f)
        }, customMaterials)
        representationsGobbler.acceptRepresentation(representation, -1)

        val position = Vector4f(0f, 0f, 0f, 1f)
        worldPosition.transform(position)
        if (voxel.emittedLightLevel > 0) {
            val light = PointLight(position.toVec3f().toVec3d().add(0.0, 0.5, 0.0), voxel.voxelTextures[0].color.toVec3f().toVec3d().mul(voxel.emittedLightLevel.toDouble()))
            representationsGobbler.acceptRepresentation(light)
        }
    }

    override fun onControllerInput(entity: Entity, pile: ItemPile, input: Input, controller: Controller): Boolean {
        try {
            if (entity.world is WorldMaster && input.name == "mouse.right") {
                // Require entities to be of the right kind
                if (entity !is WorldModificationCause) {
                    return true
                }

                val modifierEntity = entity as WorldModificationCause

                val isEntityCreativeMode = entity.traits[TraitCreativeMode::class]?.enabled ?: false

                val hit: RayResult.Hit.VoxelHit = entity.traits[TraitSight::class]?.let {
                    val reach = 5.0
                    val notMe = {other: Entity -> other != entity}
                    val query = RayQuery(it.headLocation, it.lookingAt, 0.0, reach, { it.voxel.solid }, notMe)
                    when(val hit = query.trace()) {
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
                val side =  when(normali) {
                    Vector3i(1, 0, 0) -> VoxelSide.RIGHT
                    Vector3i(-1, 0, 0) -> VoxelSide.LEFT
                    Vector3i(0, 1, 0) -> VoxelSide.TOP
                    Vector3i(0, -1, 0) -> VoxelSide.BOTTOM
                    Vector3i(0, 0, 1) -> VoxelSide.FRONT
                    Vector3i(0 ,0, -1) -> VoxelSide.BACK
                    else -> throw Exception("How did we get here ?")
                }

                val adjacentCell = if(y in 0 until hit.cell.world.maxHeight) {
                    hit.cell.world.peek(x, y, z)
                } else {
                    return false
                }

                if (adjacentCell.voxel.isAir() /** TODO handle overwriting blocks cases */) {
                    val futureCell = FutureCell(adjacentCell)

                    // Let's stop if this returns false
                    if (!prepareNewBlockData(futureCell, pointedCell, side, entity, hit))
                        return true

                    // Player events mod
                    if (controller is Player) {
                        //val ctx = entity.world.tryPeek(freeCell.location)
                        val event = PlayerVoxelModificationEvent(adjacentCell, futureCell,
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

    open fun prepareNewBlockData(cell: FutureCell, adjacentCell: Cell, adjacentCellSide: VoxelSide, placingEntity: Entity, hit: RayResult.Hit.VoxelHit): Boolean {
        cell.voxel = voxel
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
}
