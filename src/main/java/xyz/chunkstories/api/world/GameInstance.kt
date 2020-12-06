package xyz.chunkstories.api.world

import org.slf4j.Logger
import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.ContentTranslator
import xyz.chunkstories.api.plugin.PluginManager

interface GameInstance {
    val world: World
    val content: Content
    val contentTranslator: ContentTranslator
    val pluginManager: PluginManager

    val logger: Logger
}