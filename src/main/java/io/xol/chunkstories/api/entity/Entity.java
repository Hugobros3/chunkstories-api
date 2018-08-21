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

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.entity.traits.Trait;
import io.xol.chunkstories.api.entity.traits.serializable.TraitLocation;
import io.xol.chunkstories.api.entity.traits.serializable.TraitSerializable;
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

import javax.annotation.Nonnull;
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
 * {@link TraitSerializable Components} are data bits associated with an entity,
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
	/* Warning: this class is kind of complicated! It does a bunch of internal stuff
	 * you don't need to understand as a modder, and should definitely not touch
	 * unless you're an engine dev with a good reason. For more information on how
	 * to program entities, please check the wiki page at <TODO> */
	public final EntityDefinition definition;

	@Nonnull
	public final Traits traits = new Traits();
	// public final Traits traits = new Traits();
	public final Subscribers subscribers = new Subscribers();

	// You cannot change the world of an entity, at least not without serializing it
	// out and back in
	// The reason is double: that constraint makes the code simpler/more readable
	// and different worlds can have different content translators, with different
	// underlying object ids.
	public final World world;

	/** Holds the actual location and ensures it's sanity */
	public final TraitLocation entityLocation;

	/** Internal flag used to keep track of the initialization */
	private boolean initialized = false;

	private long uuid = -2;

	protected Entity(EntityDefinition definition, Location location) {
		this.definition = definition;

		if (location == null || location.getWorld() == null)
			throw new RuntimeException("You can't create an entity without a location or a world.");

		// ALL entities are set to have a location
		entityLocation = new TraitLocation(this, location);

		this.world = location.getWorld();
	}

	public final EntityDefinition getDefinition() {
		return definition;
	}

	/** Called by EntityDefinition::create after the constructor */
	public final void afterIntialization() {
		if (initialized)
			throw new RuntimeException("afterIntialization() is supposed to be called only once.");

		// Creates an unique set for the components we end up having
		Set<Trait> componentsSet = new HashSet<>();
		componentsSet.addAll(traits.map.values());

		// Build an immutable view of that set
		traits.all = Collections.unmodifiableSet(componentsSet);

		// Assign them ids and build an array
		traits.byId = new Trait[componentsSet.size()];
		int i = 0;

		System.out.println(componentsSet.size());
		System.out.println("this = " + this.toString());
		for (Trait c : componentsSet) {
			i = c.id();
			traits.byId[i] = c;
			i++;
		}

		// Creates an unique set for the traits we end up having
		// Set<Trait> traitsSet = new HashSet<>();
		// traitsSet.addAll(traits.traits.values());
		// Build an immutable view of that set
		// traits.all = Collections.unmodifiableSet(traitsSet);

		// Set the initialized flag so the above structures become immutable
		initialized = true;

		// uuid == -2 means the entity isn't initialized properly, and the world would
		// reject it otherwise, uuid = -1 means the world has to assign one to it
		uuid = -1;
	}

	public final class Traits {
		protected Map<Class<? extends Trait>, Trait> map = new HashMap<>();
		protected Set<Trait> all = null;
		protected Trait[] byId = null;

		private int count = 0;

		@SuppressWarnings("unchecked")
		public int registerTrait(Trait component) {
			if (initialized)
				throw new RuntimeException("You can't register traits after the entity initializes.");

			if (map.get(component.getClass()) == component) {
				world.getGameLogic().getGameContext().logger().warn(
						"Tried to register the same trait twice" + " (hint: don't call registerComponent yourself, the superconstructor does it already )");
				return component.id();
			}

			System.out.println("registering trait " + component);
			int id = purge(component);
			if (id == -1)
				id = count++;
			System.out.println("got id " + id);

			map.put(component.getClass(), component);

			// We allow refering to a component by it's superclass so we bake in all
			// superclasses that component encompasses
			Class<?> c = component.getClass();
			while (true) {
				if (c.getDeclaredAnnotationsByType(Specialized.class).length != 0) {
					// System.out.println(c.getSimpleName()+" : this is a specialized class, no
					// overshadowing parents");
					break;
				}
				c = c.getSuperclass();
				if (c == Trait.class)
					break; // stop there

				if (c.getDeclaredAnnotationsByType(Generalized.class).length != 0) {
					// System.out.println(c.getSimpleName()+" : this is a generalized class, not
					// overriding that.");
					break;
				}

				// System.out.println("registering component with superclass: "+c);
				map.put((Class<? extends Trait>) c, component);
			}

			return id;
		}

		private int purge(Trait component) {
			Trait remove = null;

			// System.out.println("Purging matching components...");
			Class<?> c = component.getClass();
			while (true) {
				if (c.getDeclaredAnnotationsByType(Specialized.class).length != 0) {
					// System.out.println(c.getSimpleName()+" : this is a specialized class, no
					// removing parents");
					break;
				}

				Trait comp = map.get(c);
				if (comp != null) {
					// System.out.println("Found conflicting component: "+comp + "with id
					// "+comp.id());
					remove = comp;
					break;
				}

				c = c.getSuperclass();
				if (c == Trait.class)
					break;
				if (c.getDeclaredAnnotationsByType(Generalized.class).length != 0) {
					// System.out.println(c.getSimpleName()+" : this is a generalized class,
					// stopping purge");
					break;
				}
			}

			if (remove != null) {
				System.out.println("Purging matching component: " + remove);
				Iterator<Entry<Class<? extends Trait>, Trait>> i = map.entrySet().iterator();
				while (i.hasNext()) {
					Entry<Class<? extends Trait>, Trait> e = i.next();
					if (e.getValue() == remove) {
						// System.out.println("Purging : "+e.getKey());
						i.remove();
					}
				}

				System.out.println("returning purged id:" + remove.id());
				return remove.id();
			}

			return -1;
		}

		public boolean has(Class<? extends Trait> componentType) {
			return map.containsKey(componentType);
		}

		@Nullable
		@SuppressWarnings("unchecked")
		public <EC extends Trait> EC get(Class<EC> trait) {
			return (EC) map.get(trait);
		}

		@Nullable
		@SuppressWarnings("unchecked")
		/** Tries to find a component matching this type, executes some action on it and
		 * returns the result. Returns null if no such component was found. */
		public <EC extends Trait, RETURN_TYPE> RETURN_TYPE tryWith(Class<EC> componentType, ReturnsAction<EC, RETURN_TYPE> action) {
			EC component = (EC) map.get(componentType);
			if (component != null) {
				return action.run(component);
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		/** Tries to find a component matching this type, executes some boolean action
		 * on it and returns the result. Returns false if no such component was
		 * found. */
		public <EC extends Trait> boolean tryWithBoolean(Class<EC> componentType, BooleanAction<EC> action) {
			EC component = (EC) map.get(componentType);
			if (component != null) {
				return action.run(component);
			}
			return false;
		}

		@SuppressWarnings("unchecked")
		/** Tries to find a component matching this type, executes some action on it and
		 * returns true. Returns false if no such component was found. */
		public <EC extends Trait> boolean with(Class<EC> componentType, VoidAction<EC> action) {
			EC component = (EC) map.get(componentType);
			if (component != null) {
				action.run(component);
				return true;
			}
			return false;
		}

		public Collection<Trait> all() {
			return all;
		}

		public Trait[] byId() {
			return byId;
		}

		@Override
		public String toString() {
			String ok = "";
			for (Trait trait : all())
				ok += "(" + safename(trait.getClass()) + ", " + trait.id() + ")" + ", ";
			return all().size() + "{" + ok + "}";
		}

		private String safename(Class<?> klass) {
			String simpleName = klass.getSimpleName();
			if (simpleName.equals(""))
				return safename(klass.getSuperclass());
			return simpleName;
		}
	}

	public class Subscribers {
		final private Set<Subscriber> subscribers = ConcurrentHashMap.newKeySet();

		/** Internal method called by a {@link subscriber} ( like a {@link Player} ... )
		 * when he subscribe() to this entity */
		public final boolean register(Subscriber subscriber) {
			// If it didn't already contain the subscriber ...
			if (subscribers.add(subscriber)) {
				return true;
			}
			return false;
		}

		/** Internal method called by a {@link subscriber} ( like a {@link Player} ... )
		 * when he unsubscribe() to this entity */
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
		return "[" + this.getClass().getSimpleName() + " Type: " + this.definition + " subs:" + this.subscribers.subscribers.size() + "  position : "
				+ this.entityLocation.get() + " UUID : " + this.uuid + " Chunk:" + this.entityLocation.getChunk() + " Traits:" + this.traits + " ]";
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

	/** The bounding box is used for any entity, it is used to determine the "rough
	 * size" of it, rendering, collisions, iterations etc purposes.<br/>
	 * The bounding box should always be at least as big as the model and optional
	 * collision boxes */
	public CollisionBox getBoundingBox() {
		return new CollisionBox(1.0, 1.0, 1.0).translate(-0.5, 0.0, -0.5);
	}

	public final CollisionBox getTranslatedBoundingBox() {
		CollisionBox box = getBoundingBox();
		box.translate(getLocation());
		return box;
	}

}
