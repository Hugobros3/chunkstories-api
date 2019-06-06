//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.representation

import org.joml.Vector3d
import org.joml.Vector4d
import org.joml.Vector4f
import xyz.chunkstories.api.graphics.systems.dispatching.RepresentationsGobbler

data class Line(val start: Vector3d, val end: Vector3d, val color: Vector4f) : Representation

fun RepresentationsGobbler.drawCube(start: Vector3d, end: Vector3d, color: Vector4f) {
    acceptRepresentation(Line(Vector3d(start.x, start.y, start.z), Vector3d(start.x, start.y, end.z), color))
    acceptRepresentation(Line(Vector3d(start.x, start.y, start.z), Vector3d(end.x, start.y, start.z), color))
    acceptRepresentation(Line(Vector3d(end.x, start.y, start.z), Vector3d(end.x, start.y, end.z), color))
    acceptRepresentation(Line(Vector3d(start.x, start.y, end.z), Vector3d(end.x, start.y, end.z), color))

    acceptRepresentation(Line(Vector3d(start.x, end.y, start.z), Vector3d(start.x, end.y, end.z), color))
    acceptRepresentation(Line(Vector3d(start.x, end.y, start.z), Vector3d(end.x, end.y, start.z), color))
    acceptRepresentation(Line(Vector3d(end.x, end.y, start.z), Vector3d(end.x, end.y, end.z), color))
    acceptRepresentation(Line(Vector3d(start.x, end.y, end.z), Vector3d(end.x, end.y, end.z), color))

    acceptRepresentation(Line(Vector3d(start.x, start.y, start.z), Vector3d(start.x, end.y, start.z), color))
    acceptRepresentation(Line(Vector3d(end.x, start.y, start.z), Vector3d(end.x, end.y, start.z), color))
    acceptRepresentation(Line(Vector3d(start.x, start.y, end.z), Vector3d(start.x, end.y, end.z), color))
    acceptRepresentation(Line(Vector3d(end.x, start.y, end.z), Vector3d(end.x, end.y, end.z), color))
}