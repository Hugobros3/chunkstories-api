package io.xol.chunkstories.api.dsl

import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.entity.*
import io.xol.chunkstories.api.entity.traits.Trait

import kotlin.reflect.KClass

interface EntitiesDeclarationsContext {
    /** DSL-style convinience method to write an entity declaration in brackers */
    fun entity(f: EntityDeclarationContext.() -> Unit)
}

interface EntityDeclarationContext {
    val store : Content.EntityDeclarations

    var name : String
    var ext : MutableMap<String, String>

    var abstract: Boolean

    fun extends(otherEntity: String)

    fun <T : Trait> trait(traitClass: KClass<out T>) = trait(traitClass.java)
    fun <T : Trait> trait(traitClass: KClass<out T>, initCode: T.() -> Unit) = trait(traitClass.java, initCode)
    fun <T : Trait> trait(traitClass: Class<out T>) = trait(traitClass) {Unit}

    /** Adds a trait to that entity prototype, along with extra initialisation code */
    fun <T : Trait> trait(traitClass: Class<out T>, initCode: T.() -> Unit)// = traits.add(DeclaredTrait(traitClass, initCode))

    /** Appends code to the initialization lambda */
    fun init(behavior: Entity.() -> Unit)

    /** Appends code to the tick lambda */
    fun tick(behavior: Entity.() -> Unit)

    fun representation(representationDeclaration : EntityRepresentationBuildingInstructions)
}