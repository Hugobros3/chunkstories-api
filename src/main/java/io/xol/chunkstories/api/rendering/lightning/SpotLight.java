package io.xol.chunkstories.api.rendering.lightning;

import org.joml.Vector3fc;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class SpotLight extends Light
{
	public SpotLight(Vector3fc color, Vector3fc position, float decay, float angle, Vector3fc direction)
	{
		super(color, position, decay);
		this.angle = angle;
		this.direction = direction;
	}

	public float angle;
	public Vector3fc direction;

	public float getAngle()
	{
		return angle;
	}

	public void setAngle(float angle)
	{
		this.angle = angle;
	}

	public Vector3fc getDirection()
	{
		return direction;
	}

	public void setDirection(Vector3fc direction)
	{
		this.direction = direction;
	}
}
