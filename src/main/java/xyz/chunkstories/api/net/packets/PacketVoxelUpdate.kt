//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets

import xyz.chunkstories.api.client.net.ClientPacketsProcessor
import xyz.chunkstories.api.content.json.stringSerialize
import xyz.chunkstories.api.content.json.toJson
import xyz.chunkstories.api.exceptions.world.WorldException
import xyz.chunkstories.api.net.PacketDestinator
import xyz.chunkstories.api.net.PacketReceptionContext
import xyz.chunkstories.api.voxel.VoxelFormat
import xyz.chunkstories.api.voxel.components.VoxelComponent
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.chunk.ChunkCell
import xyz.chunkstories.api.net.PacketSender
import xyz.chunkstories.api.net.PacketSendingContext
import xyz.chunkstories.api.net.PacketWorld
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

/** Describes a voxel change, including to the VoxelComponents  */
class PacketVoxelUpdate : PacketWorld {

    private var context: ChunkCell? = null
    private var componentToUpdate: VoxelComponent? = null

    constructor(world: World) : super(world) {}

    constructor(context: ChunkCell) : super(context.world) {
        this.context = context
    }

    constructor(context: ChunkCell, componentToUpdate: VoxelComponent?) : super(context.world) {
        this.context = context
        this.componentToUpdate = componentToUpdate
    }

    @Throws(IOException::class)
    override fun send(destinator: PacketDestinator, out: DataOutputStream, sendingContext: PacketSendingContext) {
        out.writeInt(context!!.x)
        out.writeInt(context!!.y)
        out.writeInt(context!!.z)
        out.writeInt(context!!.data)

        if (componentToUpdate == null) {
            for ((key, value) in context!!.components.allVoxelComponents) {
                val serializedData = value.serialize() ?: continue
                out.writeByte(0x01.toByte().toInt())
                out.writeUTF(key)
                out.writeUTF(serializedData.stringSerialize())
            }
        } else {
            val serializedData = componentToUpdate!!.serialize()
            if(serializedData != null) {
                out.writeByte(0x01.toByte().toInt())
                out.writeUTF(componentToUpdate!!.name)
                out.writeUTF(serializedData.stringSerialize())
            }
        }

        // No further information
        out.writeByte(0x00.toByte().toInt())
    }

    @Throws(IOException::class)
    override fun process(sender: PacketSender, dis: DataInputStream, processor: PacketReceptionContext) {
        if (processor is ClientPacketsProcessor) {

            val x = dis.readInt()
            val y = dis.readInt()
            val z = dis.readInt()
            val data = dis.readInt()

            val voxel = world.contentTranslator.getVoxelForId(VoxelFormat.id(data))

            var nextComponent = dis.readByte()

            try {
                val context = processor.world.chunksManager.getChunkWorldCoordinates(x, y, z)!!.poke(x, y, z, voxel, VoxelFormat.sunlight(data),
                        VoxelFormat.blocklight(data), VoxelFormat.meta(data), null)

                while (nextComponent.toInt() != 0) {
                    val componentName = dis.readUTF()
                    context.components.getVoxelComponent(componentName)!!.deserialize(dis.readUTF().toJson())
                    //context.components.getVoxelComponent(componentName)!!.pull(sender, dis)
                    nextComponent = dis.readByte()
                }

            } catch (e: WorldException) {
                // Maybe the world wasn't ready ?
                // Edge case: what happens we receive an update for a chunk we haven't received
                // the data yet ?
                // The best option would be to delay the process but this is complicated for a
                // rare instance
                // Maybe one day
            }

        } else {
            // Fail hard
        }
    }
}
