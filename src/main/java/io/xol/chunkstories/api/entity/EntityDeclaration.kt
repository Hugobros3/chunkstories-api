//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity

import io.xol.chunkstories.api.Location
import io.xol.chunkstories.api.content.Content.EntityDeclarations
import io.xol.chunkstories.api.content.Declaration
import io.xol.chunkstories.api.dsl.EntityRepresentationBuildingContext

interface EntityDeclaration<E : Entity> : Declaration {
    /** Reference to the containing store */
    fun store(): EntityDeclarations

    /** The actual class used by this Entity. Cannot be Entity::class ! */
    val clazz: Class<E>

    /** How near the entity needs to be so clients can see it in multiplayer */
    val onlineReplicationDistance: Double

    /** Initialization code that is applied to the Entity at initialization (but after constructor)*/
    val prototype: E.() -> Unit

    /** Instructions for building a representation based on the item */
    val representation: EntityRepresentationBuildingContext<E>.() -> Unit

    /** Creates a new entity from this declaration */
    fun newEntity(location: Location): Entity
}