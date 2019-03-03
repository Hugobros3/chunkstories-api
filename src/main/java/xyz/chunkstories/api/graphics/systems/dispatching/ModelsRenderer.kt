package xyz.chunkstories.api.graphics.systems.dispatching

import xyz.chunkstories.api.graphics.representation.ModelInstance

interface ModelsRenderer : DispatchingSystem {
    var shader: String
    var materialTag: String
}