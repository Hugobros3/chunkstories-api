package io.xol.chunkstories.api.entity

import io.xol.chunkstories.api.dsl.DynamicRepresentationBuildingContext
import io.xol.chunkstories.api.graphics.representation.Representation

interface EntityRepresentation : Representation {
    val entity : Entity
}

typealias EntityRepresentationBuildingInstructions = (EntityRepresentationBuildingContext.() -> Unit)
interface EntityRepresentationBuildingContext : DynamicRepresentationBuildingContext {
    //override val representation: EntityRepresentation

    val entity : Entity
}