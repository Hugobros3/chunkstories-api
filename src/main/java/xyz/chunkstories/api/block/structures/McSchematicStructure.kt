//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.block.structures

import io.xol.enklume.nbt.*
import io.xol.enklume.nbt.NBTFile.CompressionScheme
import org.joml.Vector3i
import xyz.chunkstories.api.content.Asset
import xyz.chunkstories.api.converter.MinecraftBlocksTranslator
import xyz.chunkstories.api.world.cell.PodCellData
import java.io.File
import java.io.IOException
import java.util.zip.GZIPInputStream

// TODO belongs in implementation

fun fromFile(file: File?, translator: MinecraftBlocksTranslator): Prefab? {
    return try {
        val nbtFile = NBTFile(file, CompressionScheme.GZIPPED)
        val root = nbtFile.root
        loadMinecraftSchematicPrefab(root, translator)
    } catch (e: IOException) {
        null
    }
}

fun fromAsset(asset: Asset, translator: MinecraftBlocksTranslator): Prefab? {
    return try {
        val zis = GZIPInputStream(asset.read())
        val root = NBTag.parseInputStream(zis) as NBTCompound
        loadMinecraftSchematicPrefab(root, translator)
    } catch (e: IOException) {
        null
    }
}

fun loadMinecraftSchematicPrefab(root: NBTCompound, translator: MinecraftBlocksTranslator): Prefab {
    val offset = Vector3i()
    val width = (root.getTag("Width") as NBTShort).data.toInt()
    val height = (root.getTag("Height") as NBTShort).data.toInt()
    val length = (root.getTag("Length") as NBTShort).data.toInt()

    if (root.getTag("WEOffsetX") != null && root.getTag("WEOffsetX") is NBTInt) {
        offset.x = (root.getTag("WEOffsetX") as NBTInt).data
        offset.y = (root.getTag("WEOffsetY") as NBTInt).data
        offset.z = (root.getTag("WEOffsetZ") as NBTInt).data
    }
    if (root.getTag("WEOffsetX") != null && root.getTag("WEOffsetX") is NBTShort) {
        offset.x = (root.getTag("WEOffsetX") as NBTShort).data.toInt()
        offset.y = (root.getTag("WEOffsetY") as NBTShort).data.toInt()
        offset.z = (root.getTag("WEOffsetZ") as NBTShort).data.toInt()
    }
    val air = translator.gameInstance.content.blockTypes.air

    val cells = Array(width * height * length) { PrefabCell(-1, -1, -1, PodCellData(air, 0, 0, 0)) }

    val blocks = root.getTag("Blocks") as NBTByteArray
    val blocksdata = root.getTag("Data") as NBTByteArray
    for (z in 0 until length) {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val mcindex = (y * length + z) * width + x
                val mapper = translator.getMapper(blocks.data[mcindex].toInt(), blocksdata.data[mcindex].toInt())
                val mapped = mapper?.output(blocks.data[mcindex].toInt(), blocksdata.data[mcindex]) ?: PodCellData(
                    blockType = air,
                    sunlightLevel = 0,
                    blocklightLevel = 0,
                    extraData = 0
                )
                cells[x + y * width + z * width * height] = PrefabCell(x, y, z, mapped)
            }
        }
    }

    val size = (Vector3i((root.getTag("Width") as NBTShort).data.toInt(), (root.getTag("Height") as NBTShort).data.toInt(), (root.getTag("Length") as NBTShort).data.toInt()))
    return Prefab(size, offset, cells)
}