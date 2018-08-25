//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.content;

import java.util.Collection;
import java.util.Iterator;

import io.xol.chunkstories.api.voxel.materials.Material;
import org.slf4j.Logger;

import io.xol.chunkstories.api.GameContext;
import io.xol.chunkstories.api.animation.SkeletalAnimation;
import io.xol.chunkstories.api.content.mods.ModsManager;
import io.xol.chunkstories.api.entity.EntityDeclaration;
import io.xol.chunkstories.api.exceptions.net.UnknowPacketException;
import io.xol.chunkstories.api.item.ItemDeclaration;
import io.xol.chunkstories.api.mesh.MeshLibrary;
import io.xol.chunkstories.api.net.Packet;
import io.xol.chunkstories.api.net.PacketDefinition;
import io.xol.chunkstories.api.particles.ParticleTypeHandler;
import io.xol.chunkstories.api.rendering.voxel.VoxelRenderer;
import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.voxel.textures.VoxelTexture;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.generator.WorldGenerator;

import javax.annotation.Nullable;

/** Encapsulates all the user-definable content available */
public interface Content {
	/** Returns the game context that owns this */
	GameContext getContext();

	/** Returns the ModManager that was used to load this content */
	ModsManager modsManager();

	/** Obtains an Asset using it's name string More advanced options for obtaining
	 * assets are avaible using the ModsManager class */
	@Nullable
	Asset getAsset(String assetName);

	/** Reloads everything. Warning: might not be appropriate as this will reload
	 * classes you will most likely have instanced into objects, causing weird
	 * errors if you do this while in a world. */
	void reload();

	Voxels voxels();

	interface Voxels {

		/** Returns the 'air' voxel ( No voxel data ) */
		Voxel air();

		@Nullable
		Voxel getVoxel(String voxelName);

		Iterator<Voxel> all();

		Content parent();

		VoxelTextures textures();

		interface VoxelTextures {

			VoxelTexture getVoxelTexture(String voxelTextureName);

			VoxelTexture getDefaultVoxelTexture();

			Iterator<VoxelTexture> all();

			Voxels parent();

			Logger logger();
		}

		VoxelMaterials materials();

		interface VoxelMaterials {

			Material getVoxelMaterial(String materialName);

			Iterator<Material> all();

			Material getDefaultMaterial();

			Content parent();

			Logger logger();
		}

		VoxelRenderer getDefaultVoxelRenderer();

		Logger logger();
	}

	ItemsDefinitions items();

	interface ItemsDefinitions {

		@Nullable
		ItemDeclaration getItemDefinition(String itemName);

		Iterator<ItemDeclaration> all();

		Content parent();

		Logger logger();
	}

	EntityDeclarations entities();

	interface EntityDeclarations {

		@Nullable
		EntityDeclaration getEntityDefinition(String entityName);

		Iterator<EntityDeclaration> all();

		Content parent();

		Logger logger();
	}

	ParticlesTypes particles();

	interface ParticlesTypes {

		@Nullable
		ParticleTypeHandler getParticleType(String string);

		Iterator<ParticleTypeHandler> all();

		Content parent();

		Logger logger();
	}

	PacketDefinitions packets();

	interface PacketDefinitions {

		@Nullable
		PacketDefinition getPacketByName(String name);

		PacketDefinition getPacketFromInstance(Packet packet) throws UnknowPacketException;

		Iterator<PacketDefinition> all();

		Content parent();

		Logger logger();
	}

	WorldGenerators generators();

	interface WorldGenerators {

		WorldGeneratorDefinition getWorldGenerator(String name);

		String getWorldGeneratorName(WorldGenerator generator);

		Iterator<WorldGeneratorDefinition> all();

		/** Contains the parameters stated in a 'generator' section of a .generators
		 * config file */
		interface WorldGeneratorDefinition extends Definition {
			String getName();

			/** Calls the constructor of whatever WorldGenerator you asked for */
			WorldGenerator createForWorld(World world);
		}

		Content parent();

		Logger logger();
	}

	LocalizationManager localization();

	interface LocalizationManager extends Translation {

		Collection<String> listTranslations();

		Content parent();

		void reload();

		void loadTranslation(String translationCode);

		Logger logger();
	}

	interface Translation {

		String getLocalizedString(String stringName);

		String localize(String text);
	}

	AnimationsLibrary getAnimationsLibrary();

	interface AnimationsLibrary {
		SkeletalAnimation getAnimation(String name);

		Content parent();

		void reloadAll();

		Logger logger();
	}

	MeshLibrary meshes();

	/** General logger about content */
	Logger logger();
}
