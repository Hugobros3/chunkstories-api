//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.systems.dispatching

import xyz.chunkstories.api.graphics.rendergraph.RenderTaskInstance
import xyz.chunkstories.api.graphics.representation.Representation
import xyz.chunkstories.api.graphics.systems.GraphicSystem

/** A Dispatching system receives representations and draws them. It might schedule draws in multiple passes. */
interface DispatchingSystem : GraphicSystem {
    val representationName: String
}

/** The interface responsible for enumerating every representation that will be used to draw frame N */
interface RepresentationsGobbler {
    //TODO val frame: Frame
    //TODO val framgeGraph: FrameGraph
    //val passInstances: Array<PassInstance>
    //val cameras: Array<Camera>
    val renderTaskInstances: Array<RenderTaskInstance>

    /** Register a representation to be drawn for this frame. Each bit of visibilityMask correspond to one entry in the passInstances array. */
    fun <T : Representation> acceptRepresentation(representation: T, mask: Int = -1)
}

interface RepresentationsProvider {
    fun gatherRepresentations(representationsGobbler: RepresentationsGobbler)
}