//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.gui.elements;

import io.xol.chunkstories.api.gui.Font;
import io.xol.chunkstories.api.gui.GuiDrawer;
import io.xol.chunkstories.api.gui.Layer;
import org.joml.Vector4f;

public class LargeButton extends Button {

    public LargeButton(Layer layer, String text) {
        super(layer, layer.getGui().getFonts().getFont("LiberationSansNarrow-Bold__aa", 18.666f), 0, 0, text);
        this.setWidth(96);
        this.setHeight(24);

        this.text = "#{menu." + text + "}";
    }

    @Override
    public void render(GuiDrawer renderer) {
        String localizedText = getLayer().getGui().localization().localize(text);

        String buttonTexture = ("./textures/gui/mainMenu.png");
        if (isFocused() || isMouseOver())
            buttonTexture = ("./textures/gui/mainMenuOver.png");

        renderer.drawBoxWithCorners(getPositionX(), getPositionY(), getWidth(), getHeight(), 8, buttonTexture);

        Font font = renderer.getFonts().getFont("LiberationSansNarrow-Bold__aa", 16f);

        int yPositionText = getPositionY() + 2;

        int centering = getWidth() / 2 - font.getWidth(localizedText);
        renderer.drawString(font, getPositionX() + centering + 1, yPositionText - 1, localizedText, -1,
                new Vector4f(161 / 255f, 161 / 255f, 161 / 255f, 1));
        renderer.drawString(font, getPositionX() + centering, yPositionText, localizedText, -1,
                new Vector4f(38 / 255f, 38 / 255f, 38 / 255f, 1));
    }

}
