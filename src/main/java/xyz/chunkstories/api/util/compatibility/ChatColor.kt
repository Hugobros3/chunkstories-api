//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//
package xyz.chunkstories.api.util.compatibility

import xyz.chunkstories.api.math.intToHex

/**
 * Compatibility layer for older Bukkit-based plugins ports
 */
enum class ChatColor(var hex: String) {
    BLACK("#000000"),
    WHITE("#FFFFFF"),
    LIGHT_PURPLE("#FF55FF"),
    DARK_PURPLE("#AA00AA"),
    GOLD("#FFAA00"),
    BLUE("#5555FF"),
    AQUA("#55FFFF"),
    RED("#FF5555"),
    GREEN("#55FF55"),
    GRAY("#AAAAAA"),
    DARK_BLUE("#0000AA"),
    DARK_AQUA("#00AAAA"),
    DARK_RED("#AA0000"),
    DARK_GREEN("#00AA00"),
    DARK_GRAY("#555555"),
    YELLOW("#FFFF55"),
    ITALIC(""),
    BOLD(""),
    UNDERLINE(""),
    MAGIC(""),
    RESET("#FFFFFF");

    override fun toString(): String {
        if (name == "MAGIC") {
            var color = ""
            color += intToHex((Math.random() * 255).toInt())
            color += intToHex((Math.random() * 255).toInt())
            color += intToHex((Math.random() * 255).toInt())
            return "#$color"
        }
        return hex
    }
}