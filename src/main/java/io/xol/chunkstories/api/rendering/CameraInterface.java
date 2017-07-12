package io.xol.chunkstories.api.rendering;

import org.joml.Vector3dc;
import org.joml.Vector3fc;

import io.xol.chunkstories.api.physics.CollisionBox;
import io.xol.chunkstories.api.rendering.pipeline.ShaderInterface;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface CameraInterface
{
	public boolean isBoxInFrustrum(Vector3fc center, Vector3fc dimensions);
	
	public boolean isBoxInFrustrum(CollisionBox box);

	public void setupShader(ShaderInterface shaderProgram);

	public Vector3dc getCameraPosition();
	
	public void setCameraPosition(Vector3dc pos);

	public Vector3fc getViewDirection();
	
	public float getFOV();
	
	public void setFOV(float fov);

	public Vector3fc transform3DCoordinate(Vector3fc vector3f);

	void setRotationZ(float rotationZ);

	float getRotationZ();

	void setRotationY(float rotationY);

	float getRotationY();

	void setRotationX(float rotationX);

	float getRotationX();

	public void setupUsingScreenSize(int width, int height);
}