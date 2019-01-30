//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics

import xyz.chunkstories.api.graphics.TextureFormat.*
import xyz.chunkstories.api.graphics.rendergraph.DepthTestingConfiguration.DepthTestMode.*
import xyz.chunkstories.api.graphics.rendergraph.PassOutput.BlendMode.*


import xyz.chunkstories.api.graphics.rendergraph.RenderGraphDeclarationScript
import xyz.chunkstories.api.graphics.structs.InterfaceBlock
import xyz.chunkstories.api.graphics.systems.drawing.*
import org.junit.Test
import xyz.chunkstories.api.graphics.ImageInput.SamplingMode.LINEAR

data class MyTestInterfaceBlock(val someFloat: Float, val someInt: Int) : InterfaceBlock

class TestRenderGraphDSL {
    @Test
    fun testRenderGraphDSL() {
        val testScript: RenderGraphDeclarationScript = {
            renderTask {
                name = "mainTask"

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

                        dependsOn("sky")

                        /**/

                        draws {
                            system(FullscreenQuadDrawer::class) {

                            }
                        }

                        depth {
                            depthBuffer = "myDepthBuffer"
                            mode = LESS_OR_EQUAL
                        }

                        inputs {
                            imageInput {
                                name = "colorMap"

                                source = texture("textures/effects/tonemap.png")
                                samplingMode = LINEAR
                                wrapping = false
                            }
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
}