//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.input.Input
import xyz.chunkstories.api.entity.traits.Trait

/** Holds information about who controls one entity.
 * Important note: this information is not serialized on purpose ! */
abstract class TraitControllable(entity: Entity) : Trait(entity)/*, TraitNetworked<TraitControllable.ControllerUpdate>*/ {
    override val traitName = "controller"
    //private var actualController: Controller? = null

    //var controller: Controller? = null
    /*    get() = actualController
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
        }*/

    /*data class ControllerUpdate(val setAsController: Boolean) : TraitMessage() {
        override fun write(dos: DataOutputStream) {
            dos.writeBoolean(setAsController)
        }
    }*/

    /*override fun readMessage(dis: DataInputStream) = ControllerUpdate(dis.readBoolean())

    override fun processMessage(message: ControllerUpdate, player: IngamePlayer?) {
        // Only purely client worlds will accept these requests
        if (entity.world is WorldMaster) {
            assert(player != null)
            // Terminate such connections immediately
            with(entity.world.gameInstance) {
                player!!.disconnect("Illegal controller set attempt, terminating client connection for $player")
                logger.info("Security alert: $player tried to do an illegal action (push controller on master)")
            }
            return
        }

        val client = entity.world.gameInstance as IngameClient
        val previouslyControlledEntity = (client.player as? IngamePlayer)?.entity
        if (message.setAsController) {
            when (previouslyControlledEntity) {
                null -> {
                    this.actualController = client.startPlayingAs_(entity)
                }
                entity -> {
                    // ???
                    throw Exception("Set as controller of the same entity twice")
                }
                else -> {
                    previouslyControlledEntity.traits[TraitControllable::class]?.controller = null
                    this.actualController = client.startPlayingAs_(entity)
                }
            }
        } else {
            if (previouslyControlledEntity == entity) {
                this.actualController = null
                client.startSpectating_()
            } else {
                logger.warn("Got told we don't control anymore an entity we didn't control in the first place")
            }
        }
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {
        if (subscriber == actualController)
            sendMessage(subscriber, ControllerUpdate(true))
    }*/

    /*companion object {
        val logger: Logger = LoggerFactory.getLogger("entity.traits.controller")
    }*/

    /** Called whenever an input is pressed by the controller of this entity  */
    abstract fun onControllerInput(input: Input): Boolean

    /** Called on every rendered frame */
    abstract fun onEachFrame(): Boolean
}