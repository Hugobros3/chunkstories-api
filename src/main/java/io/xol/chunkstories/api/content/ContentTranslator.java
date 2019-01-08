//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.content;

import java.util.Collection;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.EntityDefinition;
import io.xol.chunkstories.api.item.Item;
import io.xol.chunkstories.api.item.ItemDefinition;
import io.xol.chunkstories.api.voxel.Voxel;

import javax.annotation.Nullable;

/** This interface describes the world-specific mapping from named content
 * definitions to integer IDs. */
public interface ContentTranslator {
	/** Returns a Collection containing the internalNames of the mods that were used
	 * with this world. */
	public Collection<String> getRequiredMods();

	/** Get the content we are translating */
	public Content getContent();

	/** Return the assignated ID for this declaration or -1 if it isn't a part of
	 * the current content */
	public int getIdForVoxel(Voxel voxel);

	/** Return the Voxel associated with that ID or null if the ID was outside of
	 * bounds */
	@Nullable
	public Voxel getVoxelForId(int id);

	/** Return the assignated ID for this declaration or -1 if it isn't a part of
	 * the current content */
	public int getIdForItem(ItemDefinition definition);

	/** Shortcut to getIdForItem(item.getDeclaration()) */
	public int getIdForItem(Item item);

	/** Return the ItemDefinition associated with that ID or null if the ID was
	 * outside of bounds */
	@Nullable
	public ItemDefinition getItemForId(int id);

	/** Return the assignated ID for this declaration or -1 if it isn't a part of
	 * the current content */
	public int getIdForEntity(EntityDefinition definition);

	/** Shortcut to getIdForItem(entity.getDeclaration()) */
	public int getIdForEntity(Entity entity);

	/** Return the EntityDefinition associated with that ID or null if the ID was
	 * outside of bounds */
	@Nullable
	public EntityDefinition getEntityForId(int id);
}
