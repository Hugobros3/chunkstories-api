//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.util.kotlin

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <R, T> initOnce(): ReadWriteProperty<R, T> = object : ReadWriteProperty<R, T> {
    var value: T? = null

    override fun getValue(thisRef: R, property: KProperty<*>): T = value ?: throw Exception("Uninitialized value !")
    override fun setValue(thisRef: R, property: KProperty<*>, value: T) = if (this.value == null) this.value = value else throw Exception("You can't set that value twice !")
}