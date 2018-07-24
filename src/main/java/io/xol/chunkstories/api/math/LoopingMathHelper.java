//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.math;

/**
 * As chunkstories uses fixed-size, wrapping-around-themselves worlds, we need
 * some convinience methods
 * 
 * @author Gobrosse
 *
 */
public class LoopingMathHelper {
	public static int moduloDistance(int a, int b, int mod) {
		a = a % mod;
		b = b % mod;
		if (a < 0)
			a += mod;
		if (b < 0)
			b += mod;
		int mx = Math.min(a, b);
		int mn = Math.max(a, b);
		return Math.min(Math.abs(mn - mx), Math.abs(mx - (mn - mod)));
	}

	public static double moduloDistance(double a, double b, double mod) {
		a = a % mod;
		b = b % mod;
		if (a < 0)
			a += mod;
		if (b < 0)
			b += mod;
		double mx = Math.min(a, b);
		double mn = Math.max(a, b);
		return Math.min(Math.abs(mn - mx), Math.abs(mx - (mn - mod)));
	}

}
