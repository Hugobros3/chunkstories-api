package io.xol.chunkstories.api.dsl

interface OptionsDeclarationContext {
    fun configuration(configurationName: String, declarations: OptionsDeclarationCtx.() -> Unit)
}

class OptionsDeclarationCtx(val prefix: String) {
    fun section(sectionName: String, declarations: OptionsDeclarationCtx.() -> Unit) {
        val subctx = OptionsDeclarationCtx(prefix + sectionName + ".")
        subctx.apply(declarations)
    }

    interface OptionDeclarationCtx<T> {
        var hidden: Boolean
        var default: T
    }

    interface OptionStringDeclarationCtx : OptionDeclarationCtx<String>

    fun option(optionName: String, declaration: OptionStringDeclarationCtx.() -> Unit) {

    }

    interface OptionBooleanDeclarationCtx : OptionDeclarationCtx<Boolean>

    fun optionBoolean(optionName: String, declaration: OptionBooleanDeclarationCtx.() -> Unit) {

    }

    interface OptionMultipleChoicesDeclarationCtx<T> : OptionDeclarationCtx<T> {
        var possibleChoices: List<T>
    }

    interface OptionMultipleChoicesIntDeclarationCtx : OptionMultipleChoicesDeclarationCtx<Int>

    fun optionMultipleChoicesInt(optionName: String, declaration: OptionMultipleChoicesIntDeclarationCtx.() -> Unit ) {

    }

    interface OptionMultipleChoicesStringDeclarationCtx : OptionMultipleChoicesDeclarationCtx<String>

    fun optionMultipleChoices(optionName: String, declaration: OptionMultipleChoicesStringDeclarationCtx.() -> Unit) {

    }

    interface OptionRangeDoubleDeclarationCtx : OptionDeclarationCtx<Double> {
        var minimumValue: Double
        var maximumValue: Double
        var granularity: Double
    }

    fun optionRangeDouble(optionName: String, declaration: OptionRangeDoubleDeclarationCtx.() -> Unit) {

    }
}