//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits;

import xyz.chunkstories.api.dsl.DynamicRepresentationBuildingContext;
import xyz.chunkstories.api.entity.Entity;

/**
 * <p>
 * Marks this entity as renderable and provides a lambda expression to obtain
 * that renderer. Do note that renderers are cached per-entity declaration; it's
 * therefore impossible to have multiple renderers for the same entity type
 * </p>
 */
public class TraitRenderable extends Trait {

	/** This is a generic primary constructor, and Kotlin does *not* have that ! I
	 * did manage to find a missing feature :D */
	public <T extends Entity> TraitRenderable(T entity, RendererFactory<T> factory) {
		super(entity);
		this.factory = factory;
	}

	private final RendererFactory<?> factory;

	public interface RendererFactory<E extends Entity> {
		public void buildRepresentation(DynamicRepresentationBuildingContext ctx, E entity);
	}

	public RendererFactory<? extends Entity> getFactory() {
		return factory;
	}
}
