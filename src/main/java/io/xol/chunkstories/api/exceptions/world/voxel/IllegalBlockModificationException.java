package io.xol.chunkstories.api.exceptions.world.voxel;

import io.xol.chunkstories.api.exceptions.world.VoxelException;
import io.xol.chunkstories.api.world.VoxelContext;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/**
 * Used to restrict block operations, is thrown when a forbidden action is being attempted
 */
public class IllegalBlockModificationException extends VoxelException
{
	private final String message;

	private static final long serialVersionUID = -1717494086092644106L;
	
	public IllegalBlockModificationException(VoxelContext context, String message)
	{
		super(context);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
