package xyz.chunkstories.api.entity

interface MeleeWeapon {
    val name: String

    val damage: Float
    /** How much time until the hit lands */
    val warmupMillis: Int

    /** How long until we can attack again */
    val cooldownMillis: Int

    val reach: Double

    val attackSound: String?
}