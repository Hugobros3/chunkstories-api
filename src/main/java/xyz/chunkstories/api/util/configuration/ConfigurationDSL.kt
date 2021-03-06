//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.util.configuration

//TODO add defensive error checking & logging
/** Executes a snippet of Configuration DSL and adds a bunch of options to a Configuration */
class OptionsDeclarationCtx(private val configuration: Configuration, private val prefix: String) {
    fun section(sectionName: String, declarations: OptionsDeclarationCtx.() -> Unit) {
        val subctx = OptionsDeclarationCtx(configuration, "$prefix$sectionName.")
        subctx.apply(declarations)
    }

    open class OptionDeclarationCtx<T : Any> {
        var hidden: Boolean = false
        var transient: Boolean = false
        var default: T? = null

        internal val hooks = mutableListOf<Configuration.Option<T>.() -> Unit>()
        fun hook(hook: Configuration.Option<T>.() -> Unit) {
            hooks.add(hook)
        }
    }

    class OptionStringDeclarationCtx : OptionDeclarationCtx<String>()

    fun option(optionName: String, declaration: OptionStringDeclarationCtx.() -> Unit): String {
        val declared = OptionStringDeclarationCtx().apply(declaration)
        val option = configuration.OptionString("$prefix$optionName", declared.default ?: "undefined")
        option.hidden = declared.hidden
        option.transient = declared.transient
        declared.hooks.forEach { option.addHook(it) }

        configuration.registerOption(option)
        return option.name
    }

    class OptionBooleanDeclarationCtx : OptionDeclarationCtx<Boolean>()

    fun optionBoolean(optionName: String, declaration: OptionBooleanDeclarationCtx.() -> Unit): String {
        val declared = OptionBooleanDeclarationCtx().apply(declaration)
        val option = configuration.OptionBoolean("$prefix$optionName", declared.default ?: false)
        declared.hooks.forEach { option.addHook(it) }
        configuration.registerOption(option)
        return option.name
    }

    open class OptionMultipleChoicesDeclarationCtx<T : Any> : OptionDeclarationCtx<T>() {
        lateinit var possibleChoices: List<T>
    }

    class OptionMultipleChoicesIntDeclarationCtx : OptionMultipleChoicesDeclarationCtx<Int>()

    fun optionMultipleChoicesInt(optionName: String, declaration: OptionMultipleChoicesIntDeclarationCtx.() -> Unit): String {
        val declared = OptionMultipleChoicesIntDeclarationCtx().apply(declaration)
        val option = configuration.OptionMultiChoiceInt("$prefix$optionName", declared.default ?: declared.possibleChoices[0], declared.possibleChoices)
        declared.hooks.forEach { option.addHook(it) }
        configuration.registerOption(option)
        return option.name
    }

    class OptionMultipleChoicesStringDeclarationCtx : OptionMultipleChoicesDeclarationCtx<String>()

    fun optionMultipleChoices(optionName: String, declaration: OptionMultipleChoicesStringDeclarationCtx.() -> Unit): String {
        val declared = OptionMultipleChoicesStringDeclarationCtx().apply(declaration)
        val option = configuration.OptionMultiChoice("$prefix$optionName", declared.default ?: declared.possibleChoices[0], declared.possibleChoices)
        declared.hooks.forEach { option.addHook(it) }
        configuration.registerOption(option)
        return option.name
    }

    class OptionRangeDoubleDeclarationCtx : OptionDeclarationCtx<Double>() {
        var minimumValue: Double = 0.0
        var maximumValue: Double = 1.0
        var granularity: Double = 0.0
    }

    fun optionRangeDouble(optionName: String, declaration: OptionRangeDoubleDeclarationCtx.() -> Unit): String {
        val declared = OptionRangeDoubleDeclarationCtx().apply(declaration)
        val option = configuration.OptionDoubleRange("$prefix$optionName", declared.default ?: declared.minimumValue, declared.minimumValue, declared.maximumValue, declared.granularity)
        declared.hooks.forEach { option.addHook(it) }
        configuration.registerOption(option)
        return option.name
    }

    class OptionIntDeclarationCtx : OptionDeclarationCtx<Int>()

    fun optionInt(optionName: String, declaration: OptionIntDeclarationCtx.() -> Unit): String {
        val declared = OptionIntDeclarationCtx().apply(declaration)
        val option = configuration.OptionInt(optionName, declared.default ?: 0)
        declared.hooks.forEach { option.addHook(it) }
        configuration.registerOption(option)
        return option.name
    }
}