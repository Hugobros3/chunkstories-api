//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.content.mods;

public class ModDownloadFailedException extends ModLoadFailureException
{
	private static final long serialVersionUID = -8878214806405897338L;
	
	String modName;
	String failMessage;
	
	public ModDownloadFailedException(String modName, String failMessage)
	{
		super(null, null);
		this.modName = modName;
		this.failMessage = failMessage;
	}

	public String getMessage()
	{
		return "Mod '"+modName+"' could not be downloaded : "+failMessage;
	}

}
