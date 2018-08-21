package io.xol.chunkstories.api.graphics

abstract class Texture(val format: TextureFormat) {

}

abstract class Texture1D(format: TextureFormat, var width: Int) : Texture(format) {

}

abstract class Texture2D(format: TextureFormat, var width : Int, var height : Int) : Texture(format) {

}

abstract class Texture3D(format: TextureFormat, var width : Int, var height : Int, var depth : Int) : Texture(format) {

}