//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.content

import java.util.Random

val random = ThreadLocal.withInitial { Random() }

/**
 * The legacy custom properties format had a macros system. Most of that got dropped, but the ranged RNG macro is quite useful for listing sound effects
 * so for the time being it may stay in this form of an extention function.
 *
 * An example would be input the string `sounds/footsteps/generic[1-3].ogg`: This method would return any of:
 *
 * * `sounds/footsteps/generic1.ogg`
 * * `sounds/footsteps/generic2.ogg`
 * * `sounds/footsteps/generic3.ogg`
 */
fun String.resolveIntRange(): String {
    var resolved = this

    // Resolves random numbers
    while (resolved.indexOf("[") != -1) {
        val range = resolved.substring(resolved.indexOf("[") + 1, resolved.indexOf("]"))

        val resolvedRandom: String

        // Resolves doubles
        if (range.contains(".")) {
            val minBound = java.lang.Double.parseDouble(range.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
            val maxBound = java.lang.Double.parseDouble(range.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])

            resolvedRandom = "" + (random.get().nextDouble() * (maxBound - minBound) + minBound)
        } else {
            val minBound = Integer.parseInt(range.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
            val maxBound = Integer.parseInt(range.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])

            resolvedRandom = "" + (random.get().nextInt(maxBound - minBound + 1) + minBound)
        }// Resolve integers

        // Removes resolved name from string
        resolved = (resolved.substring(0, resolved.indexOf("[")) + resolvedRandom
                + resolved.substring(resolved.indexOf("]") + 1, resolved.length))
    }

    return resolved
}