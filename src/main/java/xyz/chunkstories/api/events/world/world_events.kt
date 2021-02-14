package xyz.chunkstories.api.events.world

import xyz.chunkstories.api.events.Event
import xyz.chunkstories.api.world.World

class WorldTickEvent(val world: World) : Event()