//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.content

import xyz.chunkstories.api.GameContext
import xyz.chunkstories.api.animation.Animation
import xyz.chunkstories.api.content.mods.ModsManager
import xyz.chunkstories.api.entity.EntityDefinition
import xyz.chunkstories.api.exceptions.net.UnknowPacketException
import xyz.chunkstories.api.item.ItemDefinition
import xyz.chunkstories.api.net.Packet
import xyz.chunkstories.api.net.PacketDefinition
import xyz.chunkstories.api.voxel.Voxel
import xyz.chunkstories.api.voxel.materials.VoxelMaterial
import xyz.chunkstories.api.voxel.textures.VoxelTexture
import xyz.chunkstories.api.world.generator.WorldGeneratorDefinition
import org.slf4j.Logger
import xyz.chunkstories.api.crafting.Recipe
import xyz.chunkstories.api.graphics.representation.Model
import xyz.chunkstories.api.gui.inventory.InventorySlot
import xyz.chunkstories.api.loot.LootTable
import xyz.chunkstories.api.particles.ParticleType

/** Encapsulates all the user-definable content available  */
interface Content {
    /** Returns the game taskInstance that owns this  */
    val context: GameContext

    val animationsLibrary: AnimationsLibrary

    /** Returns the ModManager that was used to load this content  */
    val modsManager: ModsManager

    /** Obtains an Asset using it's name string More advanced options for obtaining
     * assets are avaible using the ModsManager class  */
    fun getAsset(assetName: String): Asset?

    /** Reloads everything. Warning: might not be appropriate as this will reload
     * classes you will most likely have instanced into objects, causing weird
     * errors if you do this while in a world.  */
    fun reload()

    val voxels: Voxels

    interface Voxels {

        /** Returns the 'air' voxel ( No voxel data )  */
        val air: Voxel

        fun getVoxel(voxelName: String): Voxel?

        val all: Collection<Voxel>

        val parent: Content

        val textures: VoxelTextures

        interface VoxelTextures {
            val defaultVoxelTexture: VoxelTexture

            val all: Collection<VoxelTexture>

            /** Looks for a voxel texture, if it fails it returns the default texture  */
            fun get(voxelTextureName: String): VoxelTexture

            val parent: Voxels

            val logger: Logger
        }

        val materials: VoxelMaterials

        interface VoxelMaterials {

            val defaultMaterial: VoxelMaterial

            fun getVoxelMaterial(materialName: String): VoxelMaterial?

            val all: Collection<VoxelMaterial>

            val parent: Content

            val logger: Logger
        }

        val logger: Logger
    }

    val items: ItemsDefinitions

    interface ItemsDefinitions {

        fun getItemDefinition(itemName: String): ItemDefinition?

        val all: Collection<ItemDefinition>

        val parent: Content

        val logger: Logger
    }

    val entities: EntityDefinitions

    interface EntityDefinitions {
        fun getEntityDefinition(entityName: String): EntityDefinition?

        val all: Collection<EntityDefinition>

        val parent: Content

        val logger: Logger
    }

    val particles: ParticlesTypes

    interface ParticlesTypes {
        fun <T: ParticleType.Particle> getParticleType(string: String): ParticleType<T>?

        val all: Collection<ParticleType<*>>
        val parent: Content

        val logger: Logger
    }

    val packets: PacketDefinitions

    interface PacketDefinitions {
        fun getPacketByName(name: String): PacketDefinition?

        @Throws(UnknowPacketException::class)
        fun getPacketFromInstance(packet: Packet): PacketDefinition

        val all: Collection<PacketDefinition>

        val parent: Content

        val logger: Logger
    }

    val generators: WorldGenerators

    interface WorldGenerators {
        fun getWorldGenerator(name: String): WorldGeneratorDefinition

        //fun getWorldGeneratorName(generator: WorldGenerator): String

        val all: Collection<WorldGeneratorDefinition>

        val parent: Content

        val logger: Logger
    }

    fun localization(): LocalizationManager

    interface LocalizationManager : Translation {
        fun listTranslations(): Collection<String>

        fun parent(): Content

        fun reload()

        fun loadTranslation(translationCode: String)

        val logger: Logger
    }

    interface Translation {
        fun getLocalizedString(stringName: String): String

        fun localize(text: String): String
    }

    interface AnimationsLibrary {
        fun getAnimation(name: String): Animation

        val parent: Content

        fun reloadAll()

        val logger: Logger
    }

    val models : Models
    interface Models {
        operator fun get(assetName: String) = getOrLoadModel(assetName)

        fun getOrLoadModel(assetName: String): Model

        val defaultModel: Model
    }

    val recipes: Recipes
    interface Recipes {
        val all: Collection<Recipe>

        /** The underlying slots have to be fake slots ! */
        fun getRecipeForInventorySlots(craftingAreaSlots: Array<Array<InventorySlot.FakeSlot>>): Recipe?

        fun reload()
    }

    val lootTables: LootTables
    interface LootTables {
        val all: Map<String, LootTable>

        operator fun get(name: String) = all[name]

        fun reload()
    }

    /** General logger about content  */
    val logger: Logger
}
