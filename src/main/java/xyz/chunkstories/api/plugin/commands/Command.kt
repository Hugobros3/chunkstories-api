//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.plugin.commands

import java.util.HashSet

/** A command is a special token the game uses to call the execution of a
 * certain tool  */
open class Command(val name: String) {
    protected val aliases: MutableSet<String> = HashSet()

    var handler: CommandHandler? = null

    init {
        this.aliases.add(name)
    }

    fun addAlias(alias: String) {
        this.aliases.add(alias)
    }

    override fun equals(o: Any?): Boolean {
        return if (o is String) o == name else (o as Command).name == name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    fun aliases(): Collection<String> {
        return aliases
    }
}
