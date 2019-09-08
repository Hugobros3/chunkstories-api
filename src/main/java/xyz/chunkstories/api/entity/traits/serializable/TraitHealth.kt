//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import org.joml.Vector3d
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asFloat
import xyz.chunkstories.api.entity.DamageCause
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.EntityDroppedItem
import xyz.chunkstories.api.entity.Subscriber
import xyz.chunkstories.api.entity.traits.TraitLoot
import xyz.chunkstories.api.events.entity.EntityDamageEvent
import xyz.chunkstories.api.events.entity.EntityDeathEvent
import xyz.chunkstories.api.events.player.PlayerDeathEvent
import xyz.chunkstories.api.net.Interlocutor
import xyz.chunkstories.api.physics.EntityHitbox
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.server.Server
import xyz.chunkstories.api.sound.SoundSource
import xyz.chunkstories.api.world.WorldMaster
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.IOException

/** Any entity with this component is considered living, even if it's dead.
 * Handles health management and death  */
open class TraitHealth(entity: Entity) : TraitSerializable(entity), TraitNetworked<TraitHealth.HealthUpdate> {
    val maxHealth : Float = entity.definition["maxHealth"].asFloat ?: 100.0f
    var health: Float = entity.definition["startHealth"].asFloat ?: 100.0f
        set(value) {
            val wasAliveBefore = this.health > 0.0
            field = value

            if (health <= 0.0 && wasAliveBefore)
                handleDeath()

            if (entity.world is WorldMaster) {
                if (health > 0.0)
                    sendMessageController(HealthUpdate(value))
                else // Let everyone know when an entity is dead
                    sendMessageAllSubscribers(HealthUpdate(value))
            }
        }

    private var damageCooldown: Long = 0

    var lastDamageCause: DamageCause? = null
        private set
    private var deathDespawnTimer: Long = 60 * 3 // stays for 3s (180 ticks at 60tps)

    val isDead: Boolean
        get() = health <= 0

    data class HealthUpdate(val newHealth: Float) : TraitMessage() {
        override fun write(dos: DataOutputStream) {
            dos.writeFloat(newHealth)
        }
    }

    override fun readMessage(dis: DataInputStream) = HealthUpdate(dis.readFloat())

    override fun processMessage(message: HealthUpdate, from: Interlocutor) {
        if(entity.world is WorldMaster) {
            return
        }

        health = message.newHealth
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {
        if(subscriber == entity.traits[TraitControllable::class]?.controller)
            sendMessageController(HealthUpdate(health))
    }

    override fun deserialize(json: Json) {
        health = json.asFloat ?: health
    }

    override fun serialize() = Json.Value.Number(health.toDouble())

    open fun damage(cause: DamageCause, damage: Float): Float {
        return damage(cause, null, damage)
    }

    open fun damage(cause: DamageCause, hitLocation: EntityHitbox?, damage: Float): Float {
        if (damageCooldown > System.currentTimeMillis())
            return 0f

        val event = EntityDamageEvent(entity, cause, damage)
        entity.world.gameLogic.pluginsManager.fireEvent(event)

        if (!event.isCancelled) {
            applyDamage(event.damageDealt)
            playDamageSound()
            lastDamageCause = cause

            damageCooldown = System.currentTimeMillis() + cause.cooldownInMs

            return event.damageDealt
        }

        return 0f
    }

    private fun applyDamage(damage: Float) {
        val wasntDead = health > 0.0
        this.health -= damage

        if (health <= 0.0 && wasntDead)
            handleDeath()

        if (entity.world is WorldMaster) {
            if (health > 0.0)
                sendMessageController(HealthUpdate(health))
            else // Let everyone know when an entity is dead
                sendMessageAllSubscribers(HealthUpdate(health))
        }
    }

    private fun handleDeath() {
        val entityDeathEvent = EntityDeathEvent(entity)
        entity.world.gameLogic.pluginsManager.fireEvent(entityDeathEvent)

        // Handles cases of controlled player death
        entity.traits[TraitControllable::class]?.let { ec ->
            val controller = ec.controller
            if (controller != null) {
                controller.controlledEntity = null

                // Serverside stuff
                if (controller is Player && entity.world is WorldMaster) {
                    val player = controller as Player

                    val event = PlayerDeathEvent(player)
                    entity.world.gameLogic.pluginsManager.fireEvent(event)

                    // When a player dies, delete his save as well
                    val playerSavefile = File((entity.world as WorldMaster).folderPath + "/players/" + player.name.toLowerCase() + ".csf")
                    if (playerSavefile.exists()) {
                        // Player save file is deleted upon death
                        playerSavefile.delete()
                    }

                    (player.controlledEntity?.world?.gameContext as? Server)?.broadcastMessage(event.deathMessage)
                } else {
                    // Weird, undefined cases ( controller wasn't a player, maybe some weird mod
                    // logic here
                }
            }
        }

        val world = entity.world
        // Drop items !
        for (trait in entity.traits.all()) {
            if (trait is TraitInventory) {
                for (itemPile in trait.inventory.contents) {
                    val velocity = Vector3d(Math.random() * 0.2 - 0.1, Math.random() * 0.1 + 0.1, Math.random() * 0.2 - 0.1)
                    EntityDroppedItem.spawn(itemPile.item, itemPile.amount, this.entity.location, velocity)
                }
                trait.inventory.clear()
            }
        }

        // Handle loot
        entity.traits[TraitLoot::class]?.let {
            val loot = it.lootTable.spawn(lastDamageCause)
            for ((item, amount) in loot) {
                val velocity = Vector3d(Math.random() * 0.2 - 0.1, Math.random() * 0.1 + 0.1, Math.random() * 0.2 - 0.1)
                EntityDroppedItem.spawn(item, amount, this.entity.location, velocity)
            }
        }
    }

    override fun tick() {
        if (entity.world is WorldMaster) {
            removeCorpseAfterDelay()
        }
    }

    private fun removeCorpseAfterDelay() {
        if (isDead) {
            deathDespawnTimer--
            if (deathDespawnTimer < 0) {
                entity.world.removeEntity(entity)
                return
            }
        }
    }

    open fun playDamageSound() {
        entity.world.soundManager.playSoundEffect("sounds/entities/flesh.ogg", SoundSource.Mode.NORMAL, entity.location, Math.random().toFloat() * 0.4f + 0.4f, 1f)
    }
}
