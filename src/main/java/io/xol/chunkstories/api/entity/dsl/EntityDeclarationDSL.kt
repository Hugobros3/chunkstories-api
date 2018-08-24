package io.xol.chunkstories.api.entity.dsl

import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.entity.DeclaredTrait
import io.xol.chunkstories.api.entity.Entity
import io.xol.chunkstories.api.entity.EntityDefinition
import io.xol.chunkstories.api.entity.traits.Trait
import io.xol.chunkstories.api.graphics.representation.RepresentationBuildingCtx
import io.xol.chunkstories.api.graphics.representation.RepresentationBuildingInstructions

import io.xol.chunkstories.api.util.kotlin.plus
import kotlin.reflect.KClass

interface EntitiesDefinitionsContext {
    fun registerEntityDefinition(f : EntityDefinition)

    /** DSL-style convinience method to write an entity definition in brackers */
    fun entity(f: MutableEntityDefinition.() -> Unit)

    fun EntityDefinition.representation(representationDeclaration : EntityRepresentationBuildingInstructions)
}

class MutableEntityDefinition(val store : Content.EntityDefinitions) : EntityDefinition {
    override lateinit var name : String
    override var ext = mutableMapOf<String, String>()

    override var abstract = false

    override val traits = mutableSetOf<DeclaredTrait<*>>()

    fun extends(otherEntity: String) {
        //TODO copies init, tick and traits
    }

    fun <T : Trait> trait(traitClass: KClass<out T>) = trait(traitClass.java)
    fun <T : Trait> trait(traitClass: KClass<out T>, initCode: T.() -> Unit) = trait(traitClass.java, initCode)
    fun <T : Trait> trait(traitClass: Class<out T>) = trait(traitClass) {Unit}

    /** Adds a trait to that entity prototype, along with extra initialisation code */
    fun <T : Trait> trait(traitClass: Class<out T>, initCode: T.() -> Unit) = traits.add(DeclaredTrait(traitClass, initCode))

    /** Code that gets run on initialization */
    var init : Entity.() -> Unit = { Unit }
    /** Appends code to the initialization lambda */
    fun init(behavior: Entity.() -> Unit) {
        init += behavior
    }

    /** Code that gets run every tick */
    var tick : Entity.() -> Unit = { Unit }

    /** Appends code to the tick lambda */
    fun tick(behavior: Entity.() -> Unit) {
        tick += behavior
    }

    override fun store() = store
}

typealias EntityRepresentationBuildingInstructions = (EntityRepresentationBuildingCtx.() -> Unit)
interface EntityRepresentationBuildingCtx : RepresentationBuildingCtx {
    val entity : Entity
}