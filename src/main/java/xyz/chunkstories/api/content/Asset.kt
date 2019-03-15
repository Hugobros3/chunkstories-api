//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.content

import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader

import xyz.chunkstories.api.content.mods.Mod

/** Assets are used as files in the game's virtual file system  */
interface Asset {
    /** Returns the standardized name of the asset, started by ./ Examples :
     * ./voxels/textures/air.png ./sounds/footsteps/jump.ogg
     * ./shaders/weather/weather.fs  */
    val name: String

    /** Returns the mod this asset originated from  */
    val source: Mod

    /** Accesses the asset data.  */
    fun read(): InputStream

    /** Accesses the asset data.  */
    fun reader(): Reader {
        return InputStreamReader(read())
    }
}
