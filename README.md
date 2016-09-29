# chunkstories-api
Public, open-source part of the ChunkStories source code.
http://chunkstories.xyz

## What is Chunk Stories ?

Chunk Stories is a voxel game engine made by XolioWare Interactive. It features 
 * Dynamically loaded worlds of up to 65536x1024x65536 voxels ( for now )
 * World wrapping in a torus mapping
 * Voxel-based lightning, usual stuff you'd expect from any voxel game engine
 * A somewhat decent OpenGL 2.x / 3.x renderer : Deffered shading, normal mapping, SSR, dynamic envmaps, dynamic light sources, shadow mapping, bloom, particles, decals, gamma-correction, custom shaders support
 * Configuration files for almost everything ( inputs, voxel definitions, materials, ... )
 * What is not in a configuration file certainly is overridable by use of an overriden class or a plugin
 * Dynamic (run-time) loading, downloading, updating of mods, especially from remote servers
 * Client/Master model, world streaming, neat code patterns where usually you get ugly trickery

### How to use this API ?

This is only meant as a source archive. To use the API please refer to the game's wiki at
http://chunkstories.xyz/wiki/

### What is the license of this project ?

We have not yet settled on a definitive plan for licensing our main game code and it's api's. For now feel free to check out what is currently online, for learning purposes.
