//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.plugin.commands

interface CommandHandler {
    fun handleCommand(emitter: CommandEmitter, command: Command, arguments: Array<String>): Boolean
}
