package xyz.chunkstories.api

import org.slf4j.Logger
import xyz.chunkstories.api.workers.Tasks

/** Parent interface to Client/Server */
interface Engine {
    val tasks: Tasks
    val logger: Logger
}