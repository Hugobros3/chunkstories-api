//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.world.voxel;

import xyz.chunkstories.api.exceptions.world.VoxelException;
import xyz.chunkstories.api.world.cell.CellData;

/** Used to restrict block operations, is thrown when a forbidden action is
 * being attempted */
public class IllegalBlockModificationException extends VoxelException {
	private final String message;

	private static final long serialVersionUID = -1717494086092644106L;

	public IllegalBlockModificationException(CellData context, String message) {
		super(context);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
