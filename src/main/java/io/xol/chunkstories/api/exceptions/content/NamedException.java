//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.content;

public abstract class NamedException extends Exception
{
	private final String message;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7631843635756291811L;
	
	public NamedException(String message)
	{
		this.message = message;
	}
	
	@Override
	public String getMessage()
	{
		return message;
	}

}
