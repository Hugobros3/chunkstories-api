//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.util;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;

public class CompoundIterator<T> implements IterableIterator<T> {

	private final Iterator<Iterator<T>> iteratorOfiterators;
	@Nullable
	private Iterator<T> currentIterator = null;
	
	public CompoundIterator(Collection<Iterator<T>> listOfIterators) {
		this.iteratorOfiterators = listOfIterators.iterator();
	}
	
	@Override
	public boolean hasNext() {
		
		while(true) {
			if(currentIterator == null || !currentIterator.hasNext()) {
				if(iteratorOfiterators.hasNext())
					currentIterator = iteratorOfiterators.next();
				else
					return false;
			}
			
			if(currentIterator.hasNext())
				return true;
		}
	}

	@Override
	public T next() {
		//No failsafe yet if someone spams next() without checking hasNext()
		return currentIterator.next();
	}

	@Override
	public void remove() {
		currentIterator.remove();
	}
	
}
