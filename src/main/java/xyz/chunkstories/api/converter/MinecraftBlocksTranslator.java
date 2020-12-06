//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import xyz.chunkstories.api.content.Asset;
import xyz.chunkstories.api.converter.mappings.Mapper;
import xyz.chunkstories.api.voxel.Voxel;
import xyz.chunkstories.api.world.GameInstance;
import xyz.chunkstories.api.world.cell.FutureCell;

//TODO kotlinize
/** Maps minecraft ids to chunkstories's */
public class MinecraftBlocksTranslator {
	public final int MINECRAFT_IDS_CAP = 256;
	public final int MINECRAFT_METADATA_SIZE = 16;

	private Mapper[] mappers = new Mapper[MINECRAFT_IDS_CAP * MINECRAFT_METADATA_SIZE];

	public GameInstance getGameInstance() {
		return gameInstance;
	}

	enum Section
	{
		NONE, MAPPERS, MAPPINGS,
	}

	private GameInstance gameInstance;

	public MinecraftBlocksTranslator(GameInstance gameInstance, Asset asset) throws IOException {
		this.gameInstance = gameInstance;
		BufferedReader reader = new BufferedReader(asset.reader());

		Section currentSection = Section.MAPPINGS;
		Map<String, Constructor<? extends Mapper>> customMappers = new HashMap<>();

		String line;
		while ((line = reader.readLine()) != null) {
			if (!line.startsWith("#") && line.length() > 0) {

				if (line.startsWith("_")) {
					String sectionName = line.substring(1);
					if (sectionName.equalsIgnoreCase("mappers"))
						currentSection = Section.MAPPERS;
					else if (sectionName.equalsIgnoreCase("mappings"))
						currentSection = Section.MAPPINGS;

					continue;
				}

				if (currentSection == Section.MAPPERS) {
					String[] splitted = line.split(" ");
					if (splitted.length == 2) {
						String name = splitted[0];
						String className = splitted[1];

						try {
							@SuppressWarnings("unchecked")
							Class<? extends Mapper> mapperClass = (Class<? extends Mapper>) gameInstance.getContent().getModsManager().getClassByName(className);
							Constructor<? extends Mapper> mapperConstructor = mapperClass.getConstructor(Voxel.class);
							customMappers.put(name, mapperConstructor);
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				} else if (currentSection == Section.MAPPINGS) {
					String[] splitted = line.split(" ");
					if (splitted.length >= 2) {
						String mc = splitted[0];
						String cs = splitted[1];

						String special = splitted.length >= 3 ? splitted[2] : null;

						int minecraftID;
						byte minecraftMeta = -1;

						// Read the minecraft thingie
						if (mc.contains(":")) {
							minecraftID = Integer.parseInt(mc.split(":")[0]);
							minecraftMeta = Byte.parseByte(mc.split(":")[1]);
						} else {
							minecraftID = Integer.parseInt(mc);
						}

						// Read the cs part
						String chunkStoriesName = null;
						int chunkStoriesMeta = 0;
						if (cs.contains(":")) {
							chunkStoriesName = cs.split(":")[0];
							chunkStoriesMeta = Integer.parseInt(cs.split(":")[1]);
						} else {
							chunkStoriesName = cs;
						}

						Voxel voxel = gameInstance.getContent().getVoxels().getVoxel(chunkStoriesName);
						if (voxel == null) {
							System.out.println("Error: Voxel '" + chunkStoriesName + "' is nowhere to be found in the loaded content.");
							System.out.println("Skipping line : '" + line + "'.");
							continue;
						}

						Mapper mapper;
						if (special == null) {
							mapper = new TrivialMapper(voxel, chunkStoriesMeta);
						} else {

							Constructor<? extends Mapper> mapperConstructor = customMappers.get(special);

							if (mapperConstructor == null) {
								System.out.println("Error: mapper '" + special + "' was not recognised.");
								System.out.println("Skipping line : '" + line + "'.");
								continue;
							}

							try {
								mapper = mapperConstructor.newInstance(voxel);
							} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
								e.printStackTrace();
								continue;
							}
						}

						// Fill the relevant cases
						if (minecraftMeta == -1) {
							for (int i = 0; i < 16; i++) {
								mappers[minecraftID * 16 + i] = mapper;
							}
						} else {
							mappers[minecraftID * 16 + minecraftMeta] = mapper;
						}
					}
				}
			}
		}

		reader.close();
	}

	public Mapper getMapper(int minecraftID, byte minecraftMeta) {
		minecraftID &= 0xFF;
		minecraftMeta &= 0xF;

		return mappers[minecraftID * 16 + minecraftMeta];
	}

	class TrivialMapper extends Mapper {

		int meta;

		TrivialMapper(Voxel voxel, int meta) {
			super(voxel);
			this.meta = meta;
		}

		@Override
		public void output(int minecraftId, byte minecraftMeta, FutureCell future) {
			future.setVoxel(voxel);
			future.setMetaData(meta);
		}
	}
}
