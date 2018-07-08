//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.entity.components.EntityComponent;
import io.xol.chunkstories.api.entity.components.EntityLocation;
import io.xol.chunkstories.api.entity.components.Subscriber;
import io.xol.chunkstories.api.entity.traits.Trait;
import io.xol.chunkstories.api.net.packets.PacketEntity;
import io.xol.chunkstories.api.physics.CollisionBox;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.util.BooleanAction;
import io.xol.chunkstories.api.util.Generalized;
import io.xol.chunkstories.api.util.ReturnsAction;
import io.xol.chunkstories.api.util.Specialized;
import io.xol.chunkstories.api.util.VoidAction;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

import javax.annotation.Nullable;

/**
 * <p>
 * Entities are where the real gameplay meat begins: entities roam the world and
 * do all sorts of interesting things.
 * </p>
 * 
 * <p>
 * To support all this complexity in a somewhat sane way, entities are composite
 * objects; they are made out of other simpler objects that add to their
 * behaviour. There are two categories of components to an entity: Traits and
 * Components.
 * </p>
 * 
 * <p>
 * {@link EntityComponent Components} are data bits associated with an entity,
 * they store and distribute information about one aspect of the entity: the
 * health of the player, their movement mode, inventory etc. Components have
 * methods and logic to deal with serializing to the disk and network play.
 * </p>
 * 
 * <p>
 * {@link Trait Traits} are like components but they focus entirely on
 * behaviour. Unlike components, they do not have any push/pull/propagation
 * code, and can not remember information after the game is closed. Examples of
 * traits can include an entity's Renderer, it's ability to mine blocks or
 * client side GUI rendering code.
 * </p>
 * 
 * <p>
 * Entities also store a reference to all their {@link Subscriber Subscribers}
 * so their Components can directly notify them.
 * </p>
 */
public abstract class Entity {
	/*
	 * Warning: this class is kind of complicated! It does a bunch of internal stuff
	 * you don't need to understand as a modder, and should definitely not touch
	 * unless you're an engine dev with a good reason. For more information on how
	 * to program entities, please check the wiki page at <TODO>
	 */
	public final EntityDefinition definition;

	public final Components components = new Components();
	public final Traits traits = new Traits();
	public final Subscribers subscribers = new Subscribers();

	// You cannot change the world of an entity, at least not without serializing it
	// out and in
	// The reason is double: that constraint makes the code simpler/more readable
	// and different worlds can have different content translators, with different
	// ids.
	public final World world;

	/** Holds the actual location and ensures it's sanity */
	public final EntityLocation entityLocation;

	/** Internal flag used to keep track of the initialization */
	private boolean initialized = false;

	private long uuid = -2;

	protected Entity(EntityDefinition definition, Location location) {
		this.definition = definition;

		if (location == null || location.getWorld() == null)
			throw new RuntimeException("You can't create an entity without a location or a world.");

		// ALL entities are set to have a location
		entityLocation = new EntityLocation(this, location);

		this.world = location.getWorld();
	}

	/** Called by EntityDefinition::create after the constructor */
	public final void afterIntialization() {
		if (initialized)
			throw new RuntimeException("You can't register components after the entity initializes.");

		// Creates an unique set for the components we end up having
		Set<EntityComponent> componentsSet = new HashSet<>();
		componentsSet.addAll(components.components.values());
		// Assign them ids and build an array
		components.byId = new EntityComponent[componentsSet.size()];
		int i = 0;
		for (EntityComponent c : componentsSet) {
			i = c.id();
			components.byId[i] = c;
			i++;
		}
		// Build an immutable view of that set
		components.all = Collections.unmodifiableSet(componentsSet);

		// Creates an unique set for the traits we end up having
		Set<Trait> traitsSet = new HashSet<>();
		traitsSet.addAll(traits.traits.values());
		// Build an immutable view of that set
		traits.all = Collections.unmodifiableSet(traitsSet);

		// Set the initialized flag so the above structures become immutable
		initialized = true;

		// uuid == -2 means the entity isn't initialized properly, and the world would
		// reject it otherwise
		uuid = -1;
	}

	public final class Components {
		protected Map<Class<? extends EntityComponent>, EntityComponent> components = new HashMap<>();
		protected Set<EntityComponent> all = null;
		protected EntityComponent[] byId = null;

		private int count = 0;
		
		@SuppressWarnings("unchecked")
		public int registerComponent(EntityComponent component) {
			if (initialized)
				throw new RuntimeException("You can't register components after the entity initializes.");

			if(components.get(component.getClass()) == component) {
				//TODO proper logger here
				world.getGameLogic().getGameContext().logger().warn("Tried to register the same component twice"
						+ " (hint: don't call registerComponent yourself, the superconstructor does it already )");
				return component.id();
			}
			
			//System.out.println("registering component: "+component);
			
			int id = purge(component);
			if(id == -1)
				id = count++;
			
			components.put(component.getClass(), component);

			// We allow refering to a component by it's superclass so we bake in all superclasses that component encompasses
			Class<?> c = component.getClass();
			while (true) {
				if(c.getDeclaredAnnotationsByType(Specialized.class).length != 0) {
					//System.out.println(c.getSimpleName()+" : this is a specialized class, no overshadowing parents");
					break;
				}
				c = c.getSuperclass();
				if(c == EntityComponent.class)
					break; //stop there
				
				if(c.getDeclaredAnnotationsByType(Generalized.class).length != 0) {
					//System.out.println(c.getSimpleName()+" : this is a generalized class, not overriding that.");
					break;
				}

				//System.out.println("registering component with superclass: "+c);
				components.put((Class<? extends EntityComponent>) c, component);
			}

			return id;
		}

		private int purge(EntityComponent component) {
			EntityComponent remove = null;
			
			//System.out.println("Purging matching components...");
			Class<?> c = component.getClass();
			while(true) {
				if(c.getDeclaredAnnotationsByType(Specialized.class).length != 0) {
					//System.out.println(c.getSimpleName()+" : this is a specialized class, no removing parents");
					break;
				}
				c = c.getSuperclass();
				if(c == EntityComponent.class)
					break; //stop there
				
				if(c.getDeclaredAnnotationsByType(Generalized.class).length != 0) {
					//System.out.println(c.getSimpleName()+" : this is a generalized class, stopping purge");
					break;
				}

				EntityComponent comp = components.get(c);
				if(comp != null) {
					//System.out.println("Found conflicting component: "+comp + "with id "+comp.id());
					remove = comp;
					break;
				}
			}
			
			if(remove != null) {
				//System.out.println("Purging matching component: "+remove);
				Iterator<Entry<Class<? extends EntityComponent>, EntityComponent>> i = components.entrySet().iterator();
				while(i.hasNext()) {
					Entry<Class<? extends EntityComponent>, EntityComponent> e = i.next();
					if(e.getValue() == remove) {
						//System.out.println("Purging : "+e.getKey());
						i.remove();
					}
				}
				
				//System.out.println("returning purged id:"+remove.id());
				return remove.id();
			}
			
			return -1;
		}

		public boolean has(Class<? extends EntityComponent> componentType) {
			return components.containsKey(componentType);
		}

		@Nullable
		@SuppressWarnings("unchecked")
		public <EC extends EntityComponent> EC get(Class<EC> trait) {
			return (EC) components.get(trait);
		}

		@Nullable
		@SuppressWarnings("unchecked")
		/**
		 * Tries to find a component matching this type, executes some action on it and
		 * returns the result. Returns null if no such component was found.
		 */
		public <EC extends EntityComponent, RETURN_TYPE> RETURN_TYPE tryWith(Class<EC> componentType, ReturnsAction<EC, RETURN_TYPE> action) {
			EC component = (EC) components.get(componentType);
			if (component != null) {
				return action.run(component);
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		/**
		 * Tries to find a component matching this type, executes some boolean action on
		 * it and returns the result. Returns false if no such component was found.
		 */
		public <EC extends EntityComponent> boolean tryWithBoolean(Class<EC> componentType, BooleanAction<EC> action) {
			EC component = (EC) components.get(componentType);
			if (component != null) {
				return action.run(component);
			}
			return false;
		}

		@SuppressWarnings("unchecked")
		/**
		 * Tries to find a component matching this type, executes some action on it and
		 * returns true. Returns false if no such component was found.
		 */
		public <EC extends EntityComponent> boolean with(Class<EC> componentType, VoidAction<EC> action) {
			EC component = (EC) components.get(componentType);
			if (component != null) {
				action.run(component);
				return true;
			}
			return false;
		}

		public Collection<EntityComponent> all() {
			return all;
		}

		public EntityComponent[] byId() {
			return byId;
		}
		
		@Override
		public String toString() {
			String ok = "";
			for(EntityComponent c : all())
				ok += c.getClass().getSimpleName()+", ";
			return all().size()+"{"+ok+"}";
		}
	}

	public final class Traits {
		protected Map<Class<? extends Trait>, Trait> traits = new HashMap<>();
		protected Set<Trait> all = null;

		@SuppressWarnings("unchecked")
		public int registerTrait(Trait trait) {
			if (initialized)
				throw new RuntimeException("You can't register traits after the entity initializes.");

			//TODO smart traits purging (@Generalized and @Specialized)
			//System.out.println("registering trait: "+trait);
			traits.put(trait.getClass(), trait);

			// We allow refering to a trait by it's superclass, it's up to the user to make
			// sure to ask for a specific
			// enough superclass as to not have collisions
			Class<?> c = trait.getClass().getSuperclass();
			while (Trait.class.isAssignableFrom(c) && c != Trait.class) {

				//System.out.println("registering trait superclass: "+c);
				traits.put((Class<? extends Trait>) c, trait);
				c = c.getSuperclass();
			}

			return traits.size();
		}

		public boolean has(Class<? extends Trait> trait) {
			return traits.containsKey(trait);
		}

		@Nullable
		@SuppressWarnings("unchecked")
		public <T extends Trait> T get(Class<T> trait) {
			return (T) traits.get(trait);
		}

		@Nullable
		@SuppressWarnings("unchecked")
		/**
		 * Tries to find a trait matching this type, executes some action on it and
		 * returns the result. Returns null if no such component was found.
		 */
		public <T extends Trait, RETURN_TYPE> RETURN_TYPE tryWith(Class<T> traitType, ReturnsAction<T, RETURN_TYPE> action) {
			T trait = (T) traits.get(traitType);
			if (trait != null) {
				return action.run(trait);
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		/**
		 * Tries to find a trait matching this type, executes some boolean action on it
		 * and returns the result. Returns false if no such component was found.
		 */
		public <T extends Trait> boolean tryWithBoolean(Class<T> traitType, BooleanAction<T> action) {
			T trait = (T) traits.get(traitType);
			if (trait != null) {
				return action.run(trait);
			}
			return false;
		}

		/**
		 * Tries to find a trait matching this type, executes some action on it and
		 * returns true. Returns false if no such component was found.
		 */
		@SuppressWarnings("unchecked")
		public <T extends Trait> boolean with(Class<T> traitType, VoidAction<T> action) {
			T trait = (T) traits.get(traitType);
			//System.out.println("with "+traitType+": "+trait);
			if (trait != null) {
				action.run(trait);
				return true;
			}
			return false;
		}

		public Collection<Trait> all() {
			return all;
		}
		
		@Override
		public String toString() {
			String ok = "";
			for(Trait t : all())
				ok += (t.getClass().getCanonicalName() == null ? t.getClass().getSuperclass().getSimpleName() : t.getClass().getSimpleName())+", ";
			return all().size()+"{"+ok+"}";
		}
	}

	public class Subscribers {
		final private Set<Subscriber> subscribers = ConcurrentHashMap.newKeySet();

		/**
		 * Internal method called by a {@link subscriber} ( like a {@link Player} ... )
		 * when he subscribe() to this entity
		 */
		public final boolean register(Subscriber subscriber) {
			// If it didn't already contain the subscriber ...
			if (subscribers.add(subscriber)) {
				return true;
			}
			return false;
		}

		/**
		 * Internal method called by a {@link subscriber} ( like a {@link Player} ... )
		 * when he unsubscribe() to this entity
		 */
		public boolean unregister(Subscriber subscriber) {
			if (subscribers.remove(subscriber)) {
				subscriber.pushPacket(new PacketEntity(Entity.this));

				// PacketEntity checks if the subscriber is registered in the entity it's about
				// to update him about, if he's not he'll send a special flag to tell the
				// subscriber
				// to remove the entity from his view of the world
				return true;
			}
			return false;
		}

		public boolean isRegistered(StreamTarget subscriber) {
			return subscribers.contains(subscriber);
		}

		public Set<Subscriber> all() {
			return Collections.unmodifiableSet(subscribers);
		}
	}

	public abstract void tick();

	public final World getWorld() {
		return world;
	}

	public final long getUUID() {
		return uuid;
	}

	public final void setUUID(long entityUUID) {
		if (uuid != -1 && entityLocation.hasSpawned())
			throw new RuntimeException("Can't change an entity UUID after it's been set");
		this.uuid = entityUUID;
	}

	@Override
	public String toString() {
		return "[" + 
				this.getClass().getSimpleName() + 
				" Type: " + this.definition + 
				" subs:" + this.subscribers.subscribers.size() + 
				"  position : " + this.entityLocation.get() + 
				" UUID : " + this.uuid  + 
				" Chunk:" + this.entityLocation.getChunk() + 
				" Components:" + this.components + 
				" Traits:" + this.traits + 
				" ]";
	}

	public final Location getLocation() {
		return entityLocation.get();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Entity))
			return false;
		return ((Entity) o).getUUID() == uuid;
	}

	public final EntityDefinition getDefinition() {
		return definition;
	}

	/**
	 * The bounding box is used for any entity, it is used to determine the "rough
	 * size" of it, rendering, collisions, iterations etc purposes.<br/>
	 * The bounding box should always be at least as big as the model and optional
	 * collision boxes
	 */
	public CollisionBox getBoundingBox() {
		return new CollisionBox(1.0, 1.0, 1.0).translate(-0.5, 0.0, -0.5);
	}

	public CollisionBox getTranslatedBoundingBox() {
		CollisionBox box = getBoundingBox();
		box.translate(getLocation());
		return box;
	}

}
