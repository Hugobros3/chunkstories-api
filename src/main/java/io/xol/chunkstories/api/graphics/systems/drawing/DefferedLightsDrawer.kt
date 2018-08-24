package io.xol.chunkstories.api.graphics.systems.drawing

/** Draws all the deffered lights in the scene (exc. the sun) as fullscreen quads. */
interface DefferedLightsDrawer : DrawingSystem {
    var maxBatchSize: Int
}