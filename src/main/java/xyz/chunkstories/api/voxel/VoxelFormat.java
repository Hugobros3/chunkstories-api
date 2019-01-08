//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.voxel;

/**
 * <p>
 * VoxelFormat 2.0 (changed in API 106)
 * </p>
 * 
 * <p>
 * This helper defines the game's saving format<br/>
 * It stores all voxels in 32-bit signed ( Java won't allow unsigned :c )
 * ints<br/>
 * The ints are composed as : 0xMMBSIIII<br/>
 * <ul>
 * <li>0->15 16-bit block<b>I</b>D, allowing for 65536 different blocks
 * types</li>
 * <li>16->19 4-bit <b>S</b>unlight</li>
 * <li>20->23 4-bit <b>B</b>locklight</li>
 * <li>24->31 8-bit <b>M</b>eta data</li>
 * </ul>
 * </p>
 */
public class VoxelFormat {
	// These may help you
	public static final int idMask = 0x0000FFFF;
	public static final int sunlightMask = 0x000F0000;
	public static final int blocklightMask = 0x00F00000;
	public static final int metaMask = 0xFF000000;

	public static final int idBitshift = 0x0;
	public static final int sunBitshift = 0x10;
	public static final int blockBitshift = 0x14;
	public static final int metaBitshift = 0x18;

	public final static int format(int blockID, int metadata, int sunlight, int blocklight) {
		blockID &= 0xFFFF;
		sunlight &= 0xF;
		blocklight &= 0xF;
		metadata &= 0xFF;

		return blockID | metadata << 0x18 | sunlight << 0x10 | blocklight << 0x14;
	}

	public final static int id(int src) {
		return src & 0xFFFF;
	}

	public final static int changeId(int src, int id) {
		return src & 0xFFFF0000 | id;
	}

	public final static int meta(int src) {
		return (src >>> 0x18) & 0xFF;
	}

	public final static int changeMeta(int src, int meta) {
		return src & 0x00FFFFFF | meta << 0x18;
	}

	public final static int sunlight(int src) {
		return (src >>> 0x10) & 0xF;
	}

	public final static int changeSunlight(int src, int sunlight) {
		return src & 0xFFF0FFFF | sunlight << 0x10;
	}

	public final static int blocklight(int src) {
		return (src >>> 0x14) & 0xF;
	}

	public final static int changeBlocklight(int src, int blocklight) {
		return src & 0xFF0FFFFF | blocklight << 0x14;
	}
}
