//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.plugin;

import java.io.File;
import java.util.Collection;

import io.xol.chunkstories.api.GameContext;
import io.xol.chunkstories.api.exceptions.plugins.PluginCreationException;
import io.xol.chunkstories.api.plugin.commands.PluginCommand;

public interface PluginInformation
{
	public String getName();

	public String getPluginVersion();

	public String getAuthor();

	public PluginType getPluginType();

	public ChunkStoriesPlugin createInstance(GameContext pluginExecutionContext) throws PluginCreationException;

	public Collection<PluginCommand> getCommands();
	
	public File getDirectory();
	
	public enum PluginType
	{
		UNIVERSAL, CLIENT_ONLY, MASTER;
	}
}