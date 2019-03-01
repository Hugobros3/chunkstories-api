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
import xyz.chunkstories.api.graphics.Mesh
import xyz.chunkstories.api.item.ItemDefinition
import xyz.chunkstories.api.net.Packet
import xyz.chunkstories.api.net.PacketDefinition
import xyz.chunkstories.api.particles.ParticleTypeHandler
import xyz.chunkstories.api.voxel.Voxel
import xyz.chunkstories.api.voxel.materials.VoxelMaterial
import xyz.chunkstories.api.voxel.textures.VoxelTexture
import xyz.chunkstories.api.world.generator.WorldGenerator
import xyz.chunkstories.api.world.generator.WorldGeneratorDefinition
import org.slf4j.Logger
import xyz.chunkstories.api.graphics.representation.Model

/** Encapsulates all the user-definable content available  */
interface Content {
    /** Returns the game context that owns this  */
    val context: GameContext

    val animationsLibrary: AnimationsLibrary

    /** Returns the ModManager that was used to load this content  */
    fun modsManager(): ModsManager

    /** Obtains an Asset using it's name string More advanced options for obtaining
     * assets are avaible using the ModsManager class  */
    fun getAsset(assetName: String): Asset?

    /** Reloads everything. Warning: might not be appropriate as this will reload
     * classes you will most likely have instanced into objects, causing weird
     * errors if you do this while in a world.  */
    fun reload()

    fun voxels(): Voxels

    interface Voxels {

        /** Returns the 'air' voxel ( No voxel data )  */
        fun air(): Voxel

        fun getVoxel(voxelName: String): Voxel?

        fun all(): Iterator<Voxel>

        fun parent(): Content

        fun textures(): VoxelTextures

        interface VoxelTextures {
            val defaultVoxelTexture: VoxelTexture

            val all: Collection<VoxelTexture>

            /** Looks for a voxel texture, if it fails it returns the default texture  */
            fun get(voxelTextureName: String): VoxelTexture

            fun parent(): Voxels

            fun logger(): Logger
        }

        fun materials(): VoxelMaterials

        interface VoxelMaterials {

            val defaultMaterial: VoxelMaterial

            fun getVoxelMaterial(materialName: String): VoxelMaterial?

            fun all(): Iterator<VoxelMaterial>

            fun parent(): Content

            fun logger(): Logger
        }

        fun logger(): Logger
    }

    fun items(): ItemsDefinitions

    interface ItemsDefinitions {

        fun getItemDefinition(itemName: String): ItemDefinition?

        fun all(): Iterator<ItemDefinition>

        fun parent(): Content

        fun logger(): Logger
    }

    fun entities(): EntityDefinitions

    interface EntityDefinitions {
        fun getEntityDefinition(entityName: String): EntityDefinition?

        fun all(): Iterator<EntityDefinition>

        fun parent(): Content

        fun logger(): Logger
    }

    fun particles(): ParticlesTypes

    interface ParticlesTypes {
        fun getParticleType(string: String): ParticleTypeHandler?

        fun all(): Iterator<ParticleTypeHandler>

        fun parent(): Content

        fun logger(): Logger
    }

    fun packets(): PacketDefinitions

    interface PacketDefinitions {
        fun getPacketByName(name: String): PacketDefinition?

        @Throws(UnknowPacketException::class)
        fun getPacketFromInstance(packet: Packet): PacketDefinition

        fun all(): Iterator<PacketDefinition>

        fun parent(): Content

        fun logger(): Logger
    }

    fun generators(): WorldGenerators

    interface WorldGenerators {
        fun getWorldGenerator(name: String): WorldGeneratorDefinition

        //fun getWorldGeneratorName(generator: WorldGenerator): String

        fun all(): Iterator<WorldGeneratorDefinition>

        fun parent(): Content

        fun logger(): Logger
    }

    fun localization(): LocalizationManager

    interface LocalizationManager : Translation {
        fun listTranslations(): Collection<String>

        fun parent(): Content

        fun reload()

        fun loadTranslation(translationCode: String)

        fun logger(): Logger
    }

    interface Translation {
        fun getLocalizedString(stringName: String): String

        fun localize(text: String): String
    }

    interface AnimationsLibrary {
        fun getAnimation(name: String): Animation

        fun parent(): Content

        fun reloadAll()

        fun logger(): Logger
    }

    val models : Models
    interface Models {
        operator fun get(assetName: String) = getOrLoadModel(assetName)

        fun getOrLoadModel(assetName: String): Model

        val defaultModel: Model
    }

    /** General logger about content  */
    fun logger(): Logger
}
