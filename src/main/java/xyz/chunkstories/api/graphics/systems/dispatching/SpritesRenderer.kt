package xyz.chunkstories.api.graphics.systems.dispatching

interface SpritesRenderer : DispatchingSystem {
    var materialTag: String

    var shader: String
}