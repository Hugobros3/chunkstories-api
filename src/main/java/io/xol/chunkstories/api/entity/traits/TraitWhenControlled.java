//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.traits;

import org.joml.Vector3d;

import io.xol.chunkstories.api.client.LocalPlayer;
import io.xol.chunkstories.api.entity.Controller;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.input.Input;
import io.xol.chunkstories.api.rendering.RenderingInterface;

/** These methods are called when an entity is controlled ( {@see EntityController} ) and handle inputs */
public class TraitWhenControlled extends Trait {

	public TraitWhenControlled(Entity entity) {
		super(entity);
	}
	
	public void onEachFrame(LocalPlayer controller) {
		
	}
	
	public boolean onControllerInput(Input input, Controller controller) {
		return false; //return true to signify you handled that event
	}

	public void setupCamera(RenderingInterface renderer) {
		renderer.getCamera().setCameraPosition(new Vector3d(entity.getLocation()));

		// Default FOV
		renderer.getCamera().setFOV((float) renderer.getClient().getConfiguration().getDoubleOption("fov"));
	}
}
