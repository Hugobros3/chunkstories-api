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
import xyz.chunkstories.api.exceptions.PacketProcessingException;
import xyz.chunkstories.api.net.PacketDestinator;
import xyz.chunkstories.api.net.PacketReceptionContext;
import xyz.chunkstories.api.net.PacketSender;
import xyz.chunkstories.api.net.PacketSendingContext;
import xyz.chunkstories.api.net.PacketWorld;
import xyz.chunkstories.api.world.World;

import javax.annotation.Nullable;

// TODO: Use ContentTranslator to assign ids to particles
// TODO: Use reflection or something and send the raw fields of the Particle
// object
public class PacketParticle extends PacketWorld {
	private String particleName = "";
	private Vector3dc position;
	@Nullable
	private Vector3dc velocity;

	public PacketParticle(World world) {
		super(world);
	}

	public PacketParticle(World world, String particleName, Vector3dc position, @Nullable Vector3dc velocity) {
		super(world);
		this.particleName = particleName;
		this.position = position;
		this.velocity = velocity;
	}

	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context) throws IOException {
		out.writeUTF(particleName);
		out.writeDouble(position.x());
		out.writeDouble(position.y());
		out.writeDouble(position.z());
		out.writeBoolean(velocity != null);
		if (velocity != null) {
			out.writeDouble(velocity.x());
			out.writeDouble(velocity.y());
			out.writeDouble(velocity.z());
		}
	}

	@Override
	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext processor) throws IOException, PacketProcessingException {
		particleName = in.readUTF();
		Vector3d position = new Vector3d();
		position.x = (in.readDouble());
		position.y = (in.readDouble());
		position.z = (in.readDouble());

		Vector3d velocity = new Vector3d();
		if (in.readBoolean()) {
			velocity.x = (in.readDouble());
			velocity.y = (in.readDouble());
			velocity.z = (in.readDouble());
		}

		/* if (processor instanceof ClientPacketsProcessor) { ClientPacketsProcessor cpp
		 * = (ClientPacketsProcessor) processor;
		 * cpp.getTaskInstance().getParticlesManager().
		 * spawnParticleAtPositionWithVelocity(particleName, position, velocity); } */
	}
}
