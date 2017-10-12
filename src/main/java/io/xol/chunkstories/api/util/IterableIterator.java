package io.xol.chunkstories.api.util;

import java.util.Iterator;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/**
 * Java silly spec workarround ( syntax sugar )
 * I want to be allowed to 'for(Object o : iterator)'.
 */
public interface IterableIterator<T> extends Iterator<T>, Iterable<T>
{
	public default Iterator<T> iterator()
	{
		return this;
	}
}
