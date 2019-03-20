//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.content

import xyz.chunkstories.api.content.Asset

open class AssetException(val asset: Asset) : Exception() {
    companion object {
        private val serialVersionUID = -1208547268257208116L
    }
}
