//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity;

public interface DamageCause {
	public String getName();

	/**
	 * How many milliseconds should the target be set invulnerable after an attack
	 */
	public default long getCooldownInMs() {
		return 0;
	}

	public static DamageCause DAMAGE_CAUSE_FALL = new DamageCause() {

		@Override
		public String getName() {
			return "damage.fall";
		}

	};
}
