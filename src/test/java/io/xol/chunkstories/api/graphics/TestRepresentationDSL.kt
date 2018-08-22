package io.xol.chunkstories.api.graphics

import io.xol.chunkstories.api.graphics.representation.RepresentationDeclarationCtx
import org.joml.Vector3d
import org.junit.Test

class TestRepresentationDSL {

    @Test
    fun testBasicRepresentation() {
        val representation: RepresentationDeclarationCtx.() -> Unit = {
            var i = 0

            defaultRepresentation {
                modelInstance("./models/shrek.obj") {
                    animation = content.animationsLibrary.getAnimation("./animations/dancing.bvh")

                    children {
                        //Create a red light
                        light(Vector3d(1.0, 0.0, 0.0)) {
                            //Make it so it's translated directly above
                            matrix.translateLocal(0.0, 5.0, 0.0)
                        }
                    }
                }
            }

            onEveryFrame {
                println("shrek is love, shrek is life")
                i++

                if(i % 60 == 0) {

                }
            }
        }
    }
}