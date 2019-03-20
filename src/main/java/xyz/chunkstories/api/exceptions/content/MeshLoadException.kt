//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.content

import xyz.chunkstories.api.content.Asset

class MeshLoadException(asset: Asset) : AssetException(asset) {

    override val message: String?
        get() = "Mesh from asset $asset failed to load."

    companion object {

        private val serialVersionUID = 5322485540396142553L
    }

}
