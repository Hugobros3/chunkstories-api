//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.voxel;

import xyz.chunkstories.api.events.CancellableEvent;
import xyz.chunkstories.api.events.EventListeners;
import xyz.chunkstories.api.world.cell.CellData;

public abstract class VoxelEvent extends CancellableEvent {
	// Every event class has to have this

	static EventListeners listeners = new EventListeners(VoxelEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	// Specific event code

	final CellData context;

	public VoxelEvent(CellData context) {
		super();
		this.context = context;
	}

	/** Returns the context before the voxel destruction */
	public CellData getContext() {
		return context;
	}
}
