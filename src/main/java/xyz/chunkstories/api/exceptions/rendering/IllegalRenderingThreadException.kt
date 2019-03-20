//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.rendering

/** Rendering functions should only be called inside the main rendering thread.
 * To check if you are in the proper thread, please use
 * GameWindowOpenGL.isMainGLWindow()  */
class IllegalRenderingThreadException : RuntimeException() {
    companion object {

        /**
         *
         */
        private val serialVersionUID = -8543094072345220060L
    }

}
