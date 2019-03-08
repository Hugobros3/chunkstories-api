//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.client

import xyz.chunkstories.api.GameContext
import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.graphics.GraphicsEngine
import xyz.chunkstories.api.graphics.Window
import xyz.chunkstories.api.graphics.systems.dispatching.DecalsManager
import xyz.chunkstories.api.gui.Gui
import xyz.chunkstories.api.net.AuthenticationMethod
import xyz.chunkstories.api.particles.ParticlesManager
import xyz.chunkstories.api.plugin.PluginManager
import xyz.chunkstories.api.util.configuration.Configuration
import xyz.chunkstories.api.world.WorldClient

/** The game client abstracted from a generic runtime perspective (not necessarily in game) */
interface Client {
    val gameWindow: Window
    val graphics: GraphicsEngine
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

    override val pluginManager: PluginManager

    val particlesManager: ParticlesManager // TODO move in world
    val decalsManager: DecalsManager // TODO move in world

    /** Closes current world and exits to main menu  */
    fun exitToMainMenu()

    /** Closes current world and exits to main menu with an error message  */
    fun exitToMainMenu(errorMessage: String)
}

interface ClientIdentity {
    val name : String

    val authenticationMethod : AuthenticationMethod
    // val uuid: UUID //TODO: Move to real UUIDs
}