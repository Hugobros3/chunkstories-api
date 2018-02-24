//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.net;

import io.xol.chunkstories.api.net.Packet;

public class UnknowPacketException extends Exception
{
	String m;
	
	public UnknowPacketException(int packetType)
	{
		this.m = "Unknown packet ID received : "+packetType;
	}
	
	public UnknowPacketException(Packet packet)
	{
		this.m = "Couldn't determine the ID for the packet : "+packet.getClass().getSimpleName() + ", is it declared in a .packets file ?";
	}
	
	@Override
	public String getMessage()
	{
		return m;
	}
	
	private static final long serialVersionUID = 7612121415158158595L;

}
