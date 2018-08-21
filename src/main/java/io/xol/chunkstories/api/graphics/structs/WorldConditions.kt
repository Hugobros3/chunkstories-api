package io.xol.chunkstories.api.graphics.structs

import org.joml.Vector3dc

/** Proof-of-concept use of the InterfaceBlock construct, this is used by shaders to obtain basic world data */
data class WorldConditions(
        var time: Float,
        var weather: Float,
        var sunPosition: Vector3dc
) : InterfaceBlock