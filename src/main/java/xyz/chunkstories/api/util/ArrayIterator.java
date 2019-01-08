//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.util;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {

	private final T[] array;
	private int i = 0;

	public ArrayIterator(T[] array) {
		this.array = array;
	}

	@Override
	public boolean hasNext() {
		return i < array.length;
	}

	@Override
	public T next() {
		T o = array[i];
		i++;
		return o;
	}

}
