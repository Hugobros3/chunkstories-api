//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.voxel.structures;

import java.io.File;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import xyz.chunkstories.api.content.Asset;
import xyz.chunkstories.api.converter.MinecraftBlocksTranslator;
import xyz.chunkstories.api.voxel.Voxel;
import xyz.chunkstories.api.world.DummyWorld;
import xyz.chunkstories.api.world.cell.AbstractCell;
import xyz.chunkstories.api.world.cell.FutureCell;
import io.xol.enklume.nbt.NBTByteArray;
import io.xol.enklume.nbt.NBTCompound;
import io.xol.enklume.nbt.NBTFile;
import io.xol.enklume.nbt.NBTFile.CompressionScheme;
import io.xol.enklume.nbt.NBTInt;
import io.xol.enklume.nbt.NBTShort;
import io.xol.enklume.nbt.NBTag;

import javax.annotation.Nullable;

public class McSchematicStructure extends Structure {

	@Nullable
	public static McSchematicStructure fromFile(File file, MinecraftBlocksTranslator translator) {
		try {
			NBTFile nbtFile = new NBTFile(file, CompressionScheme.GZIPPED);
			NBTCompound root = nbtFile.getRoot();
			return new McSchematicStructure(root, translator);

		} catch (IOException e) {
			return null;
		}
	}

	@Nullable
	public static McSchematicStructure fromAsset(Asset asset, MinecraftBlocksTranslator translator) {
		try {
			GZIPInputStream zis = new GZIPInputStream(asset.read());
			NBTCompound root = (NBTCompound) NBTag.parseInputStream(zis);
			return new McSchematicStructure(root, translator);

		} catch (IOException e) {
			return null;
		}
	}

	public McSchematicStructure(NBTCompound root, MinecraftBlocksTranslator translator) {
		super(((NBTShort) root.getTag("Width")).data, ((NBTShort) root.getTag("Height")).data, ((NBTShort) root.getTag("Length")).data);

		int width = ((NBTShort) root.getTag("Width")).data;
		int height = ((NBTShort) root.getTag("Height")).data;
		int length = ((NBTShort) root.getTag("Length")).data;

		// Vector3i offset = new Vector3i();
		if (root.getTag("WEOffsetX") != null && root.getTag("WEOffsetX") instanceof NBTInt) {
			offset.x = ((NBTInt) root.getTag("WEOffsetX")).data;
			offset.y = ((NBTInt) root.getTag("WEOffsetY")).data;
			offset.z = ((NBTInt) root.getTag("WEOffsetZ")).data;
		}
		if (root.getTag("WEOffsetX") != null && root.getTag("WEOffsetX") instanceof NBTShort) {
			offset.x = ((NBTShort) root.getTag("WEOffsetX")).data;
			offset.y = ((NBTShort) root.getTag("WEOffsetY")).data;
			offset.z = ((NBTShort) root.getTag("WEOffsetZ")).data;
		}

		NBTByteArray blocks = (NBTByteArray) root.getTag("Blocks");
		NBTByteArray blocksdata = (NBTByteArray) root.getTag("Data");

		Voxel air = translator.getContext().getContent().getVoxels().getAir();

		data = new AbstractCell[width * height * length];
		FutureCell future = new FutureCell(new DummyWorld(), 0, 0, 0, air, 0, 0, 0);

		for (int z = 0; z < length; z++) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int mcindex = (y * length + z) * width + x;
					translator.getMapper(blocks.data[mcindex], blocksdata.data[mcindex]).output(blocks.data[mcindex], blocksdata.data[mcindex], future);

					StructureCell cell = new StructureCell(x, y, z, air, 0, 0, 0);
					cell.setVoxel(future.getVoxel());
					cell.setMetaData(future.getMetaData());

					data[x + y * width + z * width * height] = cell;
				}
			}
		}
	}

}
