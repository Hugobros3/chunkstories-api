//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.graphics

import io.xol.chunkstories.api.dsl.DynamicRepresentationBuildingContext
import org.joml.Vector3d
import org.junit.Test

class TestRepresentationDSL {

    @Test
    fun testBasicRepresentation() {
        val representation: DynamicRepresentationBuildingContext.() -> Unit = {
            var i = 0

            model("models/shrek.obj") {
                animation = "animations/dancing.bvh"

                children {
                    //Create a red light
                    light(Vector3d(1.0, 0.0, 0.0)) {
                        //Make it so it's translated directly above
                        matrix.translateLocal(0.0, 5.0, 0.0)
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