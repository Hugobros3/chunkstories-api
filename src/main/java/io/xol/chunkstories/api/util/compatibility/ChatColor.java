//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.util.compatibility;

import io.xol.chunkstories.api.math.HexTools;

/**
 * Compatibility layer for older Bukkit-based plugins ports
 */
public enum ChatColor
{
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
	RESET("#FFFFFF"),
	;

	String hex;
	
	ChatColor(String hex)
	{
		this.hex = hex;
	}

	public String toString()
	{
		if (this.name().equals("MAGIC"))
		{
			String color = "";
			color += HexTools.intToHex((int) (Math.random() * 255));
			color += HexTools.intToHex((int) (Math.random() * 255));
			color += HexTools.intToHex((int) (Math.random() * 255));
			return "#" + color;
		}
		return hex;
	}

	public static String stripColor(String string)
	{
		return string;
	}
}
