//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net

/** The plan is to support multiple authentication methods for users who so desire. */
enum class AuthenticationMethod {
    /** No checks. You have to manually enable this in the configuration ! */
    NONE,
    /** Accounts on chunkstories.xyz */
    @Deprecated("Should use public key auth rather")
    CHUNKSTORIES_OFFICIAL_WEBSITE,
    //TODO implement this
    PUBLIC_KEY,
}