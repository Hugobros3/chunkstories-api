package io.xol.chunkstories.api.player;

import io.xol.chunkstories.api.GameContext;
import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.entity.Controller;
import io.xol.chunkstories.api.entity.components.Subscriber;
import io.xol.chunkstories.api.entity.interfaces.EntityControllable;
import io.xol.chunkstories.api.item.inventory.Inventory;
import io.xol.chunkstories.api.net.PacketDestinator;
import io.xol.chunkstories.api.net.PacketSender;
import io.xol.chunkstories.api.plugin.commands.CommandEmitter;
import io.xol.chunkstories.api.world.World;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Represents a player (Remote or Local) as the once calling shots */
public interface Player extends CommandEmitter, Controller, Subscriber, PacketDestinator, PacketSender
{
	/** @return the username of the player */
	public String getName();

	/** @return the displayable name of the player (including things like tags, color etc) */
	public String getDisplayName();
	
	/** @return The {@link World} the entity belongs to. */
	public World getWorld();

	/** @return The {@link GameContext context} the world, and thus the entity belongs to. */
	public GameContext getContext();
	
	/** @return the entity this player is controlling */
	public EntityControllable getControlledEntity();
	
	/** Sets the entity this player has control over (and tells him) */
	public boolean setControlledEntity(EntityControllable entity);
	
	/** Sends a text message to this player chat */
	public void sendMessage(String msg);
	
	/** @return The {@link Location} of this player in his world */
	public Location getLocation();
	
	/** Sets the {@link Location} of the user. Warning, can't change the world he's in with this method ! */
	public void setLocation(Location l);
	
	/** @return True once the player connection was interrupted */
	public boolean isConnected();

	/** @return True once the player has been spawned inside it's {@link World}. */
	public boolean hasSpawned();
	
	/** Helper method: Tries to open the specified inventory for the following player */
	public void openInventory(Inventory inventory);
}
