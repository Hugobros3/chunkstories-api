//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.gui.elements

import xyz.chunkstories.api.gui.Font
import xyz.chunkstories.api.gui.GuiDrawer
import xyz.chunkstories.api.gui.Layer
import org.joml.Vector4f

class LargeButton(layer: Layer, text: String) : Button(layer, layer.gui.fonts.getFont("LiberationSansNarrow-Bold", 18.666f), 0, 0, text) {

    init {
        this.width = 96
        this.height = 24

        this.text = "#{menu.$text}"
    }

    override fun render(renderer: GuiDrawer) {
        val localizedText = layer.gui.localization().localize(text)

        var buttonTexture = "textures/gui/mainMenu.png"
        if (isFocused || isMouseOver)
            buttonTexture = "textures/gui/mainMenuOver.png"

        renderer.drawBoxWithCorners(positionX, positionY, width, height, 8, buttonTexture)

        val font = renderer.fonts.getFont("LiberationSansNarrow-Bold", 16.5f)

        val yPositionText = positionY + 2

        val centering = width / 2 - font.getWidth(localizedText) / 2
        renderer.drawString(font, positionX + centering + 1, yPositionText - 1, localizedText, -1,
                Vector4f(161 / 255f, 161 / 255f, 161 / 255f, 1f))
        renderer.drawString(font, positionX + centering, yPositionText, localizedText, -1,
                Vector4f(38 / 255f, 38 / 255f, 38 / 255f, 1f))
    }

}
