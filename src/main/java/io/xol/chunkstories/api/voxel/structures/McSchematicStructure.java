package io.xol.chunkstories.api.voxel.structures;

import java.io.File;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import io.xol.chunkstories.api.content.Asset;
import io.xol.chunkstories.api.converter.MinecraftBlocksTranslator;
import io.xol.chunkstories.api.world.DummyWorld;
import io.xol.chunkstories.api.world.cell.Cell;
import io.xol.chunkstories.api.world.cell.FutureCell;
import io.xol.enklume.nbt.NBTByteArray;
import io.xol.enklume.nbt.NBTCompound;
import io.xol.enklume.nbt.NBTFile;
import io.xol.enklume.nbt.NBTFile.CompressionScheme;
import io.xol.enklume.nbt.NBTInt;
import io.xol.enklume.nbt.NBTShort;
import io.xol.enklume.nbt.NBTag;

public class McSchematicStructure extends Structure {

	public static McSchematicStructure fromFile(File file, MinecraftBlocksTranslator translator) {
		try {
			NBTFile nbtFile = new NBTFile(file, CompressionScheme.GZIPPED);
			NBTCompound root = nbtFile.getRoot();
			return new McSchematicStructure(root, translator);
			
		} catch (IOException e) {
			return null;
		}
	}
	
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
		super(((NBTShort)root.getTag("Width")).data, ((NBTShort)root.getTag("Height")).data, ((NBTShort)root.getTag("Length")).data);
		
		int width = ((NBTShort)root.getTag("Width")).data;
		int height = ((NBTShort)root.getTag("Height")).data;
		int length = ((NBTShort)root.getTag("Length")).data;
		
		//Vector3i offset = new Vector3i();
		if(root.getTag("WEOffsetX") != null && root.getTag("WEOffsetX") instanceof NBTInt) {
			offset.x = ((NBTInt)root.getTag("WEOffsetX")).data;
			offset.y = ((NBTInt)root.getTag("WEOffsetY")).data;
			offset.z = ((NBTInt)root.getTag("WEOffsetZ")).data;
		}
		if(root.getTag("WEOffsetX") != null && root.getTag("WEOffsetX") instanceof NBTShort) {
			offset.x = ((NBTShort)root.getTag("WEOffsetX")).data;
			offset.y = ((NBTShort)root.getTag("WEOffsetY")).data;
			offset.z = ((NBTShort)root.getTag("WEOffsetZ")).data;
		}
		
		NBTByteArray blocks = (NBTByteArray) root.getTag("Blocks");
		NBTByteArray blocksdata = (NBTByteArray) root.getTag("Data");
		
		data = new Cell[width * height * length];
		FutureCell future = new FutureCell(new DummyWorld(),0,0,0,null,0,0,0);
		
		for(int z = 0; z < length; z++) {
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					int mcindex = (y * length + z) * width + x;
					translator.getMapper(blocks.data[mcindex], blocksdata.data[mcindex]).output(blocks.data[mcindex], blocksdata.data[mcindex], future);

					StructureCell cell = new StructureCell(x, y, z, null, 0, 0, 0);
					cell.setVoxel(future.getVoxel());
					cell.setMetaData(future.getMetaData());
					
					data[x + y * width + z * width * height] = cell;
				}
			}
		}
	}

}
