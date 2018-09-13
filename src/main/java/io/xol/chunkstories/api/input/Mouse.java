//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.input;

public interface Mouse {
	public MouseButton getMainButton();

	/** Returns true if the secondary mouse button is pressed */
	public MouseButton getSecondaryButton();

	/** Returns true if the middle mouse button is pressed */
	public MouseButton getMiddleButton();

	public interface MouseButton extends ClientInput {
		public Mouse getMouse();
	}

	/** Sent when the mouse scrolled (up or down) */
	public interface MouseScroll extends ClientInput {
		public int amount();
	}

	public double getCursorX();

	public double getCursorY();

	public void setMouseCursorLocation(double x, double y);

	public boolean isGrabbed();

	public void setGrabbed(boolean grabbed);
}
