//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions;

public class UndefinedItemTypeException extends ItemException {
	int itemId;

	public UndefinedItemTypeException(int itemId) {
		this.itemId = itemId;
	}

	@Override
	public String getMessage() {
		return "Unknown ItemType by id=" + itemId;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3629935518207497054L;

}
