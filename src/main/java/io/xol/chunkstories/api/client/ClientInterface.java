//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.client;

import io.xol.chunkstories.api.GameContext;
import io.xol.chunkstories.api.item.inventory.Inventory;
import io.xol.chunkstories.api.particles.ParticlesManager;
import io.xol.chunkstories.api.plugin.ClientPluginManager;
import io.xol.chunkstories.api.rendering.GameWindow;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.effects.DecalsManager;
import io.xol.chunkstories.api.util.Configuration;
import io.xol.chunkstories.api.world.WorldClient;

import javax.annotation.Nullable;

public interface ClientInterface extends GameContext {
	/** Returns the username logged in */
	public String username();

	/** Returns a valid PlayerClient as long as this client is currently in-game */
	@Nullable
	public LocalPlayer getPlayer();

	public GameWindow getGameWindow();

	/** Prints some text into the client chat */
	public void printChat(String textToPrint);

	/** Returns the currently played world, if such exist or null */
	@Nullable
	public WorldClient getWorld();

	/** Changes the game to a new world */
	public void changeWorld(WorldClient world);

	/** Closes current world and exits to main menu */
	public void exitToMainMenu();

	/** Closes current world and exits to main menu with an error message */
	public void exitToMainMenu(String errorMessage);

	/**
	 * Opens the inventory GUI with all the specified inventories opened
	 */
	public void openInventories(Inventory... inventories);

	/**
	 * @return Is the game GUI in focus or obstructed by other things ?
	 */
	public boolean hasFocus();

	/**
	 * Reloads all assets, shaders, sounds and whatnot from the mods and the main
	 * game.
	 */
	public void reloadAssets();

	public ClientContent getContent();

	public ClientSoundManager getSoundManager();

	public ClientInputsManager getInputsManager();

	public ClientPluginManager getPluginManager();

	public ParticlesManager getParticlesManager(); // TODO move in world

	public DecalsManager getDecalsManager(); // TODO same

	public RenderingInterface getRenderingInterface();

	public Configuration getConfiguration();
}
