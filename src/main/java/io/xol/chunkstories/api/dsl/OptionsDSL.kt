package io.xol.chunkstories.api.dsl

import io.xol.chunkstories.api.util.Configuration

/** Executes a snippet of Configuration DSL and adds a bunch of options to a Configuration */
class OptionsDeclarationCtx(private val configuration: Configuration, private val prefix: String) {
    fun section(sectionName: String, declarations: OptionsDeclarationCtx.() -> Unit) {
        val subctx = OptionsDeclarationCtx(configuration, "$prefix$sectionName.")
        subctx.apply(declarations)
    }

    open class OptionDeclarationCtx<T : Any> {
        var hidden: Boolean = false
        lateinit var default: T
    }

    class OptionStringDeclarationCtx : OptionDeclarationCtx<String>()

    fun option(optionName: String, declaration: OptionStringDeclarationCtx.() -> Unit) {
        val declared = OptionStringDeclarationCtx().apply(declaration)
        val option = configuration.OptionString(optionName, declared.default)
        configuration.registerOption(option)
    }

    class OptionBooleanDeclarationCtx : OptionDeclarationCtx<Boolean>()

    fun optionBoolean(optionName: String, declaration: OptionBooleanDeclarationCtx.() -> Unit) {
        val declared = OptionBooleanDeclarationCtx().apply(declaration)
        val option = configuration.OptionBoolean(optionName, declared.default)
        configuration.registerOption(option)
    }

    open class OptionMultipleChoicesDeclarationCtx<T : Any> : OptionDeclarationCtx<T>() {
        lateinit var possibleChoices: List<T>
    }

    class OptionMultipleChoicesIntDeclarationCtx : OptionMultipleChoicesDeclarationCtx<Int>()

    fun optionMultipleChoicesInt(optionName: String, declaration: OptionMultipleChoicesIntDeclarationCtx.() -> Unit ) {
        val declared = OptionMultipleChoicesIntDeclarationCtx().apply(declaration)
        val option = configuration.OptionMultiChoiceInt(optionName, declared.default, declared.possibleChoices)
        configuration.registerOption(option)
    }

    class OptionMultipleChoicesStringDeclarationCtx : OptionMultipleChoicesDeclarationCtx<String>()

    fun optionMultipleChoices(optionName: String, declaration: OptionMultipleChoicesStringDeclarationCtx.() -> Unit) {
        val declared = OptionMultipleChoicesStringDeclarationCtx().apply(declaration)
        val option = configuration.OptionMultiChoice(optionName, declared.default, declared.possibleChoices)
        configuration.registerOption(option)
    }

    class OptionRangeDoubleDeclarationCtx : OptionDeclarationCtx<Double>() {
        var minimumValue: Double = 0.0
        var maximumValue: Double = 1.0
        var granularity: Double = 0.0
    }

    fun optionRangeDouble(optionName: String, declaration: OptionRangeDoubleDeclarationCtx.() -> Unit) {
        val declared = OptionRangeDoubleDeclarationCtx().apply(declaration)
        val option = configuration.OptionDoubleRange(optionName, declared.default, declared.minimumValue, declared.maximumValue, declared.granularity)
        configuration.registerOption(option)
    }
}