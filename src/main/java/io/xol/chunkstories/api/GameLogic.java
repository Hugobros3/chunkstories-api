//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api;

import io.xol.chunkstories.api.plugin.PluginManager;
import io.xol.chunkstories.api.plugin.Scheduler;

public interface GameLogic
{
	public GameContext getGameContext();
	
	public int getTargetFps();
	
	public double getSimulationFps();
	
	public double getSimulationSpeed();
	
	public PluginManager getPluginsManager();

	public Scheduler getScheduler();
}