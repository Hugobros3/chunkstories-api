//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.voxel;

public enum VoxelSide {
	/**
	 * Conventions for space in Chunk Stories
	 * 
	 * 1 FRONT z+ x- LEFT 0 X 2 RIGHT x+ 3 BACK z- 4 y+ top X 5 y- bottom
	 */

	// Vanilla mc sides (stairs)
	// 1 = cs_RIGHT / mc_WEST | 3
	// 0 = cs_LEFT / mc_EAST | 0 X 1
	// 2 = cs_FRONT / mc_SOUTH | 2
	// 3 = cs_BACK / mc_NORTH |

	LEFT(-1, 0, 0), FRONT(0, 0, 1), RIGHT(1, 0, 0), BACK(0, 0, -1), TOP(0, 1, 0), BOTTOM(0, -1, 0);

	private static VoxelSide[] oppsiteSide = new VoxelSide[] { RIGHT, BACK, LEFT, FRONT, BOTTOM, TOP };

	/** Cell offsets when looking up a side cell */
	public final int dx, dy, dz;

	VoxelSide(int dx, int dy, int dz) {
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
	}

	public VoxelSide getOppositeSide() {
		return oppsiteSide[this.ordinal()];
	}

	/**
	 * Returns the Chunk Stories side from the minecraft metadata of the following
	 * objects, no top/bottom direction allowed
	 */
	public static VoxelSide getSideMcStairsChestFurnace(int mcSide) {
		switch (mcSide) {
		case 2:
			return BACK;
		case 3:
			return FRONT;
		case 4:
			return RIGHT;
		case 5:
			return LEFT;
		}

		return FRONT;
	}

	/**
	 * Returns the Chunk Stories side from the minecraft metadata of the following
	 * objects, no top/bottom direction allowed
	 */
	public static VoxelSide getSideMcDoor(int mcSide) {
		switch (mcSide) {
		case 0:
			return LEFT;
		case 1:
			return BACK;
		case 2:
			return RIGHT;
		case 3:
			return FRONT;
		}

		return FRONT;
	}

	public enum Corners {
		TOP_FRONT_RIGHT, TOP_FRONT_LEFT, TOP_BACK_RIGHT, TOP_BACK_LEFT, BOTTOM_FRONT_RIGHT, BOTTOM_FRONT_LEFT,
		BOTTOM_BACK_RIGHT, BOTTOM_BACK_LEFT,
	}
}
