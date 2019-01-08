//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits;

import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.gui.GuiDrawer;

public abstract class TraitHasOverlay extends Trait {

	public TraitHasOverlay(Entity entity) {
		super(entity);
	}

	public abstract void drawEntityOverlay(GuiDrawer drawer);

}
