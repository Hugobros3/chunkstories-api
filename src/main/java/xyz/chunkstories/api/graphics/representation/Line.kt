//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.representation

import org.joml.Matrix4d
import org.joml.Vector3d
import org.joml.Vector4d
import org.joml.Vector4f
import xyz.chunkstories.api.graphics.systems.dispatching.RepresentationsGobbler

data class Line(val start: Vector3d, val end: Vector3d, val color: Vector4f) : Representation

fun RepresentationsGobbler.drawCube(start: Vector3d, end: Vector3d, color: Vector4f, transform: Matrix4d = Matrix4d()) {
    val v000 = Vector3d(start.x, start.y, start.z).transform(transform)
    val v001 = Vector3d(start.x, start.y, end.z).transform(transform)
    val v100 = Vector3d(end.x, start.y, start.z).transform(transform)
    val v101 = Vector3d(end.x, start.y, end.z).transform(transform)

    val v010 = Vector3d(start.x, end.y, start.z).transform(transform)
    val v011 = Vector3d(start.x, end.y, end.z).transform(transform)
    val v110 = Vector3d(end.x, end.y, start.z).transform(transform)
    val v111 = Vector3d(end.x, end.y, end.z).transform(transform)

    acceptRepresentation(Line(v000, v001, color))
    acceptRepresentation(Line(v000, v100, color))
    acceptRepresentation(Line(v100, v101, color))
    acceptRepresentation(Line(v001, v101, color))

    acceptRepresentation(Line(v010, v011, color))
    acceptRepresentation(Line(v010, v110, color))
    acceptRepresentation(Line(v110, v111, color))
    acceptRepresentation(Line(v011, v111, color))

    acceptRepresentation(Line(v000, v010, color))
    acceptRepresentation(Line(v100, v110, color))
    acceptRepresentation(Line(v001, v011, color))
    acceptRepresentation(Line(v101, v111, color))
}

fun Vector3d.transform(matrix: Matrix4d) : Vector3d {
    val vec4 = Vector4d(x, y, z, 1.0)
    matrix.transform(vec4)
    val iw = 1.0 / vec4.w
    return Vector3d(vec4.x * iw, vec4.y * iw, vec4.z * iw)
}

