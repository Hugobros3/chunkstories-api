//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.world;

import io.xol.chunkstories.api.graphics.systems.dispatching.DecalsManager;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;

import io.xol.chunkstories.api.GameContext;
import io.xol.chunkstories.api.GameLogic;
import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.content.Content;
import io.xol.chunkstories.api.content.ContentTranslator;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.events.voxel.WorldModificationCause;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.input.Input;
import io.xol.chunkstories.api.particles.ParticlesManager;
import io.xol.chunkstories.api.physics.Box;
import io.xol.chunkstories.api.sound.SoundManager;
import io.xol.chunkstories.api.util.IterableIterator;
import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.world.cell.CellData;
import io.xol.chunkstories.api.world.cell.FutureCell;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkCell;
import io.xol.chunkstories.api.world.chunk.ChunkHolder;
import io.xol.chunkstories.api.world.chunk.ChunksIterator;
import io.xol.chunkstories.api.world.generator.WorldGenerator;
import io.xol.chunkstories.api.world.heightmap.WorldHeightmaps;
import io.xol.chunkstories.api.world.region.Region;

/** DummyWorld doesn't exist. DummyWorld is immutable. DummyWorld is unique. You
 * can't prove the existence of the DummyWorld, but neither can you disprove it.
 * Don't mess with the forces at play here. You have been warned. */
public class DummyWorld implements World {

	static DummyWorld instance = null;

	public static DummyWorld get() {
		if (instance == null)
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
	public ChunkCell peek(int x, int y, int z) throws WorldException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChunkCell peek(Vector3dc location) throws WorldException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorldCell peekSafely(int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorldCell peekSafely(Vector3dc location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Voxel peekSimple(int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IterableIterator<CellData> getVoxelsWithin(Box boundingBox) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChunkHolder acquireChunkHolderLocation(WorldUser user, Location location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChunkHolder acquireChunkHolderWorldCoordinates(WorldUser user, int worldX, int worldY, int worldZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChunkHolder acquireChunkHolder(WorldUser user, int chunkX, int chunkY, int chunkZ) {
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
	public Region acquireRegion(WorldUser user, int regionX, int regionY, int regionZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region acquireRegionChunkCoordinates(WorldUser user, int chunkX, int chunkY, int chunkZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region acquireRegionWorldCoordinates(WorldUser user, int worldX, int worldY, int worldZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region acquireRegionLocation(WorldUser user, Location location) {
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
	public WorldHeightmaps getRegionsSummariesHolder() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public int peekRaw(int x, int y, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public WorldCell poke(int x, int y, int z, Voxel voxel, int sunlight, int blocklight, int metadata, WorldModificationCause cause) throws WorldException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pokeSimpleSilently(int x, int y, int z, Voxel voxel, int sunlight, int blocklight, int metadata) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pokeRaw(int x, int y, int z, int newVoxelData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pokeRawSilently(int x, int y, int z, int newVoxelData) {
		// TODO Auto-generated method stub

	}

	@Override
	public CellData poke(FutureCell fvc, WorldModificationCause cause) throws WorldException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pokeSimple(int x, int y, int z, Voxel voxel, int sunlight, int blocklight, int metadata) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pokeSimple(FutureCell fvc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pokeSimpleSilently(FutureCell fvc) {
		// TODO Auto-generated method stub

	}

	@Override
	public Content getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@NotNull
	@Override
	public WorldCollisionsManager getCollisionsManager() {
		return null;
	}

	@Override
	public int getMaxHeight() {
		return 0;
	}

	@Override
	public int getSizeInChunks() {
		return 0;
	}

	@Override
	public double getWorldSize() {
		return 0;
	}
}
