package io.xol.chunkstories.api.voxel.materials

import io.xol.chunkstories.api.content.Declaration

interface Material : Declaration {
    val sounds : Sounds
    class Sounds {
        var walkingSounds = "sounds/footsteps/generic[1-3].ogg"
        var runningSounds = "$walkingSounds"
        var jumpingSounds = "sounds/footsteps/jump.ogg"
        var landingSounds = "sounds/footsteps/land.ogg"
    }

    val miningProperties : MiningProperties
    class MiningProperties {
        var defaultResistance = 1.0
        var resistancePerToolType = mapOf<String, Double>()
    }
}