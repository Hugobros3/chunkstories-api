//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.shader;

import java.util.Map;

import org.joml.Matrix3fc;
import org.joml.Matrix4fc;
import org.joml.Vector2dc;
import org.joml.Vector2fc;
import org.joml.Vector3dc;
import org.joml.Vector3fc;
import org.joml.Vector4dc;
import org.joml.Vector4fc;

public interface Shader
{
	public String getShaderName();

	public void setUniform1i(String uniformName, int uniformData);

	public void setUniform1f(String uniformName, float uniformData);
	
	public void setUniform1f(String uniformName, double uniformData);

	public void setUniform2f(String uniformName, float uniformData_x, float uniformData_y);
	
	public void setUniform2f(String uniformName, double uniformData_x, double uniformData_y);
	
	public void setUniform2f(String uniformName, Vector2fc uniformData);
	
	public void setUniform2f(String uniformName, Vector2dc uniformData);

	public void setUniform3f(String uniformName, float uniformData_x, float uniformData_y, float uniformData_z);
	
	public void setUniform3f(String uniformName, double uniformData_x, double uniformData_y, double uniformData_z);
	
	public void setUniform3f(String uniformName, Vector3fc uniformData);
	
	public void setUniform3f(String uniformName, Vector3dc uniformData);

	public void setUniform4f(String uniformName, float uniformData_x, float uniformData_y, float uniformData_z, float uniformData_w);
	
	public void setUniform4f(String uniformName, double uniformData_x, double uniformData_y, double uniformData_z, double uniformData_w);
	
	public void setUniform4f(String uniformName, Vector4fc uniformData);

	public void setUniform4f(String uniformName, Vector4dc uniformData);
	
	public void setUniformMatrix4f(String uniformName, Matrix4fc uniformData);
	
	public void setUniformMatrix3f(String uniformName, Matrix3fc uniformData);
	
	public void attachUBO(String bindingPoint, ShaderBuffer ubo);
	
	/** Returns the list of parsed texture samplers declared in the fragment shader */
	public Map<String, SamplerType> samplers();
	
	public enum SamplerType {
		TEXTURE_1D,
		TEXTURE_2D,
		TEXTURE_3D,
		CUBEMAP,
		ARRAY_TEXTURE_2D;
	}
	
	//public UniformsConfiguration getUniformsConfiguration();
}
