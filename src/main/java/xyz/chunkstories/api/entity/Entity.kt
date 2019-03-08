//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.entity.traits.Trait
import xyz.chunkstories.api.entity.traits.serializable.TraitLocation
import xyz.chunkstories.api.net.packets.PacketEntity
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.util.*
import xyz.chunkstories.api.world.World
import xyz.chunkstories.api.world.serialization.StreamTarget
import java.util.*
import kotlin.reflect.KClass
import java.util.HashSet

abstract class Entity(val definition: EntityDefinition, val world: World) {
    //var location : Location = Location(world, .0, .0, .0)
    var location: Location
        get() = traitLocation.get()
        set(value) = traitLocation.set(value)

    //fun getWorld() = world

    var UUID : Long = -1L
        set(value) {
            if(field == -1L)
                field = value
            else
                throw Exception("You can't re-assign UUIDs !")
        }

    var initialized = false
        set(value) = if(!value) throw Exception("Can't de-initialize an entity!") else field = value

    @JvmField
    val traits : Traits = Traits()

    @JvmField
    val traitLocation = TraitLocation(this, Location(world, .0, .0, .0))

    @JvmField
    val subscribers = Subscribers()
    interface Traits_ : Map<Class<Trait>, Trait> {
        fun registerTrait(trait : Trait) : Int

        fun has(trait : Trait) : Boolean

        operator fun <T: Trait> get(traitClass: KClass<T>) = this[traitClass.java]
        operator fun <T: Trait> get(traitClass: Class<T>) : T?

        fun <T : Trait> with(traitClass: Class<T>, action: VoidAction<T>)

        fun <T: Trait, R> tryWith(traitClass: KClass<T>, action: T.() -> R) : R
        fun <T: Trait, R> tryWith(traitClass: Class<T>, action : ReturnsAction<T, R>) : R

        fun <T: Trait> tryWithBoolean(traitClass: KClass<T>, action: T.() -> Boolean) : Boolean
        fun <T: Trait> tryWithBoolean(traitClass: Class<T>, action : BooleanAction<T>) : Boolean

        fun all() : Collection<Trait>

        fun byId() : Array<Trait>
    }

    fun afterIntialization() {
        if (initialized)
            throw RuntimeException("afterIntialization() is supposed to be called only once.")

        //Creates a static array and set for the traits
        val set = HashSet<Trait>()
        set.addAll(traits.map.values)
        traits.set = Collections.unmodifiableSet(set)

        val array = arrayOfNulls<Trait>(set.size)
        set.forEach {array[it.id()] = it}
        traits.byId = array.requireNoNulls()

        // Set the initialized flag so the above structures become immutable
        initialized = true
    }

    inner class Traits {
        var map: MutableMap<Class<out Trait>, Trait> = HashMap()

        lateinit var byId: Array<Trait>
        lateinit var set: Set<Trait>

        private var count = 0

        fun registerTrait(trait: Trait): Int {
            if (initialized)
                throw RuntimeException("You can't register traits after the entity initializes.")

            if (map[trait.javaClass] === trait) {
                world.gameLogic.gameContext.logger().warn(
                        "Tried to register the same trait twice" + " (hint: don't call registertrait yourself, the superconstructor does it already )")
                return trait.id()
            }

            //println("registering trait $trait")
            var id = purge(trait)
            if (id == -1)
                id = count++
            //println("got id $id")

            map[trait.javaClass] = trait

            // We allow refering to a trait by it's superclass so we bake in all
            // superclasses that trait encompasses
            var c: Class<*> = trait.javaClass
            while (true) {
                if (c.getDeclaredAnnotationsByType(Specialized::class.java).size != 0) {
                    // println(c.getSimpleName()+" : this is a specialized class, no overshadowing parents");
                    break
                }
                c = c.superclass
                if (c == Trait::class.java)
                    break // stop there

                if (c.getDeclaredAnnotationsByType(Generalized::class.java).size != 0) {
                    // println(c.getSimpleName()+" : this is a generalized class, not overriding that.");
                    break
                }

                // System.out.println("registering trait with superclass: "+c);
                map[c as Class<out Trait>] = trait
            }

            return id
        }

        private fun purge(trait: Trait): Int {
            var remove: Trait? = null

            // System.out.println("Purging matching traits...");
            var c: Class<*> = trait.javaClass
            while (true) {
                if (c.getDeclaredAnnotationsByType(Specialized::class.java).isNotEmpty()) {
                    // System.out.println(c.getSimpleName()+" : this is a specialized class, no
                    // removing parents");
                    break
                }

                val comp = map[c]
                if (comp != null) {
                    // System.out.println("Found conflicting trait: "+comp + "with id
                    // "+comp.id());
                    remove = comp
                    break
                }

                c = c.superclass
                if (c == Trait::class.java)
                    break
                if (c.getDeclaredAnnotationsByType(Generalized::class.java).size != 0) {
                    // System.out.println(c.getSimpleName()+" : this is a generalized class,
                    // stopping purge");
                    break
                }
            }

            if (remove != null) {
                println("Purging matching trait: $remove")
                val i = map.entries.iterator()
                while (i.hasNext()) {
                    val e = i.next()
                    if (e.value === remove) {
                        // System.out.println("Purging : "+e.getKey());
                        i.remove()
                    }
                }

                println("returning purged id:" + remove.id())
                return remove.id()
            }

            return -1
        }

        fun has(traitType: Class<out Trait>): Boolean {
            return map.containsKey(traitType)
        }

        operator fun <T : Trait> get(trait: Class<T>): T? {
            return map[trait] as? T?
        }

        operator fun <T : Trait> get(trait: KClass<T>) = this[trait.java]

        /** Tries to find a trait matching this type, executes some action on it and
         * returns the result. Returns null if no such trait was found.  */
        fun <T : Trait, R> tryWith(traitType: Class<T>, action: ReturnsAction<T, R>): R? {
            val trait = map[traitType] as? T?
            return if (trait != null) {
                action.run(trait)
            } else null
        }

        /** Enables to use Kotlin-style syntax */
        fun <T: Trait, R> tryWith(traitClass: KClass<T>, action: T.() -> R) = tryWith(traitClass.java, ReturnsAction<T, R> {
            it?.let(action)
        })

        /** Tries to find a trait matching this type, executes some boolean action
         * on it and returns the result. Returns false if no such trait was
         * found.  */
        fun <T : Trait> tryWithBoolean(traitType: Class<T>, action: BooleanAction<T>): Boolean {
            val trait = map[traitType] as? T?
            return if (trait != null) {
                action.run(trait)
            } else false
        }

        fun <T: Trait> tryWithBoolean(traitClass: KClass<T>, action: T.() -> Boolean) = tryWithBoolean(traitClass.java, BooleanAction{
            it?.let(action) ?: false
        })

        /** Tries to find a trait matching this type, executes some action on it and
         * returns true. Returns false if no such trait was found.  */
        fun <T : Trait> with(traitType: Class<T>, action: VoidAction<T>): Boolean {
            val trait = map[traitType] as? T?
            if (trait != null) {
                action.run(trait)
                return true
            }
            return false
        }

        fun <T : Trait> with(traitClass: KClass<T>, action: T.() -> Unit) = with(traitClass.java, VoidAction {
            it.apply(action)
        })

        fun byId(): Array<Trait> = byId

        fun all(): Set<Trait> = set

        override fun toString(): String {
            var ok = ""
            for (trait in all())
                ok += "(" + safename(trait.javaClass) + ", " + trait.id() + ")" + ", "
            return all()!!.size.toString() + "{" + ok + "}"
        }

        private fun safename(klass: Class<*>): String {
            val simpleName = klass.simpleName
            return if (simpleName == "") safename(klass.superclass) else simpleName
        }
    }

    inner class Subscribers : HashSet<Subscriber>() {
        //private val subscribers = ConcurrentHashMap.newKeySet<Subscriber>()

        /** Internal method called by a [subscriber] ( like a [Player] ... )
         * when he subscribe() to this entity  */
        fun register(subscriber: Subscriber): Boolean {
            // If it didn't already contain the subscriber ...
            return if (add(subscriber)) {
                //TODO some logic here maybe
                true
            } else false
        }

        /** Internal method called by a [subscriber] ( like a [Player] ... )
         * when he unsubscribe() to this entity  */
        fun unregister(subscriber: Subscriber): Boolean {
            if (remove(subscriber)) {
                subscriber.pushPacket(PacketEntity(this@Entity))

                // PacketEntity checks if the subscriber is registered in the entity it's about
                // to update him about, if he's not he'll send a special flag to tell the
                // subscriber
                // to remove the entity from his view of the world
                return true
            }
            return false
        }

        fun isRegistered(subscriber: StreamTarget): Boolean {
            return subscribers.contains(subscriber)
        }

        fun all(): Set<Subscriber> {
            return Collections.unmodifiableSet(this)
        }
    }

    abstract fun tick() : Unit

    open fun getBoundingBox() = Box(1.0, 1.0, 1.0)

    fun getTranslatedBoundingBox() = Box(getBoundingBox()).translate(location)
}
