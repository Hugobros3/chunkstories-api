package io.xol.chunkstories.api.graphics.systems.drawing

/** For many effects in computer graphics, you want to have a pass that just touches every pixel in the framebuffer to
 * apply some fragment shader to it. This system does exactly and nothing but that, added to a pass it means the pass
 * will just draw a quad without having to write explicit code. */
interface FullscreenQuadDrawer : DrawingSystem