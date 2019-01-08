//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.player.voxel;

import xyz.chunkstories.api.events.EventListeners;
import xyz.chunkstories.api.events.voxel.WorldModificationCause;
import xyz.chunkstories.api.events.voxel.VoxelModificationEvent;
import xyz.chunkstories.api.player.Player;
import xyz.chunkstories.api.world.cell.CellData;

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
