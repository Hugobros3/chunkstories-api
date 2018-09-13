//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.content

import java.util.Random

/** Describes a configuration section usually loaded from a configuration file.
 * Has a name, and contains properties you can query.  */
open class Definition(val name: String, private val properties : Map<String, String>) {
    /** Resolves a property from the arguments defined in the file. Returns null if
     * it was not.  */
    fun resolveProperty(propertyName: String): String? = resolvePropertyInternal(propertyName)

    /** Do the same as above but provides a default fallback value instead of null,
     * in case said property isn't defined.  */
    fun resolveProperty(propertyName: String, defaultValue: String): String = resolvePropertyInternal(propertyName) ?: defaultValue

    /** Properties can be a bit more complicated than plain text, they can include each other, generate random numbers,
     * and include the name of the definition as a shorthand. */
    private fun resolvePropertyInternal(propertyName: String): String? {
        var resolved: String? = properties[propertyName] ?: return null

        // Resolves inclusions
        while (resolved!!.indexOf("<") != -1) {
            val propertyToInclude = resolved.substring(resolved.indexOf("<") + 1, resolved.indexOf(">"))

            // Prevents resolving itself
            val propertyResolved = if (propertyToInclude == propertyName) "" else resolveProperty(propertyToInclude)

            // Removes resolved name from string
            resolved = (resolved.substring(0, resolved.indexOf("<")) + propertyResolved
                    + resolved.substring(resolved.indexOf(">") + 1, resolved.length))
        }

        // Resolves random numbers
        while (resolved!!.indexOf("[") != -1) {
            val range = resolved.substring(resolved.indexOf("[") + 1, resolved.indexOf("]"))

            val resolvedRandom: String

            // Resolves doubles
            if (range.contains(".")) {
                val minBound = java.lang.Double.parseDouble(range.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
                val maxBound = java.lang.Double.parseDouble(range.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])

                resolvedRandom = "" + (random.nextDouble() * (maxBound - minBound) + minBound)
            } else {
                val minBound = Integer.parseInt(range.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
                val maxBound = Integer.parseInt(range.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])

                resolvedRandom = "" + (random.nextInt(maxBound - minBound + 1) + minBound)
            }// Resolve integers

            // Removes resolved name from string
            resolved = (resolved.substring(0, resolved.indexOf("[")) + resolvedRandom
                    + resolved.substring(resolved.indexOf("]") + 1, resolved.length))
        }

        // Replace material name
        resolved = resolved.replace("<name>", name);

        // Alternative syntax
        resolved = resolved.replace("~", name);

        return resolved
    }

    companion object {
        private val random = Random()
    }

    operator fun get(propertyName: String) = resolveProperty(propertyName)

    val allProperties = properties.keys.toList()
}
