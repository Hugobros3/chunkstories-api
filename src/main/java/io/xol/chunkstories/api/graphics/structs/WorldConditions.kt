package io.xol.chunkstories.api.graphics.structs

import org.joml.Vector3d
import org.joml.Vector3dc
import org.joml.Vector3f

/** Proof-of-concept use of the InterfaceBlock construct, this is used by shaders to obtain basic world data */
data class WorldConditions(
        var time: Float = 0f,
        var sunPosition: Vector3dc = Vector3d(0.0, 1.0, 0.0),

        //var weather: Float = 0f,
        var wetness: Float = 0f,
        var foggyness: Float = 0f,
        var cloudyness: Float = 0f
) : InterfaceBlock