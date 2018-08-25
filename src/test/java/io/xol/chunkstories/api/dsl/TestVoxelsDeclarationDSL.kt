package io.xol.chunkstories.api.dsl

import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.voxel.Voxel
import org.junit.Test

class TestVoxelsDeclarationDSL {

    var ctx: VoxelsDeclarationsContext? = null

    @Test
    fun testVoxelDeclarations() {
        ctx?.apply {
            voxel {
                name = "cobblestone"

                texture = "Mdr"
                textures {
                    top = "grass"
                    sides = "mossy_cobble"
                }

                /* what loot can this drop ? */
                loot {
                    or {
                        entry {
                            name = "fantastic"
                            probability = 0.05
                        }

                        and {
                            probability = 0.95

                            entry {
                                name = "meh"
                            }

                            entry {
                                name = "duh"
                            }

                            entry {
                                name = "ugh"
                            }
                        }

                        probability = 0.05
                    }

                    entry {
                        //no name = this very voxel
                        probability = 0.55
                        amount = 5..6
                    }

                    entry {
                        name = "gold_nugget"
                        probability = 0.95
                    }
                }

                material = "stone"

                representation {
                    static {
                        model("mdr")
                    }
                }
            }

            voxel {
                loot {
                    entry {
                        items = this@voxel.enumerateItemsForBuilding()
                    }
                }
            }

            voxel(CustomVoxelClass::class) {
                representation {
                    dynamic {
                        model("./models/shrek.obj") {
                            animation = "./animations/dance.bvh"
                        }
                    }
                }
            }
        }


    }
}

class CustomVoxelClass(store: Content.Voxels) : Voxel(store) {

}