package io.xol.chunkstories.api.item

import io.xol.chunkstories.api.dsl.DynamicRepresentationBuildingContext
import io.xol.chunkstories.api.entity.Entity
import io.xol.chunkstories.api.graphics.representation.Representation

interface ItemRepresentation : Representation {
    val item : Item
}

//typealias ItemRepresentationBuildingInstructions = (ItemRepresentationBuildingContext<Item>.() -> Unit)

interface ItemRepresentationBuildingContext<T: Item> : DynamicRepresentationBuildingContext {
    val item : T
}