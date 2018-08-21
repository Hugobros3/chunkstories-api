//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.events.player.voxel;

import io.xol.chunkstories.api.events.EventListeners;
import io.xol.chunkstories.api.events.voxel.WorldModificationCause;
import io.xol.chunkstories.api.events.voxel.VoxelModificationEvent;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.world.cell.CellData;

public class PlayerVoxelModificationEvent extends VoxelModificationEvent {
	// Every event class has to have this

	static EventListeners listeners = new EventListeners(PlayerVoxelModificationEvent.class);

	@Override
	public EventListeners getListeners() {
		return listeners;
	}

	public static EventListeners getListenersStatic() {
		return listeners;
	}

	// Specific event code
	final Player player;

	public PlayerVoxelModificationEvent(CellData context, CellData newData, WorldModificationCause cause, Player player) {
		super(context, newData, cause);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
}
