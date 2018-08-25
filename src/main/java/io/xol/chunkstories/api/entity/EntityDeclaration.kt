//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity

import io.xol.chunkstories.api.Location
import io.xol.chunkstories.api.content.Content.EntityDeclarations
import io.xol.chunkstories.api.content.Declaration
import io.xol.chunkstories.api.entity.traits.Trait

interface EntityDeclaration : Declaration {
    val abstract: Boolean

    val traits: Set<DeclaredTrait<*>>

    fun store() : EntityDeclarations

    fun create(location: Location) : Entity
}

data class DeclaredTrait<T : Trait>(val traitClass: Class<T>, var initCode : T.() -> Unit)