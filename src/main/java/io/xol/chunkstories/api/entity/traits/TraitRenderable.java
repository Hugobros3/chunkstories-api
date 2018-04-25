package io.xol.chunkstories.api.entity.traits;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.rendering.entity.EntityRenderer;

/** 
 * <p>Marks this entity as renderable and provides a lambda expression to obtain that renderer.
 * Do note that renderers are cached per-entity definition; it's therefore impossible to have multiple renderers for the same entity type</p>
 * */
public class TraitRenderable extends Trait {

	public <T extends Entity> TraitRenderable(T entity, RendererFactory<T> factory) {
		super(entity);
		this.factory = factory;
	}
	
	private final RendererFactory<?> factory;

	public interface RendererFactory<E extends Entity> {
		public EntityRenderer<E> getRenderer();
	}

	public RendererFactory<? extends Entity> getFactory() {
		return factory;
	}
}
