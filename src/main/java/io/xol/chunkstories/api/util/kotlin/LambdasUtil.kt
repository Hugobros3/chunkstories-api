//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.util.kotlin

operator fun <T> (T.() -> Unit).plus(behavior: T.() -> Unit) = { receiver : T ->
    this.invoke(receiver)
    behavior.invoke(receiver)
}