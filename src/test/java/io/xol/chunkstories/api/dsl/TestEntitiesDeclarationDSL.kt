package io.xol.chunkstories.api.dsl

import io.xol.chunkstories.api.entity.traits.serializable.TraitHealth
import org.joml.Vector3d
import org.junit.Test

class TestEntitiesDeclarationDSL {

    /** This code never actually runs, just a test for compilation */
    var ctx: EntitiesDeclarationsContext? = null

    @Test
    fun testBasicDeclarations() {
        ctx?.apply {
            entity {
                name = "lama_samara"

                extends("motor_vehicle")

                trait(io.xol.chunkstories.api.entity.traits.serializable.TraitHealth::class) {
                    maxHealth = 50.0f
                    health = 5f
                }

                representation {
                    var x = 0.0

                    light(Vector3d(1.0, .0, x))

                    val basicModel = modelInstance("mdr")

                    val model = modelInstance("./models/none.obj") {
                        inheritParentTransformation = false
                    }

                    onEveryFrame {
                        if (frameNumber % 60 == 0)
                            x = 1.0 - x

                        if (entity.traits.tryWithBoolean(TraitHealth::class) { this.isDead }) {
                            representation.rebuildRepresentation {
                                modelInstance("./models/dead.obj")
                            }
                        }
                    }
                }
            }

            entity {
                name = "I_still_require_ext_props"
                abstract = true

                ext["extensionProperty"] = "someValue idk"
            }
        }
    }
}