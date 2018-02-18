package io.xol.chunkstories.api.world;

import io.xol.chunkstories.api.content.OnlineContentTranslator;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface WorldNetworked extends World
{	
	/** Returns a ContentTranslator that can deal with serializing live data */
	public OnlineContentTranslator getContentTranslator();
	
	//public void processIncommingPackets();
	
	//public void queueWorldPacket(PacketWorld packet);
	
	//public void queueWorldStreamingPacket(PacketWorldStreaming packet);
}
