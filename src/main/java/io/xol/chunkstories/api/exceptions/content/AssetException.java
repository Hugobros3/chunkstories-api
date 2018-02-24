//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.exceptions.content;

import io.xol.chunkstories.api.content.Asset;

public class AssetException extends Exception {

	private static final long serialVersionUID = -1208547268257208116L;

	public Asset getAsset() {
		return asset;
	}

	public AssetException(Asset asset) {
		super();
		this.asset = asset;
	}

	private final Asset asset;
}
