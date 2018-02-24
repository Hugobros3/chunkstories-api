//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.server;

/** Did you check your privilege today ? */
public interface UserPrivileges {

	boolean isUserAdmin(String username);

	boolean isUserWhitelisted(String username);

	boolean isUserBanned(String username);

	/** Syntax: ipv4:a.b.c.d or ipv6:2001:0db8:0000:85a3:0000:0000:ac1f:8001 */
	//TODO actually implement
	boolean isIpBanned(String ip);
	
	public void setUserAdmin(String username, boolean admin);
	
	public void setUserWhitelisted(String username, boolean whitelisted);
	
	/** Warning: This method won't kick the player, just prevent him from reconnecting */
	public void setUserBanned(String username, boolean banned);
	
	public void setIpBanned(String ip, boolean banned);

}