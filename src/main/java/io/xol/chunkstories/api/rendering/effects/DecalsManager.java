package io.xol.chunkstories.api.rendering.effects;

import org.joml.Vector3dc;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface DecalsManager
{
	public void drawDecal(Vector3dc position, Vector3dc orientation, Vector3dc size, String decalName);
}
