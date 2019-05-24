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
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3d
import org.joml.Vector3f
import org.slf4j.LoggerFactory
import java.lang.Exception

/** Holds information about who controls one entity  */
abstract class TraitControllable(entity: Entity) : TraitSerializable(entity) {
    private var actualController : Controller? = null
    var controller: Controller?
        get() = actualController
        set(value) {
            // Client code isn't allowed to change the controller when running on a remote server,
            // the only method that change it in that scenario is the pull method below
            if (entity.world !is WorldMaster)
                throw UnauthorizedClientActionException("setController()")

            setControllerInternal(value)
        }

    private fun setControllerInternal(newController: Controller?) {
        val formerController = this.controller
        this.actualController = newController
        // Tell the new controller the news
        if (controller != null) {
            pushComponent(controller)
        }
        // Tell the former one he's no longer
        if (formerController != null && (controller == null || controller != formerController)) {
            pushComponent(formerController)
        }
    }

    /** Returns the camera that this entity would see the world through  */
    open val camera: Camera
        get() {
            val client = entity.world.gameContext as? IngameClient ?: throw Exception("calling getCamera() on a non-client taskInstance is undefined behavior")

            val fov = (90.0 / 360.0 * (Math.PI * 2)).toFloat()
            val aspect = client.gameWindow.width.toFloat() / client.gameWindow.height.toFloat()
            val projectionMatrix = Matrix4f().perspective(fov, aspect, 0.1f, 2000f, true)

            val location = entity.location
            val cameraPosition = location.toVec3f()

            //cameraPosition.y += 1.8f

            val entityDirection = (entity.traits[TraitRotation::class]?.directionLookingAt ?: Vector3d(0.0, 0.0, 1.0)).toVec3f()
            val entityLookAt = Vector3f(cameraPosition).add(entityDirection)

            val up = (entity.traits[TraitRotation::class]?.upDirection ?: Vector3d(0.0, 0.0, 1.0)).toVec3f()

            val viewMatrix = Matrix4f()
            viewMatrix.lookAt(cameraPosition, entityLookAt, up)

            return Camera(cameraPosition, entityDirection, up, fov, viewMatrix, projectionMatrix)
        }

    /** Called whenever an input is pressed by the controller of this entity  */
    abstract fun onControllerInput(input: Input): Boolean

    /** Called on every rendered frame ( You want your camera control there, probably )  */
    abstract fun onEachFrame(): Boolean

    /*
	Methods below are concerned with synchronizing the controller state across client/server, you can safely
	ignore them if you're just interested in creating a custom entity type.
	 */
    @Throws(IOException::class)
    public override fun push(to: StreamTarget, dos: DataOutputStream) {
        // We write if the controller exists and if so we tell the uuid
        dos.writeBoolean(controller != null)
        if (controller != null)
            dos.writeLong(controller!!.uuid)
    }

    @Throws(IOException::class)
    public override fun pull(from: StreamSource, dis: DataInputStream) {
        var controllerUUID: Long = 0
        val isControllerNotNull = dis.readBoolean()
        if (isControllerNotNull)
            controllerUUID = dis.readLong()

        // Only purely client worlds will accept these requests
        if (entity.world !is WorldClientNetworkedRemote) {
            // Terminate connections immediately
            if (from is Player) {
                //from.disconnect("Illegal controller set attempt, terminating client connection for $from")
                logger.info("Security alert: player $from tried to do an illegal action (push controller on master)")
            }
            return
        }

        val player = (entity.world.gameContext as IngameClient).player
        if (isControllerNotNull) {
            val clientUUID = player.uuid
            println("Entity $entity is now under control of $controllerUUID me=$clientUUID")
            // This update tells us we are now in control of this entity
            if (clientUUID == controllerUUID) {
                // TODO sort out local hosted worlds properly ?
                // Client.getInstance().getServerConnection().subscribe(entity);
                setControllerInternal(player)

                player.controlledEntity = entity
                logger.debug("The client is now in control of entity $entity")
            } else {
                // If we receive a different UUID than ours in a EntityComponent change, it
                // means that we don't control it anymore and someone else does.
                if (player.controlledEntity != null && player.controlledEntity == entity) {
                    player.controlledEntity = null

                    // Client.getInstance().getServerConnection().unsubscribe(entity);
                    setControllerInternal(null)
                    logger.debug("Lost control of entity $entity to $controllerUUID")
                }
            }
        } else {
            // Null controller also means we lose control over then entity
            if (player.controlledEntity != null && player.controlledEntity == entity) {
                player.controlledEntity = null

                // Client.getInstance().getServerConnection().unsubscribe(entity);
                setControllerInternal(null)
                logger.debug("Lost control of entity $entity")
            }

        }
    }

    companion object {
        val logger = LoggerFactory.getLogger("entity.traits.controller")
    }
}
