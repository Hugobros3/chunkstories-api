//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.util;

import java.util.Iterator;

/** a fraud. */
public class IterableIteratorWrapper<T> implements IterableIterator<T> {

	final Iterator<T> iterator;

	public IterableIteratorWrapper(Iterator<T> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public T next() {
		return iterator.next();
	}

	@Override
	public void remove() {
		iterator.remove();
	}

	@Override
	public Iterator<T> iterator() {
		return iterator;
	}
}
