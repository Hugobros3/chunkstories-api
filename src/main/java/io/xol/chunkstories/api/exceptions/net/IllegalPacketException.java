//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.net;

import io.xol.chunkstories.api.net.Packet;

public class IllegalPacketException extends Exception {

	Packet packet;

	public IllegalPacketException(Packet packet) {
		this.packet = packet;
	}

	@Override
	public String getMessage() {
		return "Illegal packet received : " + packet.getClass().getName() + "";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4148448942644331785L;

}
