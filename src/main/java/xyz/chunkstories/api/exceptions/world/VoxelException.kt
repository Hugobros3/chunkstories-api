//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.world

import xyz.chunkstories.api.world.cell.CellData

/** An exception about some voxel Doesn't extend ChunkException to account for
 * the fact some exceptions come from unloaded stuff, or that some voxel
 * contexts are actually not referencing any real world  */
abstract class VoxelException(val context: CellData) : WorldException(context.world)
