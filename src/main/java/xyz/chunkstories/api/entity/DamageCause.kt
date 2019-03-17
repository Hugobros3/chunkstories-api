//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity

interface DamageCause {
    val name: String

    /** How many milliseconds should the target be set invulnerable after an
     * attack  */
    val cooldownInMs: Long
        get() = 0

    companion object {

        val DAMAGE_CAUSE_FALL: DamageCause = object : DamageCause {

            override val name: String
                get() = "damage.fall"

        }
    }
}
