//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.server

/** Did you check your privilege today ?  */
interface UserPrivileges {

    fun isUserAdmin(username: String): Boolean

    fun isUserWhitelisted(username: String): Boolean

    fun isUserBanned(username: String): Boolean

    /** Syntax: ipv4:a.b.c.d or ipv6:2001:0db8:0000:85a3:0000:0000:ac1f:8001  */
    // TODO actually implement
    fun isIpBanned(ip: String): Boolean

    fun setUserAdmin(username: String, admin: Boolean)

    fun setUserWhitelisted(username: String, whitelisted: Boolean)

    /** Warning: This method won't kick the player, just prevent him from
     * reconnecting  */
    fun setUserBanned(username: String, banned: Boolean)

    fun setIpBanned(ip: String, banned: Boolean)

}