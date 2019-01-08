//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api;

import xyz.chunkstories.api.plugin.PluginManager;
import xyz.chunkstories.api.plugin.Scheduler;

public interface GameLogic {
	public GameContext getGameContext();

	public int getTargetFps();

	public double getSimulationFps();

	public double getSimulationSpeed();

	public PluginManager getPluginsManager();

	public Scheduler getScheduler();
}