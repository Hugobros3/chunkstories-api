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

public class LargeButtonWithIcon extends Button {

    private String iconName;

    public LargeButtonWithIcon(Layer layer, String iconName) {
        super(layer, layer.getGui().getFonts().getFont("LiberationSansNarrow-Bold__aa", 16f), 0, 0, iconName);
        this.setWidth(96);
        this.setHeight(48);

        this.iconName = iconName;
        this.text = "#{menu." + iconName + "}";
    }

    @Override
    public void render(GuiDrawer renderer) {
        String localizedText = getLayer().getGui().localization().localize(text);

        String buttonTexture = "./textures/gui/mainMenu.png";
        if (isFocused() || isMouseOver())
            buttonTexture = "./textures/gui/mainMenuOver.png";

        renderer.drawBoxWithCorners(getPositionX(), getPositionY(), getWidth(), getHeight(), 8, buttonTexture);

        Font font = getLayer().getGui().getFonts().getFont("LiberationSansNarrow-Bold__aa", 32f);
        int a = 1;

        int yPositionText = getPositionY() + 26;
        int centering = getWidth() / 2 - font.getWidth(localizedText) * a / 2;
        renderer.drawString(font, getPositionX() + centering + 1, yPositionText - 1, localizedText, -1,
                new Vector4f(161 / 255f, 161 / 255f, 161 / 255f, 1));
        renderer.drawString(font, getPositionX() + centering, yPositionText, localizedText, -1,
                new Vector4f(38 / 255f, 38 / 255f, 38 / 255f, 1));

        renderer.drawBox(getPositionX() + getWidth() / 2 - 16, getPositionY() + getHeight() / 2 - 26,
                32, 32, 0, 1, 1, 0, "./textures/gui/icons/" + iconName + ".png", null);
    }

}