package io.xol.chunkstories.api.util;

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
