//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.math.random;

import org.joml.Vector4d;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.ArrayList;
import java.util.List;

// This class takes care of providing the simplex noise functions for the world
// generator. It is based on other people's work as the comments below show.

/**
 * A speed-improved simplex noise algorithm for 2D, 3D and 4D in Java.
 * <p>
 * Based on example code by Stefan Gustavson (stegu@itn.liu.se). Optimisations
 * by Peter Eastman (peastman@drizzle.stanford.edu). Better rank ordering method
 * by Stefan Gustavson in 2012.
 * <p>
 * This could be speeded up even further, but it's useful as it is.
 * <p>
 * Version 2012-03-09
 * <p>
 * This code was placed in the public domain by its original author, Stefan
 * Gustavson. You may use it as you see fit, but attribution is appreciated.
 */
public class SeededSimplexNoiseGenerator {
	private Grad[] grad3 = { new Grad(1, 1, 0), new Grad(-1, 1, 0), new Grad(1, -1, 0), new Grad(-1, -1, 0), new Grad(1, 0, 1), new Grad(-1, 0, 1),
			new Grad(1, 0, -1), new Grad(-1, 0, -1), new Grad(0, 1, 1), new Grad(0, -1, 1), new Grad(0, 1, -1), new Grad(0, -1, -1) };

	private Grad[] grad4 = {
			new Grad(0, 1, 1, 1), new Grad(0, 1, 1, -1), new Grad(0, 1, -1, 1), new Grad(0, 1, -1, -1),
			new Grad(0, -1, 1, 1), new Grad(0, -1, 1, -1), new Grad(0, -1, -1, 1), new Grad(0, -1, -1, -1),

			new Grad(1, 0, 1, 1), new Grad(1, 0, 1, -1), new Grad(1, 0, -1, 1), new Grad(1, 0, -1, -1),
			new Grad(-1, 0, 1, 1), new Grad(-1, 0, 1, -1), new Grad(-1, 0, -1, 1), new Grad(-1, 0, -1, -1),

			new Grad(1, 1, 0, 1), new Grad(1, 1, 0, -1), new Grad(1, -1, 0, 1), new Grad(1, -1, 0, -1),
			new Grad(-1, 1, 0, 1), new Grad(-1, 1, 0, -1), new Grad(-1, -1, 0, 1), new Grad(-1, -1, 0, -1),

			new Grad(1, 1, 1, 0), new Grad(1, 1, -1, 0), new Grad(1, -1, 1, 0), new Grad(1, -1, -1, 0),
			new Grad(-1, 1, 1, 0), new Grad(-1, 1, -1, 0), new Grad(-1, -1, 1, 0), new Grad(-1, -1, -1, 0) };

	// To remove the need for index wrapping, float the permutation table length
	public short[] perm = new short[512];
	private short[] permMod12 = new short[512];

	public SeededSimplexNoiseGenerator(String seed) {
		// Init p array based on seed
		short[] p = new short[256];
		List<Short> temp = new ArrayList<Short>();
		for (short i = 0; i < 256; i++) {
			temp.add(i);
		}
		byte[] seedBytes = seed.getBytes();
		for (int i = 0; i < 256; i++) {
			int select = seedBytes[i % seedBytes.length];
			if (select < temp.size()) {
				p[i] = temp.get(select);
				temp.remove(select);
			} else {
				p[i] = temp.get(0);
				temp.remove(0);
			}
		}
		// Init perm arrays
		for (int i = 0; i < 512; i++) {
			perm[i] = p[i & 255];
			permMod12[i] = (short) (perm[i] % 12);
		}
	}

	// Skewing and unskewing factors for 2, 3, and 4 dimensions
	private final float F2 = (float) (0.5f * (Math.sqrt(3.0) - 1.0));
	private final float G2 = (float) ((3.0f - Math.sqrt(3.0)) / 6.0);
	private final float F4 = (float) ((Math.sqrt(5.0) - 1.0) / 4.0);
	private final float G4 = (float) ((5.0f - Math.sqrt(5.0)) / 20.0);

	// This method is a *lot* faster than using (int)Math.floor(x)
	private int fastfloor(float x) {
		int xi = (int) x;
		return x < xi ? xi - 1 : xi;
	}

	private float dot(Grad g, float x, float y) {
		return g.x * x + g.y * y;
	}

	private float dot(Grad g, float x, float y, float z) {
		return g.x * x + g.y * y + g.z * z;
	}

	private float dot(Grad g, float x, float y, float z, float w) {
		return g.x * x + g.y * y + g.z * z + g.w * w;
	}

	public float looped_noise(float x, float y, float period) {
		float s = x / period;
		float t = y / period;

		return looped_noise(s, t, zero, one);
	}

	private Vector4f one = new Vector4f(1f);
	private Vector4f zero = new Vector4f(0f);

	public float looped_noise(float s, float t, Vector4fc offset, Vector4fc multiplier) {

		float pi = (float) Math.PI;

		float nx = (float) Math.cos(s * 2 * pi) * multiplier.x() + offset.x();
		float ny = (float) Math.cos(t * 2 * pi) * multiplier.y() + offset.y();
		float nz = (float) Math.sin(s * 2 * pi) * multiplier.z() + offset.z();
		float nw = (float) Math.sin(t * 2 * pi) * multiplier.w() + offset.w();

		return noise(nx, ny, nz, nw);
	}

	public float looped_noise3d(float x, float y, float z, float period, float x1, float y1, float x2, float y2) {
		float s = x / period;
		float t = y / period;

		z /= period / 15;

		float pi = (float) Math.PI;

		float nx = (float) (x1 + Math.cos(s * 2 * pi) * x2);
		float ny = (float) (y1 + Math.cos(t * 2 * pi) * y2);
		float nz = (float) (x1 + Math.sin(s * 2 * pi) * x2);
		float nw = (float) (y1 + Math.sin(t * 2 * pi) * y2);

		return (noise(nx, ny, z) + noise(nz, nw, z)) / 2;
	}

	// 2D simplex noise
	public float noise(float xin, float yin) {
		float n0, n1, n2; // Noise contributions from the three corners
		// Skew the input space to determine which simplex cell we're in
		float s = (xin + yin) * F2; // Hairy factor for 2D
		int i = fastfloor(xin + s);
		int j = fastfloor(yin + s);
		float t = (i + j) * G2;
		float X0 = i - t; // Unskew the cell origin back to (x,y) space
		float Y0 = j - t;
		float x0 = xin - X0; // The x,y distances from the cell origin
		float y0 = yin - Y0;
		// For the 2D case, the simplex shape is an equilateral triangle.
		// Determine which simplex we are in.
		int i1, j1; // Offsets for second (middle) corner of simplex in (i,j)
		// coords
		if (x0 > y0) {
			i1 = 1;
			j1 = 0;
		} // lower triangle, XY order: (0,0)->(1,0)->(1,1)
		else {
			i1 = 0;
			j1 = 1;
		} // upper triangle, YX order: (0,0)->(0,1)->(1,1)
		// A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
		// a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
		// c = (3-sqrt(3))/6
		float x1 = x0 - i1 + G2; // Offsets for middle corner in (x,y) unskewed
		// coords
		float y1 = y0 - j1 + G2;
		float x2 = x0 - 1.0f + 2.0f * G2; // Offsets for last corner in (x,y)
		// unskewed coords
		float y2 = y0 - 1.0f + 2.0f * G2;
		// Work out the hashed gradient indices of the three simplex corners
		int ii = i & 255;
		int jj = j & 255;
		int gi0 = permMod12[ii + perm[jj]];
		int gi1 = permMod12[ii + i1 + perm[jj + j1]];
		int gi2 = permMod12[ii + 1 + perm[jj + 1]];
		// Calculate the contribution from the three corners
		float t0 = 0.5f - x0 * x0 - y0 * y0;
		if (t0 < 0)
			n0 = 0.0f;
		else {
			t0 *= t0;
			n0 = t0 * t0 * dot(grad3[gi0], x0, y0); // (x,y) of grad3 used for
			// 2D gradient
		}
		float t1 = 0.5f - x1 * x1 - y1 * y1;
		if (t1 < 0)
			n1 = 0.0f;
		else {
			t1 *= t1;
			n1 = t1 * t1 * dot(grad3[gi1], x1, y1);
		}
		float t2 = 0.5f - x2 * x2 - y2 * y2;
		if (t2 < 0)
			n2 = 0.0f;
		else {
			t2 *= t2;
			n2 = t2 * t2 * dot(grad3[gi2], x2, y2);
		}
		// Add contributions from each corner to get the final noise value.
		// The result is scaled to return values in the interval [-1,1].
		return 70.0f * (n0 + n1 + n2);
	}

	// 3D simplex noise
	public float noise(float xin, float yin, float zin) {
		float n0, n1, n2, n3; // Noise contributions from the four corners
		// Skew the input space to determine which simplex cell we're in
		float f3 = (float) (1.0f / 3.0);
		float s = (xin + yin + zin) * f3; // Very nice and simple skew factor
		// for 3D
		int i = fastfloor(xin + s);
		int j = fastfloor(yin + s);
		int k = fastfloor(zin + s);
		float g3 = (float) (1.0f / 6.0);
		float t = (i + j + k) * g3;
		float X0 = i - t; // Unskew the cell origin back to (x,y,z) space
		float Y0 = j - t;
		float Z0 = k - t;
		float x0 = xin - X0; // The x,y,z distances from the cell origin
		float y0 = yin - Y0;
		float z0 = zin - Z0;
		// For the 3D case, the simplex shape is a slightly irregular
		// tetrahedron.
		// Determine which simplex we are in.
		int i1, j1, k1; // Offsets for second corner of simplex in (i,j,k)
		// coords
		int i2, j2, k2; // Offsets for third corner of simplex in (i,j,k) coords
		if (x0 >= y0) {
			if (y0 >= z0) {
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			} // X Y Z order
			else if (x0 >= z0) {
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			} // X Z Y order
			else {
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			} // Z X Y order
		} else { // x0<y0
			if (y0 < z0) {
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 0;
				j2 = 1;
				k2 = 1;
			} // Z Y X order
			else if (x0 < z0) {
				i1 = 0;
				j1 = 1;
				k1 = 0;
				i2 = 0;
				j2 = 1;
				k2 = 1;
			} // Y Z X order
			else {
				i1 = 0;
				j1 = 1;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			} // Y X Z order
		}
		// A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
		// a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z),
		// and
		// a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z),
		// where
		// c = 1/6.
		float x1 = x0 - i1 + g3; // Offsets for second corner in (x,y,z) coords
		float y1 = y0 - j1 + g3;
		float z1 = z0 - k1 + g3;
		float x2 = x0 - i2 + 2.0f * g3; // Offsets for third corner in (x,y,z)
		// coords
		float y2 = y0 - j2 + 2.0f * g3;
		float z2 = z0 - k2 + 2.0f * g3;
		float x3 = x0 - 1.0f + 3.0f * g3; // Offsets for last corner in (x,y,z)
		// coords
		float y3 = y0 - 1.0f + 3.0f * g3;
		float z3 = z0 - 1.0f + 3.0f * g3;
		// Work out the hashed gradient indices of the four simplex corners
		int ii = i & 255;
		int jj = j & 255;
		int kk = k & 255;
		int gi0 = permMod12[ii + perm[jj + perm[kk]]];
		int gi1 = permMod12[ii + i1 + perm[jj + j1 + perm[kk + k1]]];
		int gi2 = permMod12[ii + i2 + perm[jj + j2 + perm[kk + k2]]];
		int gi3 = permMod12[ii + 1 + perm[jj + 1 + perm[kk + 1]]];
		// Calculate the contribution from the four corners
		float t0 = 0.6f - x0 * x0 - y0 * y0 - z0 * z0;
		if (t0 < 0)
			n0 = 0.0f;
		else {
			t0 *= t0;
			n0 = t0 * t0 * dot(grad3[gi0], x0, y0, z0);
		}
		float t1 = 0.6f - x1 * x1 - y1 * y1 - z1 * z1;
		if (t1 < 0)
			n1 = 0.0f;
		else {
			t1 *= t1;
			n1 = t1 * t1 * dot(grad3[gi1], x1, y1, z1);
		}
		float t2 = 0.6f - x2 * x2 - y2 * y2 - z2 * z2;
		if (t2 < 0)
			n2 = 0.0f;
		else {
			t2 *= t2;
			n2 = t2 * t2 * dot(grad3[gi2], x2, y2, z2);
		}
		float t3 = 0.6f - x3 * x3 - y3 * y3 - z3 * z3;
		if (t3 < 0)
			n3 = 0.0f;
		else {
			t3 *= t3;
			n3 = t3 * t3 * dot(grad3[gi3], x3, y3, z3);
		}
		// Add contributions from each corner to get the final noise value.
		// The result is scaled to stay just inside [-1,1]
		return 32.0f * (n0 + n1 + n2 + n3);
	}

	// 4D simplex noise, better simplex rank ordering method 2012-03-09
	public float noise(float x, float y, float z, float w) {
		float n0, n1, n2, n3, n4; // Noise contributions from the five corners
		// Skew the (x,y,z,w) space to determine which cell of 24 simplices
		// we're in
		float s = (x + y + z + w) * F4; // Factor for 4D skewing
		int i = fastfloor(x + s);
		int j = fastfloor(y + s);
		int k = fastfloor(z + s);
		int l = fastfloor(w + s);
		float t = (i + j + k + l) * G4; // Factor for 4D unskewing
		float X0 = i - t; // Unskew the cell origin back to (x,y,z,w) space
		float Y0 = j - t;
		float Z0 = k - t;
		float W0 = l - t;
		float x0 = x - X0; // The x,y,z,w distances from the cell origin
		float y0 = y - Y0;
		float z0 = z - Z0;
		float w0 = w - W0;
		// For the 4D case, the simplex is a 4D shape I won't even try to
		// describe.
		// To find out which of the 24 possible simplices we're in, we need to
		// determine the magnitude ordering of x0, y0, z0 and w0.
		// Six pair-wise comparisons are performed between each possible pair
		// of the four coordinates, and the results are used to rank the
		// numbers.
		int rankx = 0;
		int ranky = 0;
		int rankz = 0;
		int rankw = 0;
		if (x0 > y0)
			rankx++;
		else
			ranky++;
		if (x0 > z0)
			rankx++;
		else
			rankz++;
		if (x0 > w0)
			rankx++;
		else
			rankw++;
		if (y0 > z0)
			ranky++;
		else
			rankz++;
		if (y0 > w0)
			ranky++;
		else
			rankw++;
		if (z0 > w0)
			rankz++;
		else
			rankw++;
		int i1, j1, k1, l1; // The integer offsets for the second simplex corner
		int i2, j2, k2, l2; // The integer offsets for the third simplex corner
		int i3, j3, k3, l3; // The integer offsets for the fourth simplex corner
		// simplex[c] is a 4-vector with the numbers 0, 1, 2 and 3 in some
		// order.
		// Many values of c will never occur, since e.g. x>y>z>w makes x<z, y<w
		// and x<w
		// impossible. Only the 24 indices which have non-zero entries make any
		// sense.
		// We use a thresholding to set the coordinates in turn from the largest
		// magnitude.
		// Rank 3 denotes the largest coordinate.
		i1 = rankx >= 3 ? 1 : 0;
		j1 = ranky >= 3 ? 1 : 0;
		k1 = rankz >= 3 ? 1 : 0;
		l1 = rankw >= 3 ? 1 : 0;
		// Rank 2 denotes the second largest coordinate.
		i2 = rankx >= 2 ? 1 : 0;
		j2 = ranky >= 2 ? 1 : 0;
		k2 = rankz >= 2 ? 1 : 0;
		l2 = rankw >= 2 ? 1 : 0;
		// Rank 1 denotes the second smallest coordinate.
		i3 = rankx >= 1 ? 1 : 0;
		j3 = ranky >= 1 ? 1 : 0;
		k3 = rankz >= 1 ? 1 : 0;
		l3 = rankw >= 1 ? 1 : 0;
		// The fifth corner has all coordinate offsets = 1, so no need to
		// compute that.
		float x1 = x0 - i1 + G4; // Offsets for second corner in (x,y,z,w)
		// coords
		float y1 = y0 - j1 + G4;
		float z1 = z0 - k1 + G4;
		float w1 = w0 - l1 + G4;
		float x2 = x0 - i2 + 2.0f * G4; // Offsets for third corner in (x,y,z,w)
		// coords
		float y2 = y0 - j2 + 2.0f * G4;
		float z2 = z0 - k2 + 2.0f * G4;
		float w2 = w0 - l2 + 2.0f * G4;
		float x3 = x0 - i3 + 3.0f * G4; // Offsets for fourth corner in
		// (x,y,z,w) coords
		float y3 = y0 - j3 + 3.0f * G4;
		float z3 = z0 - k3 + 3.0f * G4;
		float w3 = w0 - l3 + 3.0f * G4;
		float x4 = x0 - 1.0f + 4.0f * G4; // Offsets for last corner in (x,y,z,w)
		// coords
		float y4 = y0 - 1.0f + 4.0f * G4;
		float z4 = z0 - 1.0f + 4.0f * G4;
		float w4 = w0 - 1.0f + 4.0f * G4;
		// Work out the hashed gradient indices of the five simplex corners
		int ii = i & 255;
		int jj = j & 255;
		int kk = k & 255;
		int ll = l & 255;
		int gi0 = perm[ii + perm[jj + perm[kk + perm[ll]]]] % 32;
		int gi1 = perm[ii + i1 + perm[jj + j1 + perm[kk + k1 + perm[ll + l1]]]] % 32;
		int gi2 = perm[ii + i2 + perm[jj + j2 + perm[kk + k2 + perm[ll + l2]]]] % 32;
		int gi3 = perm[ii + i3 + perm[jj + j3 + perm[kk + k3 + perm[ll + l3]]]] % 32;
		int gi4 = perm[ii + 1 + perm[jj + 1 + perm[kk + 1 + perm[ll + 1]]]] % 32;
		// Calculate the contribution from the five corners
		float t0 = 0.6f - x0 * x0 - y0 * y0 - z0 * z0 - w0 * w0;
		if (t0 < 0)
			n0 = 0.0f;
		else {
			t0 *= t0;
			n0 = t0 * t0 * dot(grad4[gi0], x0, y0, z0, w0);
		}
		float t1 = 0.6f - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1;
		if (t1 < 0)
			n1 = 0.0f;
		else {
			t1 *= t1;
			n1 = t1 * t1 * dot(grad4[gi1], x1, y1, z1, w1);
		}
		float t2 = 0.6f - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
		if (t2 < 0f)
			n2 = 0.0f;
		else {
			t2 *= t2;
			n2 = t2 * t2 * dot(grad4[gi2], x2, y2, z2, w2);
		}
		float t3 = 0.6f - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
		if (t3 < 0f)
			n3 = 0.0f;
		else {
			t3 *= t3;
			n3 = t3 * t3 * dot(grad4[gi3], x3, y3, z3, w3);
		}
		float t4 = 0.6f - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
		if (t4 < 0f)
			n4 = 0.0f;
		else {
			t4 *= t4;
			n4 = t4 * t4 * dot(grad4[gi4], x4, y4, z4, w4);
		}
		// Sum up and scale the result to cover the range [-1,1]
		return 27.0f * (n0 + n1 + n2 + n3 + n4);
	}

	// Inner class to speed upp gradient computations
	// (array access is a lot slower than member access)
	private static class Grad {
		float x, y, z, w;

		Grad(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		Grad(float x, float y, float z, float w) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.w = w;
		}
	}
}
