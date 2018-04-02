package io.xol.chunkstories.api.mesh;

public interface MeshMaterial {
	public Mesh getMesh();
	
	/** First vertex id that uses this material */
	public int firstVertex();
	
	/** Last vertex id that uses this material */
	public int lastVertex();
	
	public String getAlbedoTextureName();
	
	public String getNormalTextureName();
	
	public String getSpecularTextureName();
}
