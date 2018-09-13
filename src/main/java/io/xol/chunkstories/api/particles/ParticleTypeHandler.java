//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.particles;

import io.xol.chunkstories.api.physics.Box;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.cell.CellData;
import org.joml.Vector3f;

/**
 * Determines how a specific particle type should be handled, what type of
 * metadata to keep for each particle, how to render them etc.
 */
public abstract class ParticleTypeHandler {

    private final ParticleTypeDefinition type;

    public ParticleTypeHandler(ParticleTypeDefinition type) {
        this.type = type;
    }

    public ParticleTypeDefinition getType() {
        return type;
    }

    public String getName() {
        return type.getName();
    }

    /**
     * Particle data is at least a vector3f
     */
    //TODO InterfaceBlock & data class
    public class ParticleData extends Vector3f {
        boolean ded = false;

        public ParticleData(float x, float y, float z) {
            super(x, y, z);
        }

        public void destroy() {
            ded = true;
        }

        public boolean isDed() {
            return ded;
        }

        /**
         * Helper method for particles to check their collisions efficiently and
         * concisely
         */
        public boolean isCollidingAgainst(World world) {
            return isCollidingAgainst(world, x, y, z);
        }

        /**
         * Helper method for particles to check their collisions efficiently and
         * concisely
         */
        public boolean isCollidingAgainst(World world, float x, float y, float z) {

            CellData peek = world.peekSafely((int) x, (int) y, (int) z);

            if (peek.getVoxel().isSolid()) {
                // Fast check if the voxel is just a solid block
                // TODO isOpaque doesn't mean that exactly, newEntity a new type variable that
                // represents that specific trait
                if (peek.getVoxel().isOpaque())
                    return true;

                // Else iterate over each box that make up that block
                Box[] boxes = peek.getVoxel().getTranslatedCollisionBoxes(peek);
                if (boxes != null)
                    for (Box box : boxes)
                        if (box.isPointInside(x, y, z))
                            return true;

            }
            return false;
        }
    }

    public ParticleData createNew(World world, float x, float y, float z) {
        return new ParticleData(x, y, z);
    }

    public abstract void forEach_Physics(World world, ParticleData data);
}
