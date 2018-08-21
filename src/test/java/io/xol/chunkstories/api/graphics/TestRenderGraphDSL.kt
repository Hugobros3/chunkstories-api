package io.xol.chunkstories.api.graphics

import io.xol.chunkstories.api.graphics.TextureFormat.*
import io.xol.chunkstories.api.graphics.rendergraph.DepthTestingConfiguration.DepthTestMode.*
import io.xol.chunkstories.api.graphics.rendergraph.PassOutput.BlendMode.*
import io.xol.chunkstories.api.graphics.rendergraph.ImageInput.SamplingMode.*


import io.xol.chunkstories.api.graphics.rendergraph.RenderGraphDeclarationScript
import org.junit.Test

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
                }
            }

            passes {
                pass {
                    name = "sky"

                    depth {
                        enabled = false
                    }
                }

                pass {
                    name = "mainPass"

                    default = true

                    inputs {
                        imageInput {
                            name = "colorMap"

                            source = "./textures/effects/tonemap.png"
                            samplingMode = LINEAR
                            wrapping = false
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