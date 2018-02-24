//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.voxel;

import java.util.Random;

/**
 *  <p>VoxelFormat 2.0 (changed in API 106)</p>
 *  
 *  <p>
 *	This helper defines the game's saving format<br/>
 *	It stores all voxels in 32-bit signed ( Java won't allow unsigned :c ) ints<br/>
 *	The ints are composed as : 0xMMBSIIII<br/>
 *	<ul>
 *		<li>0->15  16-bit block<b>I</b>D, allowing for 65536 different blocks types</li>
 *		<li>16->19  4-bit <b>S</b>unlight</li>
 *		<li>20->23  4-bit <b>B</b>locklight</li>
 *		<li>24->31  8-bit <b>M</b>eta data</li>
 *	</ul>
 *	</p>
*/
public class VoxelFormat
{
	//These may help you
	public static final int idMask = 0x0000FFFF;
	public static final int sunlightMask = 0x000F0000;
	public static final int blocklightMask = 0x00F00000;
	public static final int metaMask = 0xFF000000;

	public static final int idBitshift = 0x0;
	public static final int sunBitshift = 0x10;
	public static final int blockBitshift = 0x14;
	public static final int metaBitshift = 0x18;
	
	/** Junit tests are for hipsters, if this runs it's probably cool */
	public static void main(String a[])
	{
		// Demo-debug
		int data = format(65535, 255, 0, 0);
		data = changeId(data, 15);
		data = changeSunlight(data, 3);
		data = changeBlocklight(data, 7);
		
		System.out.println(data);
		System.out.println("BlockID : " + id(data) + " Meta : " + meta(data) + " Sun : " + sunlight(data) + " Block : " + blocklight(data));

		Random random = new Random();
		int tests = 100000000;
		for(int test = 0; test < tests; test++) {
			int blockId = random.nextInt(65536);
			int blockLight = random.nextInt(16);
			int sunLight = random.nextInt(16);
			int metaData = random.nextInt(256);
			
			int formatted = format(blockId, metaData, sunLight, blockLight);
			if(blockId == id(formatted) && blockLight == blocklight(formatted) && sunLight == sunlight(formatted) && metaData == meta(formatted)) {
				//Ok good
			}
			else {
				System.out.println(formatted);
				System.out.println(blockId + " vs " + id(formatted));
				System.out.println(blockLight + " vs " + blocklight(formatted));
				System.out.println(sunLight + " vs " + sunlight(formatted));
				System.out.println(metaData + " vs " + meta(formatted));
				throw new RuntimeException("Test failed.");
			}

			int blockId2 = random.nextInt(65536);
			int blockLight2 = random.nextInt(16);
			int sunLight2 = random.nextInt(16);
			int metaData2 = random.nextInt(256);
			
			int blockIdExpected = blockId;
			int blockLightExpected = blockLight;
			int sunLightExpected = sunLight;
			int metaDataExpected = metaData;
			
			blockIdExpected = blockId2;
			formatted = changeId(formatted, blockIdExpected);
			
			if(!(blockIdExpected == id(formatted) && blockLightExpected == blocklight(formatted) && sunLightExpected == sunlight(formatted) && metaDataExpected == meta(formatted)))
				throw new RuntimeException("Test failed.");
			
			metaDataExpected = metaData2;
			formatted = changeMeta(formatted, metaDataExpected);
			
			if(!(blockIdExpected == id(formatted) && blockLightExpected == blocklight(formatted) && sunLightExpected == sunlight(formatted) && metaDataExpected == meta(formatted)))
				throw new RuntimeException("Test failed.");
			
			sunLightExpected = sunLight2;
			formatted = changeSunlight(formatted, sunLightExpected);
			
			if(!(blockIdExpected == id(formatted) && blockLightExpected == blocklight(formatted) && sunLightExpected == sunlight(formatted) && metaDataExpected == meta(formatted)))
				throw new RuntimeException("Test failed.");
			
			blockLightExpected = blockLight2;
			formatted = changeBlocklight(formatted, blockLightExpected);
			
			if(!(blockIdExpected == id(formatted) && blockLightExpected == blocklight(formatted) && sunLightExpected == sunlight(formatted) && metaDataExpected == meta(formatted)))
				throw new RuntimeException("Test failed.");
		}
		
		System.out.println("Ran through "+tests+" runs of testing just fine.");
	}

	public final static int format(int blockID, int metadata, int sunlight, int blocklight)
	{
		blockID &= 0xFFFF;
		sunlight &= 0xF;
		blocklight &= 0xF;
		metadata &= 0xFF;

		return blockID | metadata << 0x18 | sunlight << 0x10 | blocklight << 0x14;
	}

	public final static int id(int src)
	{
		return src & 0xFFFF;
	}

	public final static int changeId(int src, int id)
	{
		return src & 0xFFFF0000 | id;
	}

	public final static int meta(int src)
	{
		return (src >>> 0x18) & 0xFF;
	}

	public final static int changeMeta(int src, int meta)
	{
		return src & 0x00FFFFFF | meta << 0x18;
	}

	public final static int sunlight(int src)
	{
		return (src >>> 0x10) & 0xF;
	}

	public final static int changeSunlight(int src, int sunlight)
	{
		return src & 0xFFF0FFFF | sunlight << 0x10;
	}

	public final static int blocklight(int src)
	{
		return (src >>> 0x14) & 0xF;
	}

	public final static int changeBlocklight(int src, int blocklight)
	{
		return src & 0xFF0FFFFF | blocklight << 0x14;
	}
}
