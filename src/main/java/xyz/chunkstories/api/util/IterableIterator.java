//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.util;

import java.util.Iterator;

// TODO nuke
/** Java silly spec workarround ( syntax sugar ) I want to be allowed to
 * 'for(Object o : iterator)'. */
public interface IterableIterator<T> extends Iterator<T>, Iterable<T> {
	public default Iterator<T> iterator() {
		return this;
	}
}
