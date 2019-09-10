//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

import xyz.chunkstories.api.client.IngameClient
import xyz.chunkstories.api.entity.Controller
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.exceptions.UnauthorizedClientActionException
import xyz.chunkstories.api.graphics.structs.Camera
import xyz.chunkstories.api.input.Input
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.util.kotlin.toVec3f
import xyz.chunkstories.api.world.WorldClientNetworkedRemote
import xyz.chunkstories.api.world.WorldMaster
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget
import org.joml.Vector3d
import org.slf4j.LoggerFactory
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.graphics.structs.makeCamera
import xyz.chunkstories.api.net.Interlocutor
import xyz.chunkstories.api.net.RemoteServer
import xyz.chunkstories.api.server.RemotePlayer
import java.lang.Exception

/** Holds information about who controls one entity.
 * Important note: this information is not serialized on purpose ! */
abstract class TraitControllable(entity: Entity) : Trait(entity), TraitNetworked<TraitControllable.ControllerUpdate> {
    override val traitName = "controller"
    private var actualController : Controller? = null

    var controller: Controller?
        get() = actualController
        set(newController) {
            // Client code isn't allowed to change the controller when running on a remote server,
            // the only method that change it in that scenario is the pull method below
            if (entity.world !is WorldMaster)
                throw UnauthorizedClientActionException("setController()")

            val formerController = this.controller
            this.actualController = newController

            // Tell the new controller the news
            if (newController != null) {
                sendMessage(newController, ControllerUpdate(true))
            }

            // Tell the former one he's no longer
            if (formerController != null && (controller == null || controller != formerController)) {
                sendMessage(formerController, ControllerUpdate(false))
            }
        }

    data class ControllerUpdate(val areYou: Boolean) : TraitMessage() {
        override fun write(dos: DataOutputStream) {
            dos.writeBoolean(areYou)
        }
    }

    override fun readMessage(dis: DataInputStream) = ControllerUpdate(dis.readBoolean())

    override fun processMessage(message: ControllerUpdate, from: Interlocutor) {
        // Only purely client worlds will accept these requests
        if (entity.world is WorldMaster) {
            if (from is Player) {
                // Terminate connections immediately
                if(from is RemotePlayer)
                    from.disconnect("Illegal controller set attempt, terminating client connection for $from")
                logger.info("Security alert: player $from tried to do an illegal action (push controller on master)")
            }
            return
        }

        val player = (entity.world.gameContext as IngameClient).player
        val previouslyControlledEntity = player.controlledEntity
        if(message.areYou) {
            if(previouslyControlledEntity == null) {
                player.controlledEntity = entity
                this.actualController = player
            } else if(previouslyControlledEntity == entity) {
                // ???
                logger.warn("Set as controller of the same entity twice")
            } else {
                player.controlledEntity = entity
                previouslyControlledEntity.traits[TraitControllable::class]?.controller = null
                this.actualController = player
            }
        } else {
            if(previouslyControlledEntity == entity) {
                this.actualController = null
                player.controlledEntity = null
            } else {
                logger.warn("Got told we don't control anymore an entity we didn't control in the first place")
            }
        }
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {
        if(subscriber == actualController)
            sendMessage(subscriber, ControllerUpdate(true))
    }

    companion object {
        val logger = LoggerFactory.getLogger("entity.traits.controller")
    }

    /** Returns the camera that this entity would see the world through  */
    open val camera: Camera
        get() {
            val client = entity.world.gameContext as? IngameClient ?: throw Exception("calling getCamera() on a non-client taskInstance is undefined behavior")

            val entityDirection = (entity.traits[TraitRotation::class]?.directionLookingAt ?: Vector3d(0.0, 0.0, 1.0)).toVec3f()
            val up = (entity.traits[TraitRotation::class]?.upDirection ?: Vector3d(0.0, 0.0, 1.0)).toVec3f()
            return client.makeCamera(entity.location, entityDirection, up, 90.0f)
        }

    /** Called whenever an input is pressed by the controller of this entity  */
    abstract fun onControllerInput(input: Input): Boolean

    /** Called on every rendered frame ( You want your camera control there, probably )  */
    abstract fun onEachFrame(): Boolean
}
