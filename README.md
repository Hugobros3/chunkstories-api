# chunkstories-api

![alt text](http://chunkstories.xyz/img/github_header3.png "Header screenshot")

[![Build Status](https://travis-ci.com/Hugobros3/chunkstories-api.svg?branch=master)](https://travis-ci.com/Hugobros3/chunkstories-api)

This repository contains the API for the [Chunk Stories](https://github.com/Hugobros3/chunkstories) project. The `chunkstories` project implements this API, and all mods (including the [core content](https://github.com/Hugobros3/chunkstories-core) of the game) link against the API. 

To learn more about Chunk Stories in general, please [read the readme of the main repo.](https://github.com/Hugobros3/chunkstories)

# Building

*This is for building `chunkstories-api`, the game's API. If you are only looking to write mods, you do not have to mess with this at all and should rather follow the [mods creation guide](http://chunkstories.xyz/wiki/doku.php?id=mod_setup) on the project Wiki !*

## Setup

Nothing in particular to do. Gradle handles the dependencies for you.

[Enklume](https://github.com/Hugobros3/Enklume) is our homegrown Minecraft Anvil file format library. Artifacts are in our repository, you don't have to worry about it.

## Gradle Tasks

 * `./gradlew install` builds the API and installs it to the local maven repository.

# Links

 * To lean how to play the game and register an account, please visit http://chunkstories.xyz
 * You can find a lot more information on the game wiki, including guides to writing mods, at http://chunkstories.xyz/wiki/
 * You can find videos and dev logs on the lead developper youtube channel: http://youtube.com/Hugobros3
 * We have a discord where anyone can discuss with the devs: https://discord.gg/wudd4pe
 * You can get support either by opening a issue on this project or by visiting the subreddit over at https://reddit.com/r/chunkstories

# License

`chunkstories-api`'s **implementation** is released under LGPL, see LICENSE.MD