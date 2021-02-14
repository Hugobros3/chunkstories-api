package xyz.chunkstories.api.events.entity

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.DamageCause
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.events.CancellableEvent
import xyz.chunkstories.api.events.Event


/** When a EntityLiving damage() method is called  */
class EntityDamageEvent(val entity: Entity, val damageCause: DamageCause, var damageDealt: Float) : CancellableEvent()

/** This event is called upon confirmed death of a living entity. You can't and
 * shouldn't prevent it from dying here, instead use the EntityDamageEvent to
 * cancel the damage.  */
class EntityDeathEvent(val entity: Entity) : Event()

class EntityTeleportEvent(val entity: Entity, val newLocation: Location) : CancellableEvent()