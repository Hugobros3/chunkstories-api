package io.xol.chunkstories.api.events.voxel;

import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.world.VoxelContext;

public class VoxelModificationEvent extends VoxelEvent
{
	// Every event class has to have this
	
	static EventListeners listeners = new EventListeners(VoxelModificationEvent.class);
	
	@Override
	public EventListeners getListeners()
	{
		return listeners;
	}

	public static EventListeners getListenersStatic()
	{
		return listeners;
	}
	
	// Specific event code

	final WorldModificationCause modificationCause;
	VoxelContext newData;
	
	public VoxelModificationEvent(VoxelContext context, VoxelContext newData, WorldModificationCause cause)
	{
		super(context);
		this.newData = newData;
		this.modificationCause = cause;
	}

	public VoxelContext getNewData()
	{
		return newData;
	}

	public WorldModificationCause getModificationCause()
	{
		return modificationCause;
	}

	public ModifiationType getModification() {
		if(getContext().getVoxel().isAir())
			return ModifiationType.PLACEMENT;
		else
		{
			if(newData.getVoxel().isAir())
				return ModifiationType.DESTRUCTION;
			else
				return ModifiationType.REPLACEMENT;
		}
	}
	
	public enum ModifiationType {
		DESTRUCTION,
		PLACEMENT,
		REPLACEMENT;
	}
}
