package io.xol.chunkstories.api.graphics.systems.dispatching

/** A Dispatching system is a system that has the ability to dispatch draw commands in potentially multiple passes.
 * The passes do not get to choose whether or not they receive the input vertex data. */
interface DispatchingSystem