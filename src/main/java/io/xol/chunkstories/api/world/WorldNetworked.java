package io.xol.chunkstories.api.world;

import io.xol.chunkstories.api.net.PacketWorld;
import io.xol.chunkstories.api.net.PacketWorldStreaming;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface WorldNetworked
{	
	/**
	 * Temp
	 */
	public void processIncommingPackets();
	
	public void queueWorldPacket(PacketWorld packet);
	
	public void queueWorldStreamingPacket(PacketWorldStreaming packet);
}
