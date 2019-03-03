package xyz.chunkstories.api.graphics.systems.dispatching

interface ModelsRenderer : DispatchingSystem {
    var materialTag: String

    var shader: String
    var supportsAnimations: Boolean
}