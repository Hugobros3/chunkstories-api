package xyz.chunkstories.api.world

import xyz.chunkstories.api.world.cell.EditableCell

interface WorldCell : EditableCell {
    override val world: World
}