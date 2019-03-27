//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.util.kotlin

/** "concatenation" of lambdas (execute on after the other) */
operator fun <T> (T.() -> Unit).plus(behavior: T.() -> Unit) = { receiver : T ->
    this.invoke(receiver)
    behavior.invoke(receiver)
}