//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.voxel;

import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.world.cell.CellData;

public class VoxelModificationEvent extends VoxelEvent {
	// Every event class has to have this

	static EventListeners listeners = new EventListeners(VoxelModificationEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	// Specific event code

	final WorldModificationCause modificationCause;
	CellData newData;

	public VoxelModificationEvent(CellData context, CellData newData, WorldModificationCause cause) {
		super(context);
		this.newData = newData;
		this.modificationCause = cause;
	}

	public CellData getNewData() {
		return newData;
	}

	public WorldModificationCause getModificationCause() {
		return modificationCause;
	}

	public ModifiationType getModification() {
		if (getContext().getVoxel().isAir())
			return ModifiationType.PLACEMENT;
		else {
			if (newData.getVoxel().isAir())
				return ModifiationType.DESTRUCTION;
			else
				return ModifiationType.REPLACEMENT;
		}
	}

	public enum ModifiationType
	{
		DESTRUCTION, PLACEMENT, REPLACEMENT;
	}
}
