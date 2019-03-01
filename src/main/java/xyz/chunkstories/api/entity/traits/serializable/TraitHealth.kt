//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.IOException

import org.joml.Vector3d

import xyz.chunkstories.api.GameContext
import xyz.chunkstories.api.entity.Controller
import xyz.chunkstories.api.entity.DamageCause
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.events.entity.EntityDamageEvent
import xyz.chunkstories.api.events.entity.EntityDeathEvent
import xyz.chunkstories.api.events.player.PlayerDeathEvent
import xyz.chunkstories.api.physics.EntityHitbox
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.server.Server
import xyz.chunkstories.api.world.WorldMaster
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget

/** Any entity with this component is considered living, even if it's dead.
 * Handles health management and death  */
open class TraitHealth(entity: Entity) : TraitSerializable(entity) {
    var maxHealth = 100f
    private var health: Float = 0.toFloat()

    private var damageCooldown: Long = 0

    var lastDamageCause: DamageCause? = null
        private set
    private var deathDespawnTimer: Long = 6000

    val isDead: Boolean
        get() = getHealth() <= 0

    init {
        this.health = maxHealth
    }

    fun getHealth(): Float {
        return health
    }

    fun setHealth(health: Float) {
        val wasntDead = this.health > 0.0
        this.health = health

        if (health <= 0.0 && wasntDead)
            handleDeath()

        if (entity.getWorld() is WorldMaster) {
            if (health > 0.0)
                this.pushComponentController()
            else
                this.pushComponentEveryone()
        }
    }

    open fun damage(cause: DamageCause, damage: Float): Float {
        return damage(cause, null, damage)
    }

    open fun damage(cause: DamageCause, hitLocation: EntityHitbox?, damage: Float): Float {
        if (damageCooldown > System.currentTimeMillis())
            return 0f

        val event = EntityDamageEvent(entity, cause, damage)
        entity.getWorld().gameLogic.pluginsManager.fireEvent(event)

        if (!event.isCancelled) {
            applyDamage(event.damageDealt)
            lastDamageCause = cause

            damageCooldown = System.currentTimeMillis() + cause.cooldownInMs

            val damageDealt = event.damageDealt

            // Applies knockback
            if (cause is Entity) {
                // Only runs if the entity do have a velocity
                entity.traits[TraitVelocity::class]?.let { ev ->

                    val attacker = cause as Entity
                    val attackKnockback = entity.location.sub(attacker.location.add(0.0, 0.0, 0.0))
                    attackKnockback.y = 0.0
                    attackKnockback.normalize()

                    val knockback = Math.max(1.0, Math.pow(damageDealt.toDouble(), 0.5)).toFloat()

                    attackKnockback.mul(knockback / 50.0)
                    attackKnockback.y = knockback / 50.0

                    ev.addVelocity(attackKnockback)
                }

            }

            return damageDealt
        }

        return 0f
    }

    fun applyDamage(dmg: Float) {
        val wasntDead = health > 0.0
        this.health -= dmg

        if (health <= 0.0 && wasntDead)
            handleDeath()

        if (entity.getWorld() is WorldMaster) {
            if (health > 0.0)
                this.pushComponentController()
            else
                this.pushComponentEveryone()
        }
    }

    private fun handleDeath() {
        val entityDeathEvent = EntityDeathEvent(entity)
        entity.getWorld().gameLogic.pluginsManager.fireEvent(entityDeathEvent)

        // Handles cases of controlled player death
        entity.traits[TraitControllable::class]?.let { ec ->
            val controller = ec.controller
            if (controller != null) {
                controller.controlledEntity = null

                // Serverside stuff
                if (controller is Player && entity.getWorld() is WorldMaster) {
                    val player = controller as Player

                    val event = PlayerDeathEvent(player)
                    entity.getWorld().gameLogic.pluginsManager.fireEvent(event)

                    // When a player dies, delete his save as well
                    val playerSavefile = File((entity.getWorld() as WorldMaster).folderPath + "/players/" + player.name.toLowerCase() + ".csf")
                    if (playerSavefile.exists()) {
                        // Player save file is deleted upon death
                        playerSavefile.delete()
                    }

                    if (event.deathMessage != null) {
                        val gc = player.context
                        if (gc is Server)
                            gc.broadcastMessage(event.deathMessage)
                    }
                } else {
                    // Weird, undefined cases ( controller wasn't a player, maybe some weird mod
                    // logic here
                }
            }
        }
    }

    @Throws(IOException::class)
    public override fun push(destinator: StreamTarget, dos: DataOutputStream) {
        dos.writeFloat(health)
    }

    @Throws(IOException::class)
    public override fun pull(from: StreamSource, dis: DataInputStream) {
        health = dis.readFloat()
    }

    fun removeCorpseAfterDelay() {
        if (isDead) {
            deathDespawnTimer--
            if (deathDespawnTimer < 0) {
                entity.getWorld().removeEntity(entity)
                return
            }
        }
    }

    /* public float getMaxHealth() { return maxHealth; } */
}
