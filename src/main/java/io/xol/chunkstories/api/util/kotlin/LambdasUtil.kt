package io.xol.chunkstories.api.util.kotlin

operator fun <T> (T.() -> Unit).plus(behavior: T.() -> Unit) = { receiver : T ->
    this.invoke(receiver)
    behavior.invoke(receiver)
}