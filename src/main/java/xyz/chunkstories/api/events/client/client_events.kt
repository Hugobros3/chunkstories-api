//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events.client

import xyz.chunkstories.api.client.Client
import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.Event
import xyz.chunkstories.api.input.Input

class ClientInputPressedEvent(val client: Client, val input: Input) : CancellableEvent()
class ClientInputReleasedEvent(val client: Client, val input: Input) : Event()