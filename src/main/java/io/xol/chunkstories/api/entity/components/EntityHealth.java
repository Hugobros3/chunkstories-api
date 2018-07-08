//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.components;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import org.joml.Vector3d;

import io.xol.chunkstories.api.GameContext;
import io.xol.chunkstories.api.entity.Controller;
import io.xol.chunkstories.api.entity.DamageCause;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.events.entity.EntityDamageEvent;
import io.xol.chunkstories.api.events.entity.EntityDeathEvent;
import io.xol.chunkstories.api.events.player.PlayerDeathEvent;
import io.xol.chunkstories.api.physics.EntityHitbox;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.server.ServerInterface;
import io.xol.chunkstories.api.world.WorldMaster;
import io.xol.chunkstories.api.world.serialization.StreamSource;
import io.xol.chunkstories.api.world.serialization.StreamTarget;

import javax.annotation.Nullable;

/**
 * Any entity with this component is considered living, even if it's dead.
 * 
 * Handles health management and death
 */
public class EntityHealth extends EntityComponent {
	private float value;

	private long damageCooldown = 0;
	@Nullable
	private DamageCause lastDamageCause;
	long deathDespawnTimer = 6000;

	public EntityHealth(Entity entity) {
		super(entity);
		this.value = Float.parseFloat(entity.getDefinition().resolveProperty("startHealth", "100"));
	}

	public float getHealth() {
		return value;
	}

	public void setHealth(float health) {
		boolean wasntDead = value > 0.0;
		this.value = health;

		if (health <= 0.0 && wasntDead)
			handleDeath();

		if (entity.getWorld() instanceof WorldMaster) {
			if (health > 0.0)
				this.pushComponentController();
			else
				this.pushComponentEveryone();
		}
	}

	public float damage(DamageCause cause, float damage) {
		return damage(cause, null, damage);
	}

	public float damage(DamageCause cause, EntityHitbox osef, float damage) {
		if (damageCooldown > System.currentTimeMillis())
			return 0f;

		EntityDamageEvent event = new EntityDamageEvent(entity, cause, damage);
		entity.getWorld().getGameLogic().getPluginsManager().fireEvent(event);

		if (!event.isCancelled()) {
			applyDamage(event.getDamageDealt());
			lastDamageCause = cause;

			damageCooldown = System.currentTimeMillis() + cause.getCooldownInMs();

			float damageDealt = event.getDamageDealt();

			// Applies knockback
			if (cause instanceof Entity) {
				// Only runs if the entity do have a velocity
				entity.components.with(EntityVelocity.class, ev -> {

					Entity attacker = (Entity) cause;
					Vector3d attackKnockback = entity.getLocation().sub(attacker.getLocation().add(0d, 0d, 0d));
					attackKnockback.y = (0d);
					attackKnockback.normalize();

					float knockback = (float) Math.max(1f, Math.pow(damageDealt, 0.5f));

					attackKnockback.mul(knockback / 50d);
					attackKnockback.y = (knockback / 50d);

					ev.addVelocity(attackKnockback);
				});

			}

			return damageDealt;
		}

		return 0f;
	}

	public void applyDamage(float dmg) {
		boolean wasntDead = value > 0.0;
		this.value -= dmg;

		if (value <= 0.0 && wasntDead)
			handleDeath();

		if (entity.getWorld() instanceof WorldMaster) {
			if (value > 0.0)
				this.pushComponentController();
			else
				this.pushComponentEveryone();
		}
	}

	private void handleDeath() {
		EntityDeathEvent entityDeathEvent = new EntityDeathEvent(entity);
		entity.getWorld().getGameLogic().getPluginsManager().fireEvent(entityDeathEvent);

		// Handles cases of controlled player death
		entity.components.with(EntityController.class, ec -> {
			Controller controller = ec.getController();
			if (controller != null) {
				controller.setControlledEntity(null);

				// Serverside stuff
				if (controller instanceof Player && entity.getWorld() instanceof WorldMaster) {
					Player player = (Player) controller;

					PlayerDeathEvent event = new PlayerDeathEvent(player);
					entity.getWorld().getGameLogic().getPluginsManager().fireEvent(event);

					// When a player dies, delete his save as well
					File playerSavefile = new File(((WorldMaster) entity.getWorld()).getFolderPath() + "/players/" + player.getName().toLowerCase() + ".csf");
					if (playerSavefile.exists()) {
						// Player save file is deleted upon death
						playerSavefile.delete();
					}

					if (event.getDeathMessage() != null) {
						GameContext gc = player.getContext();
						if (gc instanceof ServerInterface)
							((ServerInterface) gc).broadcastMessage(event.getDeathMessage());
					}
				} else {
					// Weird, undefined cases ( controller wasn't a player, maybe some weird mod
					// logic here
				}
			}
		});
	}

	@Override
	public void push(StreamTarget destinator, DataOutputStream dos) throws IOException {
		dos.writeFloat(value);
	}

	@Override
	public void pull(StreamSource from, DataInputStream dis) throws IOException {
		value = dis.readFloat();
	}

	public boolean isDead() {
		return getHealth() <= 0;
	}

	@Nullable
	public DamageCause getLastDamageCause() {
		return lastDamageCause;
	}

	public void removeCorpseAfterDelay() {
		if (isDead()) {
			deathDespawnTimer--;
			if (deathDespawnTimer < 0) {
				entity.world.removeEntity(entity);
				return;
			}
		}
	}

	public float getMaxHealth() {
		return Float.parseFloat(entity.getDefinition().resolveProperty("maxHealth", entity.getDefinition().resolveProperty("startHealth", "100")));
	}

}
