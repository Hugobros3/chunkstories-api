# chunkstories-api
Open Source (license TBD, see below) api for making mods for the game 'Chunk Stories'
http://chunkstories.xyz

## What is Chunk Stories ?

Chunk Stories is a voxel game engine made by Hugo "Gobrosse" Devillers. Featuring !
 * A Bukkit-inspired modding API for both client and server.
 * The ability for mods to override each other, using a virtual FS.
 * Completely customizable game mechanics, every single gameplay feature is defined by configuration files referencing hotloadable code. Nothing game-related is in implementation.
 * A somewhat decent OpenGL 3.3 renderer : Deffered shading, normal mapping, SSR, dynamic envmaps, dynamic light sources, shadow mapping, cubemaps, crude LoD, bloom, particles, decals, gamma-correction, custom shaders support ...
 * Configuration files for almost everything ( items, entities, inputs, voxel definitions, materials, ... )
 * What is not in a configuration file certainly is overridable by use of an overriden class or a plugin
 * Dynamically loaded worlds of up to 65x1x65kcubes ( for now )
 * World wrapping in a torus mapping (reaching the end of the world takes you to the other end)
 * Voxel-based lightning, and most of the usual fluff you'd expect from any voxel game engine
 * Client/Master model with world streaming and a dedicated server

### How to use this API ?

This API is a Gradle project. You can use it without installing anything by leveraging the Gradle wrapper bundled with it. Simply clone the repo and type in 'gradlew' to use the wrapper.
Typing in 'gradlew install' will build the API and install it to your local Maven repository. You can then use the created jars in build/libs.

Alternatively, you can use the maven repo at
http://maven.xol.io/repository/public to obtain binaries. The groupId is 'io.xol.chunkstories', the artifactId is 'api' and the version is usually a decimal number in the hundreds, '116' currently.

To lean how to play the game and register an account, please visit http://chunkstories.xyz

You can find a lot more information on the game wiki, including guides to writing mods, at http://chunkstories.xyz/wiki/

You can find videos and dev logs on the lead developper youtube channel: http://youtube.com/Hugobros3

We have a discord where anyone can discuss with the devs: https://discord.gg/wudd4pe

You can get support either by opening a issue on this project or by visiting the subreddit over at https://reddit.com/r/chunkstories

### What is the license of this project ?

We have not yet settled on a definitive plan for licensing our main game code, nor it's api's. For now feel free to check out what is online, we're looking to have a talk about the exact license this API will be put as. Feel free to hit us with suggestions and discussion, this isn't a taboo !
