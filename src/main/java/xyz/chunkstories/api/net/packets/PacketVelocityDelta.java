//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.joml.Vector3d;
import org.joml.Vector3dc;

import xyz.chunkstories.api.client.net.ClientPacketsProcessor;
import xyz.chunkstories.api.entity.Entity;
import xyz.chunkstories.api.entity.traits.serializable.TraitVelocity;
import xyz.chunkstories.api.exceptions.PacketProcessingException;
import xyz.chunkstories.api.net.PacketDestinator;
import xyz.chunkstories.api.net.PacketReceptionContext;
import xyz.chunkstories.api.net.PacketSender;
import xyz.chunkstories.api.net.PacketSendingContext;
import xyz.chunkstories.api.net.PacketWorld;
import xyz.chunkstories.api.world.World;

/** When adding momentum to a controlled entity (ie the player's current
 * entity), doing so by sending the client it's new velocity directly would
 * could result in weird, laggy behavior when the client is rapidly changing
 * direction ( it's new velocity would be based on his own, RTT-seconds ago ).
 * To avoid this we use a dedicated packet which entire purpose is to tell a
 * player to offset it's velocity by N */
public class PacketVelocityDelta extends PacketWorld {
	public PacketVelocityDelta(World world) {
		super(world);
	}

	public PacketVelocityDelta(World world, Vector3dc delta) {
		super(world);
		this.delta = delta;
	}

	private Vector3dc delta;

	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context) throws IOException {
		out.writeDouble(delta.x());
		out.writeDouble(delta.y());
		out.writeDouble(delta.z());
	}

	@Override
	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext processor) throws IOException, PacketProcessingException {
		Vector3d delta = new Vector3d(in.readDouble(), in.readDouble(), in.readDouble());

		Entity entity = ((ClientPacketsProcessor) processor).getContext().getPlayer().getControlledEntity();

		// new style
		if (entity != null) {
			entity.traits.with(TraitVelocity.class, ev -> {
				System.out.println("Debug: received velocity delta " + delta);
				ev.addVelocity(delta);
			});
		}
	}

}
