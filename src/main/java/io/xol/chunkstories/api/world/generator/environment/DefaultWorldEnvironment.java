//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world.generator.environment;

import org.joml.Vector3dc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import io.xol.chunkstories.api.math.Math2;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.textures.Texture2D;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.WorldInfo;

public class DefaultWorldEnvironment implements WorldEnvironment {

	final World world;
	
	private boolean canSnow;
	private int snowStartHeight;
	private int snowTransition;
	
	private String grassColorTexture;
	private Vector3f sunColorSunny;
	private Vector3f sunColorMoody;
	private Vector3f shadowColorSunny;
	private Vector3f shadowColorMoody;
	
	public DefaultWorldEnvironment(World world) {
		this.world = world;
		
		//Load configuration stuff
		snowStartHeight = Integer.parseInt(world.getWorldInfo().resolveProperty("snowStartHeight", "-1"));
		snowTransition = Integer.parseInt(world.getWorldInfo().resolveProperty("snowTransition", "20"));
		canSnow = snowStartHeight >= 0;
		
		grassColorTexture = world.getWorldInfo().resolveProperty("grassColorTexture", "./textures/environement/grassColor.png");
		
		sunColorSunny = loadVec3f(world.getWorldInfo(), "sunColorSunny", "1.0 1.0 1.0");
		sunColorMoody = loadVec3f(world.getWorldInfo(), "sunColorMoody", "0.5 0.5 0.5");
		shadowColorSunny = loadVec3f(world.getWorldInfo(), "shadowColorSunny", "0.2500 0.3362 0.3970");
		shadowColorMoody = loadVec3f(world.getWorldInfo(), "shadowColorMoody", "0.3 0.3 0.3");
	}
	
	private Vector3f loadVec3f(WorldInfo info, String prop, String def) {
		String r = info.resolveProperty(prop, def);
		String s[] = r.split(" ");
		assert s.length == 3;
		
		Vector3f v = new Vector3f();
		v.x = Float.parseFloat(s[0]);
		v.y = Float.parseFloat(s[1]);
		v.z = Float.parseFloat(s[2]);
		
		return v;
	}

	@Override
	public float getWorldWetness(Vector3dc cameraPosition) {
		//The world starts to get wet at 0.5 weather and is fully wet at 0.8
		float wetFactor = Math.min(Math.max(0.0f, world.getWeather() - 0.5f) / 0.3f, 1.0f);
		
		//Snow disables 'wetness', we don't want glittery snow everywhere.
		if(canSnow)
			//If the transition is instantenous, we know we are never going to see any glint
			if(snowTransition <= 0)
				return 0;
			//Linear interp
			else
				return wetFactor * (1f - Math2.clamp((cameraPosition.y() - snowStartHeight) / snowTransition, 0, 1));
		//Never snows ? Just return the world wetness then
		else
			return wetFactor;
	}

	@Override
	public Texture2D getGrassTexture(RenderingInterface renderer) {
		Texture2D vegetationTexture = renderer.textures().getTexture(grassColorTexture);
		vegetationTexture.setMipMapping(false);
		vegetationTexture.setLinearFiltering(true);
		return vegetationTexture;
	}

	@Override
	public Vector3fc getSunlightColor(Vector3dc cameraPosition) {
		float moodyness = Math.min(Math.max(0.0f, world.getWeather() - 0.0f) / 1.0f, 1.0f);
		return Math2.mix(sunColorSunny, sunColorMoody, moodyness);
	}

	@Override
	public Vector3fc getShadowColor(Vector3dc cameraPosition) {
		float moodyness = Math.min(Math.max(0.0f, world.getWeather() - 0.0f) / 1.0f, 1.0f);
		return Math2.mix(shadowColorSunny, shadowColorMoody, moodyness);
	}
}
