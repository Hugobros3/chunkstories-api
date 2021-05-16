//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.client

import xyz.chunkstories.api.Engine
import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.graphics.GraphicsEngine
import xyz.chunkstories.api.graphics.Window
import xyz.chunkstories.api.gui.Gui
import xyz.chunkstories.api.net.AuthenticationMethod
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.player.PlayerID
import xyz.chunkstories.api.plugin.PluginManager
import xyz.chunkstories.api.util.configuration.Configuration
import xyz.chunkstories.api.world.GameInstance
import xyz.chunkstories.api.world.World

/** The game client abstracted from a generic runtime perspective (not necessarily in game) */
interface Client: Engine {
    val gameWindow: Window
    val graphics: GraphicsEngine
    val gui: Gui

    val content: Content

    val soundManager: ClientSoundManager
    val inputsManager: ClientInputsManager

    /** If we are ingame this will be set ! */
    val ingame: IngameClient?

    val configuration: Configuration

    val user: ClientIdentity
}

interface IngameClient : GameInstance {
    override val engine: Client

    val player: Player

    override val world: World
    override val pluginManager: PluginManager

    /** Closes current world and exits to main menu  */
    fun exitToMainMenu()

    /** Closes current world and exits to main menu with an error message  */
    fun exitToMainMenu(errorMessage: String)
}

interface ClientIdentity {
    val name : String
    val id: PlayerID

    val authenticationMethod : AuthenticationMethod
}