//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.plugin.commands

/** Can represent the server console, a server player, the local client in case
 * of local plugins, and so on...  */
interface CommandEmitter {
    val name: String

    fun sendMessage(msg: String)

    fun hasPermission(permissionNode: String): Boolean
}
