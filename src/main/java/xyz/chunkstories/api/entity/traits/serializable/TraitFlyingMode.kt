//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.traits.generic.TraitSerializableBoolean

/** Keeps track of the flying flag, movement logic has moved to PlayerMovementController in core */
class TraitFlyingMode(entity: Entity) : TraitSerializableBoolean(entity)    
