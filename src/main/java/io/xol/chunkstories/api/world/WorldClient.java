//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world;

import io.xol.chunkstories.api.client.IngameClient;

/**
 * A 'Client' world is one responsible of graphical and input tasks A world can
 * be both client and master.
 */
public interface WorldClient extends World {
    public IngameClient getClient();

    public IngameClient getGameContext();
}
