//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions;

public class UnauthorizedClientActionException extends RuntimeException {
	private static final long serialVersionUID = 800139003416109519L;

	String functionCalled;

	public UnauthorizedClientActionException(String functionCalled) {
		this.functionCalled = functionCalled;
	}

	public String getMessage() {
		return "Illegal master function : " + functionCalled + " got called but the world is not master.";
	}
}
