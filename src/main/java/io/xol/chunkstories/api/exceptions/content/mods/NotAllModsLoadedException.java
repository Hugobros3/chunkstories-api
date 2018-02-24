//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.content.mods;

import java.util.Collection;

public class NotAllModsLoadedException extends Exception
{
	Collection<ModLoadFailureException> failed;
	
	public NotAllModsLoadedException(Collection<ModLoadFailureException> failed)
	{
		this.failed = failed;
	}
	
	public String getMessage()
	{
		String message = "Some mods failed to load : \n";
		
		for(ModLoadFailureException e : failed)
			message += e.getMessage() + "\n";
		
		return message;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5136184783162902334L;

}
