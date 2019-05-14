//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.gui.elements

import xyz.chunkstories.api.gui.GuiDrawer
import xyz.chunkstories.api.gui.Layer
import org.joml.Vector4f

class LargeButtonWithIcon(layer: Layer, private val iconName: String) : Button(layer, layer.gui.fonts.getFont("LiberationSansNarrow-Bold", 16f), 0, 0, iconName) {

    init {
        this.width = 96
        this.height = 48
        this.text = "#{menu.$iconName}"
    }

    override fun render(renderer: GuiDrawer) {
        val localizedText = layer.gui.localization().localize(text)

        var buttonTexture = "textures/gui/scalableButton.png"
        if (isFocused || isMouseOver)
            buttonTexture = "textures/gui/scalableButtonOver.png"

        renderer.drawBoxWithCorners(positionX, positionY, width, height, 8, buttonTexture)

        val font = layer.gui.fonts.getFont("LiberationSansNarrow-Bold", 16.2f)
        val a = 1

        val yPositionText = positionY + 26
        val centering = width / 2 - font.getWidth(localizedText) / 2
        renderer.drawString(font, positionX + centering + 1, yPositionText - 1, localizedText, -1,
                Vector4f(161 / 255f, 161 / 255f, 161 / 255f, 1f))

        renderer.drawString(font, positionX + centering, yPositionText, localizedText, -1,
                Vector4f(38 / 255f, 38 / 255f, 38 / 255f, 1f))

        renderer.drawBox(positionX + width / 2 - 16, positionY + 0,
                32, 32, 0f, 1f, 1f, 0f, "textures/gui/icons/$iconName.png", null)
    }

}
