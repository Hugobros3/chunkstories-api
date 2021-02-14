//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//
package xyz.chunkstories.api.converter

import xyz.chunkstories.api.world.GameInstance
import xyz.chunkstories.api.content.Asset
import xyz.chunkstories.api.block.BlockType
import xyz.chunkstories.api.world.cell.MutableCellData
import java.io.BufferedReader
import java.util.HashMap
import java.lang.Exception
import java.lang.InstantiationException
import java.lang.IllegalAccessException
import java.lang.IllegalArgumentException
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException

// TODO belongs in implementation
class MinecraftBlocksTranslator(val gameInstance: GameInstance, asset: Asset) {
    val MINECRAFT_IDS_CAP = 256
    val MINECRAFT_METADATA_SIZE = 16
    private val mappers = arrayOfNulls<Mapper>(MINECRAFT_IDS_CAP * MINECRAFT_METADATA_SIZE)

    internal enum class Section {
        NONE, MAPPERS, MAPPINGS
    }

    fun getMapper(minecraftID: Int, minecraftMeta: Int): Mapper? {
        return mappers[(minecraftID and 0xFF) * 16 + (minecraftMeta and 0xF)]
    }

    internal inner class TrivialMapper(voxel: BlockType, var metadata: Int) : Mapper(voxel) {
        override fun output(minecraftId: Int, minecraftMeta: Byte, output: MutableCellData) {
            output.blockType = blockType
            output.extraData = metadata
        }
    }

    init {
        val reader = BufferedReader(asset.reader())
        var currentSection = Section.MAPPINGS
        val customMappers: MutableMap<String, Constructor<out Mapper>> = HashMap()
        var line: String
        while (reader.readLine().also { line = it } != null) {
            if (!line.startsWith("#") && line.isNotEmpty()) {
                if (line.startsWith("_")) {
                    val sectionName = line.substring(1)
                    if (sectionName.equals("mappers", ignoreCase = true)) currentSection = Section.MAPPERS else if (sectionName.equals("mappings", ignoreCase = true)) currentSection = Section.MAPPINGS
                    continue
                }
                if (currentSection == Section.MAPPERS) {
                    val splitted = line.split(" ").toTypedArray()
                    if (splitted.size == 2) {
                        val name = splitted[0]
                        val className = splitted[1]
                        try {
                            val mapperClass = gameInstance.content.modsManager.getClassByName(className) as Class<out Mapper>?
                            val mapperConstructor = mapperClass!!.getConstructor(BlockType::class.java)
                            customMappers[name] = mapperConstructor
                        } catch (e: Exception) {
                            println(e)
                        }
                    }
                } else if (currentSection == Section.MAPPINGS) {
                    val splitted = line.split(" ").toTypedArray()
                    if (splitted.size >= 2) {
                        val mc = splitted[0]
                        val cs = splitted[1]
                        val special = if (splitted.size >= 3) splitted[2] else null
                        var minecraftID: Int
                        var minecraftMeta: Byte = -1

                        // Read the minecraft thingie
                        if (mc.contains(":")) {
                            minecraftID = mc.split(":").toTypedArray()[0].toInt()
                            minecraftMeta = mc.split(":").toTypedArray()[1].toByte()
                        } else {
                            minecraftID = mc.toInt()
                        }

                        // Read the cs part
                        var chunkStoriesName: String
                        var chunkStoriesMeta = 0
                        if (cs.contains(":")) {
                            chunkStoriesName = cs.split(":").toTypedArray()[0]
                            chunkStoriesMeta = cs.split(":").toTypedArray()[1].toInt()
                        } else {
                            chunkStoriesName = cs
                        }
                        val voxel = gameInstance.content.blockTypes[chunkStoriesName]
                        if (voxel == null) {
                            println("Error: Voxel '$chunkStoriesName' is nowhere to be found in the loaded content.")
                            println("Skipping line : '$line'.")
                            continue
                        }
                        var mapper: Mapper
                        mapper = if (special == null) {
                            TrivialMapper(voxel, chunkStoriesMeta)
                        } else {
                            val mapperConstructor = customMappers[special]
                            if (mapperConstructor == null) {
                                println("Error: mapper '$special' was not recognised.")
                                println("Skipping line : '$line'.")
                                continue
                            }
                            try {
                                mapperConstructor.newInstance(voxel)
                            } catch (e: InstantiationException) {
                                e.printStackTrace()
                                continue
                            } catch (e: IllegalAccessException) {
                                e.printStackTrace()
                                continue
                            } catch (e: IllegalArgumentException) {
                                e.printStackTrace()
                                continue
                            } catch (e: InvocationTargetException) {
                                e.printStackTrace()
                                continue
                            }
                        }

                        // Fill the relevant cases
                        if (minecraftMeta.toInt() == -1) {
                            for (i in 0..15) {
                                mappers[minecraftID * 16 + i] = mapper
                            }
                        } else {
                            mappers[minecraftID * 16 + minecraftMeta] = mapper
                        }
                    }
                }
            }
        }
        reader.close()
    }
}