//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics

import xyz.chunkstories.api.graphics.TextureFormat.*
import xyz.chunkstories.api.graphics.rendergraph.DepthTestingConfiguration.DepthTestMode.*
import xyz.chunkstories.api.graphics.rendergraph.PassOutput.BlendMode.*


import xyz.chunkstories.api.graphics.structs.InterfaceBlock
import xyz.chunkstories.api.graphics.systems.drawing.*
import org.junit.Test
import xyz.chunkstories.api.graphics.rendergraph.ImageInput
import xyz.chunkstories.api.graphics.rendergraph.RenderGraphDeclaration
import xyz.chunkstories.api.graphics.rendergraph.asset
import xyz.chunkstories.api.graphics.systems.dispatching.DefferedLightsRenderer

data class MyTestInterfaceBlock(val someFloat: Float, val someInt: Int) : InterfaceBlock

class TestRenderGraphDSL {
    @Test
    fun testRenderGraphDSL() {
        val testScript: RenderGraphDeclaration.() -> Unit = {
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
                            system(DefferedLightsRenderer::class) {
                                //maxBatchSize = 32
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

                        draws {
                            system(FullscreenQuadDrawer::class) {

                            }
                        }

                        setup {
                            shaderResources.supplyImage("colorMap") {
                                source = asset("textures/effects/tonemap.png")
                                scalingMode = ImageInput.ScalingMode.LINEAR
                                tilingMode = TextureTilingMode.CLAMP_TO_EDGE
                            }
                        }

                        depth {
                            depthBuffer = renderBuffer("myDepthBuffer")
                            mode = LESS_OR_EQUAL
                        }

                        outputs {
                            output {
                                name = "outColor"
                                //outputBuffer = "myFancyBuffer"
                                target = renderBuffer("myFancyBuffer")

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