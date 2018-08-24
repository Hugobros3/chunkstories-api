//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity

import io.xol.chunkstories.api.content.Content.EntityDefinitions
import io.xol.chunkstories.api.entity.traits.Trait

interface EntityDefinition {
    val name: String
    val ext: Map<String, String>

    val abstract: Boolean

    val traits: Set<DeclaredTrait<*>>

    fun store() : EntityDefinitions
}

data class DeclaredTrait<T : Trait>(val traitClass: Class<T>, var initCode : T.() -> Unit)