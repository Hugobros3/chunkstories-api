//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.entity;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.util.IterableIterator;

public interface RenderingIterator<E extends Entity> extends IterableIterator<E>
{
	/**
	 * __The specification of Iterator__
	 * 
	 * If the next element is within the view frustrum it will also prepare the renderer for this object
	 */
	public E next();
	
	/**
	 * Checks the current element is in view frustrum
	 * Requires you have already called next() and it returned a non-null value, otherwise it will throw a RuntimeException
	 */
	public boolean isCurrentElementInViewFrustrum();
	
	/**
	 * Returns a filtered iterator that only returns elements in-frustrum
	 */
	public RenderingIterator<E> getElementsInFrustrumOnly();
}
