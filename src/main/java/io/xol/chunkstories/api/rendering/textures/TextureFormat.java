//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.textures;

import static io.xol.chunkstories.api.rendering.textures.TextureFormat.GL_Abstraction.*;

public enum TextureFormat
{
	/** Classic OpenGL format: 8-bit per component, 4 components 32-bit per pixel. */
	RGBA_8BPP(GL_RGBA, GL_RGBA, GL_UNSIGNED_BYTE, 4),
	
	/** "HDR" format for the default renderbuffer, uses 3 floating-point component */
	RGB_HDR(GL_R11F_G11F_B10F, GL_RGB, GL_FLOAT, 4),

	/** High precision depth format (32-bit) */
	DEPTH_RENDERBUFFER(GL_DEPTH_COMPONENT32, GL_DEPTH_COMPONENT, GL_FLOAT, 4),
	
	/** Lesser precision depth format (24-bit) */
	DEPTH_SHADOWMAP(GL_DEPTH_COMPONENT24, GL_DEPTH_COMPONENT, GL_FLOAT, 3),
	
	/** One component of 32-bit floating point (very precise) */
	RED_32F(GL_R32F, GL_RED, GL_FLOAT, 4),
	
	/** One component of 16-bit integers */
	RED_16I(GL_R16UI, GL_RED_INTEGER, GL_INT, 2),
	
	/** One component of 16-bit float */
	RED_16F(GL_R16F, GL_RED, GL_FLOAT, 2),

	/** 10 bits for RGB, two for alpha. Normalized. */
	RGBA_3x10_2(GL_RGB10_A2, GL_RGBA, GL_UNSIGNED_BYTE, 4),
	
	/** High precision HDR format, 16-bit per component, quite heavy.*/
	RGBA_16F(GL_RGBA16F, GL_RGBA, GL_FLOAT, 16),
	
	/** Very high precision HDR format, fully fat 32-bit per component, slow ! */
	RGBA_32F(GL_RGBA32F, GL_RGBA, GL_FLOAT, 16),

	/** Two 8-bit components (unsigned, normalized) */
	RGB_8(GL_RGB8, GL_RGB, GL_UNSIGNED_BYTE, 3),
	
	/** Two 8-bit components (unsigned, normalized) */
	RG_8(GL_RG8, GL_RG, GL_UNSIGNED_BYTE, 2),

	/** A single 8-bit component (unsigned, normalized) */
	RED_8(GL_R8, GL_RG, GL_UNSIGNED_BYTE, 1),
	
	/** A single 8-bit component (unsigned) */
	RED_8UI(GL_R8UI, GL_RED_INTEGER, GL_INT, 1),
	;

	//Removes LWJGL dependency
	interface GL_Abstraction {
		public static final int GL_RGBA = 0x1908;
		public static final int GL_UNSIGNED_BYTE = 0x1401;
		public static final int GL_R11F_G11F_B10F = 0x8c3a;
		public static final int GL_RGB = 0x1907;
		public static final int GL_FLOAT = 0x1406;
		public static final int GL_DEPTH_COMPONENT = 0x1902;
		public static final int GL_DEPTH_COMPONENT24 = 0x81a6;
		public static final int GL_DEPTH_COMPONENT32 = 0x81a7;
		public static final int GL_R32F = 0x822e;
		public static final int GL_RED = 0x1903;
		public static final int GL_RGB10_A2 = 0x8059;
		public static final int GL_RGBA32F = 0x8814;
		public static final int GL_RGBA16F = 34842;
		public static final int GL_R16UI = 0x8234;
		public static final int GL_R16F = 33325;
		public static final int GL_R8UI = 0x8232;
		public static final int GL_R8 = 33321;
		public static final int GL_RG8 = 33323;
		public static final int GL_RG = 33319;
		public static final int GL_RGB8 = 32849;
		//public static final int GL_R8 = ;
		public static final int GL_INT = 0x1404;
		public static final int GL_RED_INTEGER = 0x8d94;
	}
	
	TextureFormat(int internalFormat, int format, int type, int bytesUsed)
	{
		this.internalFormat = internalFormat;
		this.format = format;
		this.type = type;
		this.bytesUsed = bytesUsed;
	}

	private final int internalFormat;
	private final int format;
	private final int type;
	private final int bytesUsed;

	public int getInternalFormat()
	{
		return internalFormat;
	}
	
	public int getFormat()
	{
		return format;
	}
	
	public int getType()
	{
		return type;
	}

	public int getBytesPerTexel()
	{
		return bytesUsed;
	}
}