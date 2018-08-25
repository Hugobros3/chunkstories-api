package io.xol.chunkstories.api.entity

import io.xol.chunkstories.api.Location
import io.xol.chunkstories.api.entity.traits.Trait
import io.xol.chunkstories.api.entity.traits.serializable.TraitLocation
import io.xol.chunkstories.api.util.BooleanAction
import io.xol.chunkstories.api.util.ReturnsAction
import io.xol.chunkstories.api.util.VoidAction
import io.xol.chunkstories.api.world.World
import kotlin.reflect.KClass

interface Entity {
    val declaration: EntityDeclaration

    val world : World
    var location : Location

    var UUID : Long

    val traitLocation: TraitLocation

    val traits : Traits
    interface Traits : Map<Class<Trait>, Trait> {
        fun registerTrait(trait : Trait) : Int

        fun has(trait : Trait) : Boolean

        operator fun <T: Trait> get(traitClass: KClass<T>) = this[traitClass.java]
        operator fun <T: Trait> get(traitClass: Class<T>) : T?

        fun <T : Trait> with(traitClass: KClass<T>, action: T.() -> Unit)
        fun <T : Trait> with(traitClass: Class<T>, action: VoidAction<T>)

        fun <T: Trait, R> tryWith(traitClass: KClass<T>, action: T.() -> R) : R
        fun <T: Trait, R> tryWith(traitClass: Class<T>, action : ReturnsAction<T, R>) : R

        fun <T: Trait> tryWithBoolean(traitClass: KClass<T>, action: T.() -> Boolean) : Boolean
        fun <T: Trait> tryWithBoolean(traitClass: Class<T>, action : BooleanAction<T>) : Boolean

        fun all() : Collection<Trait>

        fun byId() : Array<Trait>
    }

    val subscribers : Subscribers

    interface Subscribers : MutableCollection<Subscriber>{
        fun register(subscriber: Subscriber) = this.add(subscriber)
        fun unregister(subscriber: Subscriber) = this.remove(subscriber)
    }
}
