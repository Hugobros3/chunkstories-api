//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.events

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.HashSet
import kotlin.reflect.KProperty1
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.memberProperties

class EventListeners {
    lateinit var listeners: Array<RegisteredListener>
        internal set
    internal var unbaked: MutableSet<RegisteredListener> = HashSet()

    internal var children: MutableSet<EventListeners> = HashSet()
    lateinit var childrens: Array<EventListeners>
        internal set

    internal val owningEventName: String

    constructor() {
        owningEventName = "Anonymous event"
        bake()
        bakeChildren()
    }

    constructor(eventClass: Class<out Event>) {
        owningEventName = eventClass.name
        //println("Building EventListener for $eventClass")
        var son = eventClass
        // EventListeners sonListener = this;

        try {
            while (true) {
                val dad = son.superclass
                if (Event::class.java.isAssignableFrom(dad)) {
                    // Security
                    if (Event::class.java == dad || CancellableEvent::class.java == dad)
                        break

                    //println("Found superclass $dad")

                    val daddyEars =  try {
                        val m = dad.getMethod("getListenersStatic")

                        val o = m.invoke(null)
                        o as EventListeners
                    } catch (npe: NullPointerException) {
                        //println("Stopping inheritance lookup; stepped on NPE")
                        npe.printStackTrace()
                        break
                    } catch (nsme: NoSuchMethodException) {
                        val kotlinClass = (dad as Class<out Event>).kotlin
                        val companion = kotlinClass.companionObject
                        val instance = kotlinClass.companionObjectInstance
                        val property = companion!!.memberProperties.find { it.name == "listenersStatic" }!! as KProperty1<Any, Any>
                        val listeners = property.get(instance!!) as EventListeners
                        listeners
                    }

                    // Notice me daddy
                    daddyEars.declareChildren(this)

                    // Oedipe time
                    son = dad as Class<out Event>

                } else {
                    break
                }
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            throw RuntimeException("Error while setting up events inheritance")
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            throw RuntimeException("Error while setting up events inheritance")
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
            throw RuntimeException("Error while setting up events inheritance")
        }

        bake()
        bakeChildren()
    }

    internal fun declareChildren(heyDad: EventListeners) {
        children.add(heyDad)
        bakeChildren()
    }

    fun registerListener(RegisteredListener: RegisteredListener) {
        unbaked.add(RegisteredListener)
        bake()
    }

    fun unRegisterListener(RegisteredListener: RegisteredListener) {
        unbaked.remove(RegisteredListener)
        bake()
    }

    private fun bakeChildren() {
        childrens = children.toTypedArray()
    }

    private fun bake() {
        listeners = unbaked.toTypedArray()
        // TODO weight by priority
    }
}
