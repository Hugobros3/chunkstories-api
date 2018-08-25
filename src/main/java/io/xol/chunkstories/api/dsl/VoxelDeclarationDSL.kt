package io.xol.chunkstories.api.dsl

import io.xol.chunkstories.api.voxel.Voxel
import io.xol.chunkstories.api.voxel.VoxelSide
import io.xol.chunkstories.api.world.cell.CellData
import kotlin.reflect.KClass

interface VoxelsDeclarationsCtx {
    fun voxel(declaration: Voxel.() -> Unit)

    fun <V : Voxel> voxel(clazz: Class<V>, declaration: V.() -> Unit)
    fun <V : Voxel> voxel(clazz: KClass<V>, declaration: V.() -> Unit)

    var Voxel.texture: String
        get() = this.voxelTextures[0].name
        set(value) {
            for (i in 0..6)
                this.voxelTextures[i] = store.textures().getVoxelTexture(value)
        }

    fun Voxel.textures(texturesSetCtx: VoxelTexturesSetCtx.() -> Unit) = VoxelTexturesSetCtx(this)

    var Voxel.material: Any
        set(value) {
            when (value) {
                is String -> this.voxelMaterial = store.materials().getVoxelMaterial(value)
            }
        }
        get() = throw Exception("This isn't a real field, just DSL syntactic sugar. Use voxelMaterial instead.")

    fun Voxel.loot(lootDefinition: LootTableBuildingContext.() -> Unit) {
        this.lootLogic = LootRules(lootDefinition)
    }

    fun Voxel.representation(config: VoxelRepresentationConfigurationContext.() -> Unit)
}

class VoxelTexturesSetCtx(val voxel: Voxel) {
    var top: String
        get() = voxel.voxelTextures[VoxelSide.TOP.ordinal].name
        set(value) {
            voxel.voxelTextures[VoxelSide.TOP.ordinal] = voxel.store.textures().getVoxelTexture(value)
        }

    var sides: String
        get() = voxel.voxelTextures[0].name
        set(value) {
            //TODO set all 4 vertical sides
        }

    //TODO bottom left etc
}

interface VoxelRepresentationConfigurationContext {
    fun cube(config: VoxelCubeRepresentationConfiguration.() -> Unit)

    var staticLodMaxDistance : Int
    fun static(static: StaticVoxelRepresentationBuildingInstructions)

    var dynamicLodMaxDistance : Int
    fun dynamic(dynamic: DynamicVoxelRepresentationBuildingInstructions)
}

interface VoxelCubeRepresentationConfiguration {
    var pass: String
    var passes: Set<String>
}

typealias StaticVoxelRepresentationBuildingInstructions = (VoxelRepresentationBuildingContext.() -> Unit)
typealias DynamicVoxelRepresentationBuildingInstructions = (DynamicVoxelRepresentationBuildingContext.() -> Unit)

interface VoxelRepresentationBuildingContext : StaticRepresentationBuildingContext {
    val cellData: CellData
}

interface DynamicVoxelRepresentationBuildingContext : DynamicRepresentationBuildingContext {
    val cellData: CellData
}