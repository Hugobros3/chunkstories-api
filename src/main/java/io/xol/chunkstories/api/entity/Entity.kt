package io.xol.chunkstories.api.entity

import io.xol.chunkstories.api.Location
import io.xol.chunkstories.api.entity.traits.Trait
import io.xol.chunkstories.api.entity.traits.serializable.TraitLocation
import io.xol.chunkstories.api.util.ReturnsAction
import io.xol.chunkstories.api.util.VoidAction
import io.xol.chunkstories.api.world.World
import kotlin.reflect.KClass

interface Entity {
    val definition: EntityDefinition

    val world : World
    var location : Location

    val uuid : Long

    val traitLocation: TraitLocation

    val traits : Traits
    interface Traits {
        fun registerTrait(trait : Trait) : Int

        fun has(trait : Trait) : Boolean

        operator fun <T: Trait> get(traitClass: KClass<T>) = this[traitClass.java]
        operator fun <T: Trait> get(traitClass: Class<T>) : T

        fun <T : Trait> with(traitClass: KClass<T>, action: T.() -> Unit)
        fun <T : Trait> with(traitClass: Class<T>, action: VoidAction<T>)
    }

    val subscribers : Entity_.Subscribers
}
