//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.content;

import xyz.chunkstories.api.content.Asset;

public class MeshLoadException extends AssetException {

	private static final long serialVersionUID = 5322485540396142553L;

	public MeshLoadException(Asset asset) {
		super(asset);
	}

	@Override
	public String getMessage() {
		return "Mesh from asset " + getAsset() + " failed to load.";
	}

}
