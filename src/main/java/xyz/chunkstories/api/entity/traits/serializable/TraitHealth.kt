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
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.entity.traits.TraitLoot
import xyz.chunkstories.api.events.entity.EntityDamageEvent
import xyz.chunkstories.api.events.entity.EntityDeathEvent
import xyz.chunkstories.api.events.player.PlayerDeathEvent
import xyz.chunkstories.api.physics.EntityHitbox
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.sound.SoundSource
import xyz.chunkstories.api.world.WorldMaster
import java.io.DataInputStream
import java.io.DataOutputStream

/** Any entity with this component is considered living, even if it's dead.
 * Handles health management and death  */
open class TraitHealth(entity: Entity) : Trait(entity), TraitSerializable, TraitNetworked<TraitHealth.HealthUpdate> {
    override val traitName = "health"

    val maxHealth: Float = entity.definition["maxHealth"].asFloat ?: 100.0f
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

    override fun processMessage(message: HealthUpdate, player: Player?) {
        if (entity.world is WorldMaster) {
            return
        }

        health = message.newHealth
    }

    override fun whenSubscriberRegisters(subscriber: Subscriber) {
        if (subscriber == entity.controller)
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
        entity.world.gameInstance.pluginManager.fireEvent(event)

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
        entity.world.gameInstance.pluginManager.fireEvent(entityDeathEvent)

        // Handles cases of controlled player death
        val controller = entity.controller
        if (controller != null && controller is Player) {
            if (entity.world is WorldMaster) {
                val event = PlayerDeathEvent(controller)
                entity.world.gameInstance.pluginManager.fireEvent(event)
                entity.world.gameInstance.broadcastMessage(event.deathMessage)
            }
        }

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
                entity.world.removeEntity(entity.id)
                return
            }
        }
    }

    open fun playDamageSound() {
        entity.world.soundManager.playSoundEffect("sounds/entities/flesh.ogg", SoundSource.Mode.NORMAL, entity.location, Math.random().toFloat() * 0.4f + 0.4f, 1f)
    }
}
