package io.xol.chunkstories.api.net;

import io.xol.chunkstories.api.content.Definition;

public interface PacketDefinition extends Definition {
	public String getName();
	
	public AllowedFrom allowedFrom();
	
	public enum AllowedFrom {
		ALL,
		CLIENT,
		SERVER;
	}
	
	public PacketGenre getType();
	
	public enum PacketGenre {
		/** Internal packets the engine uses to negociate connections and do it's job */
		SYSTEM,
		/** For miscalineous uses, both the engine & mods may use those */
		GENERAL_PURPOSE,
		/** Tied to the world ticks; contains a reference to the particular world; interpreted during the world tick function */
		WORLD,
		/** Tied to the world data streaming; contains a reference to the particular world; */
		WORLD_STREAMING,
	}
}