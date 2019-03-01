//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits

import xyz.chunkstories.api.entity.Entity

/**
 *
 *
 * Marks this entity as renderable and provides a lambda expression to obtain
 * that renderer. Do note that renderers are cached per-entity declaration; it's
 * therefore impossible to have multiple renderers for the same entity type
 *
 */
class TraitRenderable<T : Entity>(entity: T) : Trait(entity)
