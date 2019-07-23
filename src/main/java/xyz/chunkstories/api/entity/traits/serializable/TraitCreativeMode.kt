//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.traits.generic.TraitSerializableBoolean
import xyz.chunkstories.api.events.voxel.WorldModificationCause
import xyz.chunkstories.api.voxel.MiningTool

class TraitCreativeMode(entity: Entity) : TraitSerializableBoolean(entity) {
    companion object {
        val CREATIVE_MODE: WorldModificationCause = object : WorldModificationCause {

            override val name: String
                get() = "Creative Mode"
        }

        val CREATIVE_MODE_MINING_TOOL: MiningTool = object : MiningTool {
            override val miningEfficiency: Float = Float.POSITIVE_INFINITY
            override val toolTypeName: String = "hand"
        }
    }
}
