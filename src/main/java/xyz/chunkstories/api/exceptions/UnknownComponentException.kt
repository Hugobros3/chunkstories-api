//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions

import xyz.chunkstories.api.entity.Entity

class UnknownComponentException(override val message: String) : PacketProcessingException() {

    constructor(componentId: Int, entityClass: Class<out Entity>) : this(
            message = "The componentId : " + componentId + " for the entity " + entityClass.name + " was not found"
    )

    constructor(componentName: String, entityClass: Class<out Entity>) : this(
            message = "The componentId : " + componentName + " for the entity " + entityClass.name + " was not found"
    )

    companion object {
        private val serialVersionUID = -3592430343334562201L
    }
}
