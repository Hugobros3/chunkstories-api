//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.interfaces;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.client.LocalPlayer;
import io.xol.chunkstories.api.entity.components.EntityComponentController;
import io.xol.chunkstories.api.entity.Controller;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.input.Input;

/**
 * Defines an entity as controllable by players, this is used to allow the Client to send controls to the entity and the
 * server to update the client about it's entity status.
 */
public interface EntityControllable extends Entity, EntityUnsaveable
{
	public EntityComponentController getControllerComponent();
	
	public default Controller getController()
	{
		return getControllerComponent().getController();
	}
	
	/**
	 * Clientside controller tick, called before the main tick() call on clients, supposed to handle the bulk of interactions
	 */
	public void tickClientController(LocalPlayer controller);

	/** Called by the RENDERING thread, each frame. Usefull for stuff like camera control maybe ? */
	public void onEachFrame(LocalPlayer controller);
	
	/**
	 * If this entity has the ability to select blocks, this method should return said block
	 * @param inside If set to true it will return the selected block, if set to false it will return the one adjacent to
	 * @return
	 */
	public Location getBlockLookingAt(boolean inside);
	
	public boolean onControllerInput(Input input, Controller controller);
	
	public default boolean shouldSaveIntoRegion()
	{
		return getController() == null;
	}
}
