package io.xol.chunkstories.api.dsl

import io.xol.chunkstories.api.util.configuration.OptionsDeclarationCtx
import org.junit.Test

class TestOptionsDSL {

    var ctx: OptionsDeclarationCtx? = null

    @Test
    fun test() {
        ctx?.apply {
            section("client") {
                section("rendering") {

                    optionMultipleChoicesInt("viewDistance") {
                        possibleChoices = listOf(64, 96, 128, 144, 160, 192, 256, 320, 384, 512, 768)
                        default = 192
                    }

                    optionBoolean("realtimeReflections") {
                        default = true
                    }

                    optionBoolean("globalIllumination") {
                        default = false
                    }

                    optionBoolean("shadowMapping") {
                        default = true
                    }

                    optionMultipleChoicesInt("shadowMappingResolution") {
                        default = 1024
                        possibleChoices = listOf(512, 1024, 2048, 4096)
                    }
                }

                section("input") {
                    optionRangeDouble("mouseSensitivity") {
                        minimumValue = 0.5
                        maximumValue = 2.0
                        granularity = 0.05
                        default = 1.0
                    }
                }

                section("game") {
                    optionMultipleChoices("logPolicy") {
                        possibleChoices = listOf("send", "dont")
                        default = "undefined"
                    }

                    optionMultipleChoicesInt("workersThreads") {
                        possibleChoices = listOf(-1, 2, 4, 6, 8, 12, 16, 32, 64)
                        default = -1
                    }

                    option("lastServerAddress") {
                        hidden = true
                    }
                }
            }


        }
    }

}