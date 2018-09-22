//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.client

import io.xol.chunkstories.api.GameContext
import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.graphics.Window
import io.xol.chunkstories.api.graphics.systems.dispatching.DecalsManager
import io.xol.chunkstories.api.gui.Gui
import io.xol.chunkstories.api.net.AuthentificationMethod
import io.xol.chunkstories.api.particles.ParticlesManager
import io.xol.chunkstories.api.plugin.ClientPluginManager
import io.xol.chunkstories.api.util.Configuration
import io.xol.chunkstories.api.world.WorldClient
import java.util.*

/** The game client abstracted from a generic runtime perspective (not necessarily in game) */
interface Client {
    val gameWindow: Window
    val gui: Gui

    val content: Content

    val soundManager: ClientSoundManager
    val inputsManager: ClientInputsManager

    /** If we are ingame this will be set ! */
    val ingame: IngameClient?

    /** Clientside configuration */
    val configuration: Configuration

    val user: ClientIdentity
    // /** Changes the game to a new world  */
    // fun changeWorld(world: WorldClient)
}

/** The game cient abstracted from a content/mod perspective. */
interface IngameClient : Client, GameContext {
    /** Returns a valid PlayerClient. */
    val player: LocalPlayer

    /** Returns the currently played world. */
    val world: WorldClient

    override val pluginManager: ClientPluginManager

    val particlesManager: ParticlesManager // TODO move in world
    val decalsManager: DecalsManager // TODO move in world

    /** Closes current world and exits to main menu  */
    fun exitToMainMenu()

    /** Closes current world and exits to main menu with an error message  */
    fun exitToMainMenu(errorMessage: String)
}

interface ClientIdentity {
    val name : String

    val authentificationMethod : AuthentificationMethod
    // val uuid: UUID //TODO: Move to real UUIDs
}