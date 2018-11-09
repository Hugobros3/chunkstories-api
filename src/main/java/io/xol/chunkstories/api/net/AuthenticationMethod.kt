package io.xol.chunkstories.api.net

/** The plan is to support multiple authentification methods for users who so desire. */
enum class AuthenticationMethod {
    /** No checks. You have to manually enable this in the configuration ! */NONE,
    /** Accounts on chunkstories.xyz */CHUNKSTORIES_OFFICIAL_WEBSITE,
    /** TODO */PUBLIC_KEY,
    /** TODO */STEAM_API;
}