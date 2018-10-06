package io.xol.chunkstories.api.graphics

import io.xol.chunkstories.api.graphics.TextureFormat.*
import io.xol.chunkstories.api.graphics.rendergraph.DepthTestingConfiguration.DepthTestMode.*
import io.xol.chunkstories.api.graphics.rendergraph.PassOutput.BlendMode.*
import io.xol.chunkstories.api.graphics.ImageInput.SamplingMode.*


import io.xol.chunkstories.api.dsl.RenderGraphDeclarationScript
import io.xol.chunkstories.api.graphics.structs.InterfaceBlock
import io.xol.chunkstories.api.graphics.systems.drawing.*
import org.junit.Test

data class MyTestInterfaceBlock(val someFloat: Float, val someInt: Int) : InterfaceBlock

class TestRenderGraphDSL {
    @Test
    fun testRenderGraphDSL() {
        val testScript: RenderGraphDeclarationScript = {
            renderBuffers {
                renderBuffer {
                    name = "myFancyBuffer"

                    format = RGBA_8
                    size = viewportSize
                }

                renderBuffer {
                    name = "myDepthBuffer"

                    format = DEPTH_32
                    size = viewportSize
                }
            }

            passes {
                pass {
                    name = "sky"

                    draws {
                        fullscreenQuad()
                        system(DefferedLightsDrawer::class) {
                            maxBatchSize = 32
                        }
                        system(FarTerrainDrawer::class)
                    }

                    depth {
                        enabled = false
                    }
                }

                pass {
                    name = "mainPass"

                    default = true
                    dependsOn("sky")

                    inputs {
                        imageInput {
                            name = "colorMap"

                            source = texture("textures/effects/tonemap.png")
                            samplingMode = LINEAR
                            wrapping = false
                        }

                        uniformInput {
                            name = "myUniform"
                            data = MyTestInterfaceBlock(5.0F, 42 + 69)
                        }
                    }

                    depth {
                        depthBuffer = "myDepthBuffer"
                        mode = LESS_OR_EQUAL
                    }

                    outputs {
                        output {
                            name = "outColor"
                            outputBuffer = "myFancyBuffer"

                            clear = true
                            blending = MIX
                        }
                    }
                }
            }
        }
    }
}