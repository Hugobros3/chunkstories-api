package io.xol.chunkstories.api.content;

import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;

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
import io.xol.chunkstories.api.voxel.materials.Material;
import io.xol.chunkstories.api.voxel.models.VoxelModel;
import io.xol.chunkstories.api.voxel.textures.VoxelTexture;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.generator.WorldGenerator;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/**
 * Encapsulates all the user-definable content available
 */
public interface Content
{
	/** Returns the ModManager that was used to load this content */
	public ModsManager modsManager();

	/** Obtains an Asset using it's name string 
	 *  More advanced options for obtaining assets are avaible using the ModsManager class */
	public Asset getAsset(String assetName);
	
	/** 
	 * Reloads everything. Warning: might not be appropriate as this will reload classes you will most likely have instanced into objects, causing weird
	 * errors if you do this while in a world.
	 */
	public void reload();
	
	public Materials materials();
	public interface Materials {
		
		public Material getMaterialByName(String materialName);
		
		public Iterator<Material> all();

		public Content parent();
		
		public Logger logger();
	}
	
	public Voxels voxels();
	public interface Voxels {
		
		//public Voxel getVoxelById(int voxelId_orVoxelData);

		/** Returns the 'air' voxel ( No voxel data ) */
		public Voxel air();
		
		public Voxel getVoxelByName(String voxelName);
		
		public Iterator<Voxel> all();

		public Content parent();
		
		public VoxelTextures textures();
		public interface VoxelTextures {
			
			public VoxelTexture getVoxelTextureByName(String voxelTextureName);
			
			public Iterator<VoxelTexture> all();

			public Voxels parent();

			public Logger logger();
		}
		
		public VoxelModels models();
		public interface VoxelModels {
			
			public VoxelModel getVoxelModelByName(String voxelModelName);
			
			public Iterator<VoxelModel> all();

			public Voxels parent();
			
			public Logger logger();
		}
		
		public VoxelRenderer getDefaultVoxelRenderer();
		
		public Logger logger();
	}
	
	public ItemsDefinitions items();
	public interface ItemsDefinitions {
		
		//public ItemType getItemTypeById(int itemId);
		
		public ItemDefinition getItemTypeByName(String itemName);
		
		public Iterator<ItemDefinition> all();

		public Content parent();
		
		public Logger logger();
	}
	
	public EntityDefinitions entities();
	public interface EntityDefinitions {
		
		//public EntityType getEntityTypeById(short entityId);
		
		public EntityDefinition getEntityTypeByName(String entityName);
		
		public Iterator<EntityDefinition> all();

		public Content parent();
		
		public Logger logger();
	}
	
	public ParticlesTypes particles();
	public interface ParticlesTypes {
		
		public ParticleTypeHandler getParticleTypeHandlerByName(String string);
		
		//public ParticleTypeHandler getParticleTypeHandlerById(int id);
		
		public Iterator<ParticleTypeHandler> all();

		public Content parent();
		
		public Logger logger();
	}
	
	public PacketDefinitions packets();
	public interface PacketDefinitions {
		
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
		
		/** Contains the parameters stated in a 'generator' section of a .generators config file */
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

		//public Translation getTranslation(String abrigedName);
		
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
	
	//TODO API-ize this
	public AnimationsLibrary getAnimationsLibrary();
	public interface AnimationsLibrary {
		public SkeletalAnimation getAnimation(String name);
		
		public Content parent();
		
		public Logger logger();
	}
	
	public MeshLibrary meshes();
	
	/** General logger about content */
	public Logger logger();
}
