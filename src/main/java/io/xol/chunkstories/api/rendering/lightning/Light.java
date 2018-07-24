//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.lightning;

import org.joml.Vector3fc;

public class Light {
	public Vector3fc color;
	public Vector3fc position;
	public float decay;

	public Light(Vector3fc color, Vector3fc position, float decay) {
		this.color = color;
		this.position = position;
		this.decay = decay;
	}

	public Vector3fc getColor() {
		return color;
	}

	public void setColor(Vector3fc color) {
		this.color = color;
	}

	public Vector3fc getPosition() {
		return position;
	}

	public void setPosition(Vector3fc position) {
		this.position = position;
	}

	public float getDecay() {
		return decay;
	}

	public void setDecay(float decay) {
		this.decay = decay;
	}
}
