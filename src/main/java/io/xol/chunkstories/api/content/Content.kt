//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.content

import io.xol.chunkstories.api.GameContext
import io.xol.chunkstories.api.animation.Animation
import io.xol.chunkstories.api.content.mods.ModsManager
import io.xol.chunkstories.api.entity.EntityDefinition
import io.xol.chunkstories.api.exceptions.net.UnknowPacketException
import io.xol.chunkstories.api.graphics.Mesh
import io.xol.chunkstories.api.item.ItemDefinition
import io.xol.chunkstories.api.net.Packet
import io.xol.chunkstories.api.net.PacketDefinition
import io.xol.chunkstories.api.particles.ParticleTypeHandler
import io.xol.chunkstories.api.voxel.Voxel
import io.xol.chunkstories.api.voxel.materials.VoxelMaterial
import io.xol.chunkstories.api.voxel.textures.VoxelTexture
import io.xol.chunkstories.api.world.generator.WorldGenerator
import io.xol.chunkstories.api.world.generator.WorldGeneratorDefinition
import org.slf4j.Logger

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

            /** Looks for a voxel texture, if it fails it returns the default texture  */
            fun getVoxelTexture(voxelTextureName: String): VoxelTexture

            fun all(): Iterator<VoxelTexture>

            fun parent(): Voxels

            fun logger(): Logger
        }

        fun materials(): VoxelMaterials

        interface VoxelMaterials {

            val defaultMaterial: VoxelMaterial

            fun getVoxelMaterial(materialName: String): VoxelMaterial

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

    interface Meshes {
        fun getMesh(meshName: String) : Mesh

        val defaultMesh: Mesh
    }

    fun meshes(): Meshes

    /** General logger about content  */
    fun logger(): Logger
}
