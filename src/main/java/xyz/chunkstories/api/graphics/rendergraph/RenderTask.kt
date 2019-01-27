package xyz.chunkstories.api.graphics.rendergraph

open class RenderTask() {
    var name: String = "undefined"
        set(value) {
            if (field == "undefined") {
                field = value
            } else throw Exception("You can't rename a pass !")
        }

}