//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world

import xyz.chunkstories.api.world.cell.EditableCell

interface WorldCell : EditableCell {
    override val world: World
}