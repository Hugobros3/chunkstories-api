package io.xol.chunkstories.api.entity.interfaces;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.components.EntityComponentCreativeMode;
import io.xol.chunkstories.api.events.voxel.WorldModificationCause;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface EntityCreative extends Entity
{
	public EntityComponentCreativeMode getCreativeModeComponent();
	
	public default boolean isCreativeMode()
	{
		return getCreativeModeComponent().get();
	}

	public default void setCreativeMode(boolean creativeMode)
	{
		getCreativeModeComponent().set(creativeMode);
	}

	public static final WorldModificationCause CREATIVE_MODE = new WorldModificationCause() {

		@Override
		public String getName()
		{
			return "Creative Mode";
		}
		
	};
}
