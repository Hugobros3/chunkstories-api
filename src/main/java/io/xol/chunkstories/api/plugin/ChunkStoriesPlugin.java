//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.plugin;

import java.io.File;

import io.xol.chunkstories.api.GameContext;

public abstract class ChunkStoriesPlugin {
	protected final GameContext pluginExecutionContext;

	private final PluginInformation pluginInformation;

	public ChunkStoriesPlugin(PluginInformation pluginInformation, GameContext pluginExecutionContext) {
		this.pluginInformation = pluginInformation;
		this.pluginExecutionContext = pluginExecutionContext;
	}

	public PluginInformation getPluginInformation() {
		return pluginInformation;
	}

	public GameContext getPluginExecutionContext() {
		return pluginExecutionContext;
	}

	public PluginManager getPluginManager() {
		return pluginExecutionContext.getPluginManager();
	}
	
	public File getDirectory() {
		return pluginInformation.getDirectory();
	}

	public abstract void onEnable();

	public abstract void onDisable();

	public String getName() {
		return pluginInformation.getName();
	}
}
