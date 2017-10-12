package io.xol.chunkstories.api.util;

import java.util.Iterator;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

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
