package io.xol.chunkstories.api.world.dummy;

import org.joml.Vector3dc;

import io.xol.chunkstories.api.GameContext;
import io.xol.chunkstories.api.GameLogic;
import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.content.ContentTranslator;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.events.voxel.WorldModificationCause;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.input.Input;
import io.xol.chunkstories.api.particles.ParticlesManager;
import io.xol.chunkstories.api.physics.CollisionBox;
import io.xol.chunkstories.api.rendering.effects.DecalsManager;
import io.xol.chunkstories.api.sound.SoundManager;
import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.util.concurrency.Fence;
import io.xol.chunkstories.api.world.VoxelContext;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.WorldCollisionsManager;
import io.xol.chunkstories.api.world.WorldInfo;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkVoxelContext;
import io.xol.chunkstories.api.world.chunk.ChunkHolder;
import io.xol.chunkstories.api.world.chunk.ChunksIterator;
import io.xol.chunkstories.api.world.chunk.Region;
import io.xol.chunkstories.api.world.chunk.WorldUser;
import io.xol.chunkstories.api.world.generator.WorldGenerator;
import io.xol.chunkstories.api.world.heightmap.RegionSummaries;

/** 
 * DummyWorld doesn't exist. DummyWorld is immutable. DummyWorld is unique.
 * You can't prove the existence of the DummyWorld, but neither can you disprove it. 
 * Don't mess with the forces at play here. You have been warned.
 */
public class DummyWorld implements World {

	static DummyWorld instance = null;
	
	public static DummyWorld get() {
		if(instance == null)
			instance = new DummyWorld();
		return instance;
	}
	
	@Override
	public WorldInfo getWorldInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorldGenerator getGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameLogic getGameLogic() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameContext getGameContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEntity(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean removeEntity(Entity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeEntityByUUID(long uuid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Entity getEntityByUUID(long uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IterableIterator<Entity> getAllLoadedEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NearEntitiesIterator getEntitiesInBox(Vector3dc center, Vector3dc boxSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChunkVoxelContext peek(int x, int y, int z) throws WorldException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChunkVoxelContext peek(Vector3dc location) throws WorldException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorldVoxelContext peekSafely(int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorldVoxelContext peekSafely(Vector3dc location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int peekSimple(int x, int y, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public WorldVoxelContext poke(int x, int y, int z, int newVoxelData, WorldModificationCause cause)
			throws WorldException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorldVoxelContext pokeSilently(int x, int y, int z, int newVoxelData) throws WorldException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pokeSimple(int x, int y, int z, int newVoxelData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pokeSimpleSilently(int x, int y, int z, int newVoxelData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IterableIterator<VoxelContext> getVoxelsWithin(CollisionBox boundingBox) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChunkHolder aquireChunkHolderLocation(WorldUser user, Location location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChunkHolder aquireChunkHolderWorldCoordinates(WorldUser user, int worldX, int worldY, int worldZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChunkHolder aquireChunkHolder(WorldUser user, int chunkX, int chunkY, int chunkZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isChunkLoaded(int chunkX, int chunkY, int chunkZ) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Chunk getChunk(int chunkX, int chunkY, int chunkZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chunk getChunkWorldCoordinates(int worldX, int worldY, int worldZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chunk getChunkWorldCoordinates(Location location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChunksIterator getAllLoadedChunks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region aquireRegion(WorldUser user, int regionX, int regionY, int regionZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region aquireRegionChunkCoordinates(WorldUser user, int chunkX, int chunkY, int chunkZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region aquireRegionWorldCoordinates(WorldUser user, int worldX, int worldY, int worldZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region aquireRegionLocation(WorldUser user, Location location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region getRegion(int regionX, int regionY, int regionZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region getRegionChunkCoordinates(int chunkX, int chunkY, int chunkZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region getRegionWorldCoordinates(int worldX, int worldY, int worldZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region getRegionLocation(Location location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegionSummaries getRegionsSummariesHolder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void redrawEverything() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Fence saveEverything() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Location getDefaultSpawnLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultSpawnLocation(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTime(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getWeather() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWeather(float overcastFactor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getTicksElapsed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean handleInteraction(Entity entity, Location blockLocation, Input input) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public WorldCollisionsManager collisionsManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecalsManager getDecalsManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParticlesManager getParticlesManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SoundManager getSoundManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentTranslator getContentTranslator() {
		// TODO Auto-generated method stub
		return null;
	}

}
