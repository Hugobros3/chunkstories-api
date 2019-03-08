//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity

import xyz.chunkstories.api.net.Packet
import xyz.chunkstories.api.world.serialization.StreamTarget

/** A subscriber wants to be kept up-to-date with the latest changes in his
 * environment  */
interface Subscriber : StreamTarget {
    /** @return An unique ID to discriminate it against all others -1 is reserved
     * for server all positive ids are hashmaps of the client username
     */
    val uuid: Long

    /** @return All Entities it is subscribed to
     */
    val subscribedToList: Collection<Entity>

    fun subscribe(entity: Entity): Boolean

    fun unsubscribe(entity: Entity): Boolean

    fun unsubscribeAll()

    fun pushPacket(packet: Packet)
}
