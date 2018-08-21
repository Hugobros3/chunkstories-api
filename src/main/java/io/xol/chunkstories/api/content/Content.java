//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.content;

import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;

import io.xol.chunkstories.api.GameContext;
import io.xol.chunkstories.api.animation.SkeletalAnimation;
import io.xol.chunkstories.api.content.mods.ModsManager;
import io.xol.chunkstories.api.entity.EntityDefinition;
import io.xol.chunkstories.api.exceptions.net.UnknowPacketException;
import io.xol.chunkstories.api.item.ItemDefinition;
import io.xol.chunkstories.api.mesh.MeshLibrary;
import io.xol.chunkstories.api.net.Packet;
import io.xol.chunkstories.api.net.PacketDefinition;
import io.xol.chunkstories.api.particles.ParticleTypeHandler;
import io.xol.chunkstories.api.rendering.voxel.VoxelRenderer;
import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.voxel.materials.VoxelMaterial;
import io.xol.chunkstories.api.voxel.models.VoxelModel;
import io.xol.chunkstories.api.voxel.textures.VoxelTexture;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.generator.WorldGenerator;

import javax.annotation.Nullable;

/** Encapsulates all the user-definable content available */
public interface Content {
	/** Returns the game context that owns this */
	public GameContext getContext();

	/** Returns the ModManager that was used to load this content */
	public ModsManager modsManager();

	/** Obtains an Asset using it's name string More advanced options for obtaining
	 * assets are avaible using the ModsManager class */
	@Nullable
	public Asset getAsset(String assetName);

	/** Reloads everything. Warning: might not be appropriate as this will reload
	 * classes you will most likely have instanced into objects, causing weird
	 * errors if you do this while in a world. */
	public void reload();

	public Voxels voxels();

	public interface Voxels {

		/** Returns the 'air' voxel ( No voxel data ) */
		public Voxel air();

		@Nullable
		public Voxel getVoxel(String voxelName);

		public Iterator<Voxel> all();

		public Content parent();

		public VoxelTextures textures();

		public interface VoxelTextures {

			public VoxelTexture getVoxelTexture(String voxelTextureName);

			public Iterator<VoxelTexture> all();

			public Voxels parent();

			public Logger logger();
		}

		public VoxelModels models();

		public interface VoxelModels {

			@Nullable
			public VoxelModel getVoxelModel(String voxelModelName);

			public Iterator<VoxelModel> all();

			public Voxels parent();

			public Logger logger();
		}

		public VoxelMaterials materials();

		public interface VoxelMaterials {

			public VoxelMaterial getVoxelMaterial(String materialName);

			public Iterator<VoxelMaterial> all();

			public Content parent();

			public Logger logger();
		}

		public VoxelRenderer getDefaultVoxelRenderer();

		public Logger logger();
	}

	public ItemsDefinitions items();

	public interface ItemsDefinitions {

		@Nullable
		public ItemDefinition getItemDefinition(String itemName);

		public Iterator<ItemDefinition> all();

		public Content parent();

		public Logger logger();
	}

	public EntityDefinitions entities();

	public interface EntityDefinitions {

		@Nullable
		public EntityDefinition getEntityDefinition(String entityName);

		public Iterator<EntityDefinition> all();

		public Content parent();

		public Logger logger();
	}

	public ParticlesTypes particles();

	public interface ParticlesTypes {

		@Nullable
		public ParticleTypeHandler getParticleType(String string);

		public Iterator<ParticleTypeHandler> all();

		public Content parent();

		public Logger logger();
	}

	public PacketDefinitions packets();

	public interface PacketDefinitions {

		@Nullable
		public PacketDefinition getPacketByName(String name);

		public PacketDefinition getPacketFromInstance(Packet packet) throws UnknowPacketException;

		public Iterator<PacketDefinition> all();

		public Content parent();

		public Logger logger();
	}

	public WorldGenerators generators();

	public interface WorldGenerators {

		public WorldGeneratorDefinition getWorldGenerator(String name);

		public String getWorldGeneratorName(WorldGenerator generator);

		public Iterator<WorldGeneratorDefinition> all();

		/** Contains the parameters stated in a 'generator' section of a .generators
		 * config file */
		public interface WorldGeneratorDefinition extends Definition {
			public String getName();

			/** Calls the constructor of whatever WorldGenerator you asked for */
			public WorldGenerator createForWorld(World world);
		}

		public Content parent();

		public Logger logger();
	}

	public LocalizationManager localization();

	public interface LocalizationManager extends Translation {

		public Collection<String> listTranslations();

		public Content parent();

		public void reload();

		public void loadTranslation(String translationCode);

		public Logger logger();
	}

	public interface Translation {

		public String getLocalizedString(String stringName);

		public String localize(String text);
	}

	public AnimationsLibrary getAnimationsLibrary();

	public interface AnimationsLibrary {
		public SkeletalAnimation getAnimation(String name);

		public Content parent();

		public void reloadAll();

		public Logger logger();
	}

	public MeshLibrary meshes();

	/** General logger about content */
	public Logger logger();
}
