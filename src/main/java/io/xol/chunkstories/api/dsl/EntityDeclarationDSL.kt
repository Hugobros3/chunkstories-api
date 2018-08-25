package io.xol.chunkstories.api.dsl

import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.content.DeclarationContext
import io.xol.chunkstories.api.entity.*
import io.xol.chunkstories.api.entity.traits.Trait

import kotlin.reflect.KClass

interface EntitiesDeclarationsContext {
    val store : Content.EntityDeclarations

    fun <E: Entity> entity(clazz: KClass<E>, f: EntityDeclarationContext<E>.() -> Unit) = entity(clazz.java, f)

    /** DSL-style convinience method to write an entity declaration in brackers */
    fun <E: Entity> entity(clazz: Class<E>, f: EntityDeclarationContext<E>.() -> Unit)

}

interface EntityDeclarationContext<E: Entity>: DeclarationContext {
    var onlineReplicationDistance: Double

    fun prototype(behavior: E.() -> Unit)

    fun representation(representationDeclaration : EntityRepresentationBuildingContext<*>.() -> Unit)
}

fun <T : Trait> Entity.trait(traitClass: KClass<out T>, initCode: T.() -> Unit) = this.traits[traitClass]?.apply(initCode)
fun <T : Trait> Entity.trait(traitClass: Class<out T>, initCode: T.() -> Unit) = this.traits[traitClass]?.apply(initCode)

interface EntityRepresentationBuildingContext<E : Entity> : DynamicRepresentationBuildingContext {
    val entity : E
}