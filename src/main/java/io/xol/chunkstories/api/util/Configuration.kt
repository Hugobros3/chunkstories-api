//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.util

import io.xol.chunkstories.api.GameContext
import io.xol.chunkstories.api.dsl.OptionsDeclarationCtx
import io.xol.chunkstories.api.events.config.OptionSetEvent
import io.xol.chunkstories.api.input.Input
import io.xol.chunkstories.api.math.Math2
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

/** Chunk Stories offer a relatively fancy way of managing user-configurable options, by way of the
 * **Configurations** API. This API allows you to declare *Configurations*, that hold a bunch of *Options*
 * that can be modified accorded to the option's value type and range. */
class Configuration(val context: GameContext?, val file: File) {
    val optionsMap = mutableMapOf<String, Option<*>>()
    val options: Set<Option<*>>
        get() = optionsMap.values.toSet()

    fun addOptions(declarations: OptionsDeclarationCtx.() -> Unit) {
        val ctx = OptionsDeclarationCtx(this, "")
        ctx.apply(declarations)
    }

    fun registerOption(option: Option<*>) = optionsMap.put(option.name, option)

    operator fun plusAssign(option: Option<*>) {
        registerOption(option)
    }

    abstract inner class Option<T : Any>(val name: String, val defaultValue: T) {
        var value: T = defaultValue

        private val hooks = mutableListOf<Option<T>.() -> Unit>()

        /** Try setting the value of that option to the argument. Will fire an
         * EventOptionSet, returns true when successful */
        open fun trySetting(value: T): Boolean {
            val event = OptionSetEvent(this)
            context?.pluginManager?.fireEvent(event)
            if (!event.isCancelled) {
                this.value = value
                hooks.forEach { it.invoke(this) }
                return true
            }

            return false
        }

        fun addHook(hook: Option<T>.() -> Unit) = hooks.add(hook)

        fun addHook(hook : OptionHook<T>) = hooks.add {
            hook.call(this)
        }
    }

    interface OptionHook<T : Any> {
        fun call(option: Option<T>) : Unit
    }

    open inner class OptionString(name: String, defaultValue: String) : Option<String>(name, defaultValue)

    open inner class OptionBoolean(name: String, defaultValue: Boolean) : Option<Boolean>(name, defaultValue) {
        fun toggle() = trySetting(!value)
    }

    open inner class OptionInt(name: String, defaultValue: Int) : Option<Int>(name, defaultValue)

    open inner class OptionDouble(name: String, defaultValue: Double) : Option<Double>(name, defaultValue)

    inner class OptionDoubleRange(name: String, defaultValue: Double, val minimumValue: Double, val maximumValue: Double, val granularity: Double) : OptionDouble(name, defaultValue) {
        override fun trySetting(value: Double): Boolean {
            val clampedValue = Math2.clampd(value, minimumValue, maximumValue)
            val actuallySettingThisValue = if (granularity != 0.0) {
                val inverted = 1.0 / granularity
                val rounded = Math.round(clampedValue * inverted).toDouble()
                rounded * inverted
            } else clampedValue

            return super.trySetting(actuallySettingThisValue)
        }
    }

    inner class OptionMultiChoice(name: String, defaultValue: String, val possibleChoices: List<String>) : OptionString(name, defaultValue) {
        override fun trySetting(value: String): Boolean {
            if (!possibleChoices.contains(value))
                return false
            return super.trySetting(value)
        }
    }

    inner class OptionMultiChoiceInt(name: String, defaultValue: Int, val possibleChoices: List<Int>) : OptionInt(name, defaultValue) {
        override fun trySetting(value: Int): Boolean {
            if (!possibleChoices.contains(value))
                return false
            return super.trySetting(value)
        }
    }

    /** Created when an input is *not* declared using the 'hidden' flag in a .inputs file! */
    inner class OptionInput(name: String, defaultValue: Input) : Option<Input>(name, defaultValue)

    /** Looks for a certain option. */
    operator fun <T : Option<*>> get(optionName: String): T? {
        val option = optionsMap.get(optionName)
        return if (option != null) option as T else null
    }

    /** Stores the value of the options in a file */
    fun save(file: File) {
        file.parentFile.mkdirs();

        val properties = Properties()
        optionsMap.values.forEach { properties[it.name] = it.value }
        properties.store(FileWriter(file), "File autogenerated on ${Date()}")
    }

    fun save() = save(file)

    /** Loads the actual values of the options from a file and casts them appropriately */
    fun load(file: File) {
        file.parentFile.mkdirs()
        if(!file.exists())
            return

        val properties = Properties()
        properties.load(FileReader(file))
        properties.forEach { (k, value) -> optionsMap.get(k)?.let {
            val value = value as String
            when(it) {
                is OptionString -> it.trySetting(value)
                is OptionDouble -> it.trySetting(value.toDouble())
                is OptionInt -> it.trySetting(value.toInt())
                is OptionBoolean -> it.trySetting(value.toBoolean())
                else -> throw Exception("No idea how to load $it")
            }
        } }
    }

    fun load() = load(file)

    //val values = QuickConfigValueAccess()

    /*inline fun <reified T : Any> Configuration.Option<T>.value() : T {
        return when {
            0 is T -> getIntValue(configNode) as T

            0.0 is T -> getDoubleValue(configNode) as T

            false is T -> getBooleanValue(configNode) as T

            "0.0" is T -> getValue(configNode) as T

            else -> throw Exception("You can only quick-access Boolean, Int, Double and String properties !")
        }
    }*/


    fun getIntValue(configNode: String) : Int = ((optionsMap[configNode] as? OptionInt)?.value ?: 0)

    fun getDoubleValue(configNode: String) : Double = ((optionsMap[configNode] as? OptionDouble)?.value ?: 0.0)

    fun getBooleanValue(configNode: String) : Boolean = ((optionsMap[configNode] as? OptionBoolean)?.value ?: false)

    fun getValue(configNode: String) : String = ((optionsMap[configNode] as? OptionString)?.value ?: "")

}
