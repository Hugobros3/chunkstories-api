package io.xol.chunkstories.api.content

interface Declaration {
    val name: String
    val ext: Map<String, String>
}

interface DeclarationContext {
    var name: String
    val ext: MutableMap<String, String>
}