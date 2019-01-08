//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item.interfaces;

/** An interface for items that provide their own draw animation */
public interface ItemCustomHoldingAnimation {
	public String getCustomAnimationName();

	public default double transformAnimationTime(double originalTime) {
		return originalTime;
	}
}
