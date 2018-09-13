//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.gui.elements;

import io.xol.chunkstories.api.gui.FocusableGuiElement;
import io.xol.chunkstories.api.gui.Font;
import io.xol.chunkstories.api.gui.GuiDrawer;
import io.xol.chunkstories.api.gui.Layer;
import io.xol.chunkstories.api.input.Input;
import io.xol.chunkstories.api.input.Mouse;
import org.joml.Vector4f;

/**
 * Provides a text field to type text into
 */
public class InputText extends FocusableGuiElement {
    private String text = "";

    private Font font;

    private boolean isTransparent = false;
    private boolean password = false;

    public InputText(Layer layer, int x, int y, int width) {
        this(layer, x, y, width, layer.getGui().getFonts().defaultFont());
    }

    public InputText(Layer layer, int x, int y, int width, Font font) {
        super(layer, width, 22);
        setPositionX(x);
        setPositionY(y);
        this.setWidth(width);
        this.setHeight(22);

        this.font = font;
    }

    public boolean handleInput(Input input) {
        if (input.equals("backspace")) {
            if (text.length() > 0)
                text = text.substring(0, text.length() - 1);
            return true;
        }
        return false;
    }

    public boolean handleTextInput(char c) {
        if (c != 0)
            text += c;

        return true;
    }

    public int getWidth() {
        int len = getWidth();
        int txtlen = font.getWidth(text);
        if (txtlen > len)
            len = txtlen;

        return len;
    }

    public boolean isMouseOver(Mouse mouse) {
        return (mouse.getCursorX() >= getPositionX() && mouse.getCursorX() < getPositionX() + getWidth() && mouse.getCursorY() >= getPositionY()
                && mouse.getCursorY() <= getPositionY() + getHeight());
    }

    @Override
    public void render(GuiDrawer renderer) {

        String text = this.text;
        if (password) {
            String passworded = "";
            for (@SuppressWarnings("unused")
                    char c : text.toCharArray())
                passworded += "*";
            text = passworded;
        }

        String backgroundTexture = (isFocused() ? "./textures/gui/textbox.png" : "./textures/gui/textboxnofocus.png");
        if (this.isTransparent)
            backgroundTexture = (isFocused() ? "./textures/gui/textboxnofocustransp.png" : "./textures/gui/textboxtransp.png");

        renderer.drawCorneredBoxTiled(getPositionX(), getPositionY(), getWidth(), getHeight(), 4,
                backgroundTexture, 32);
        renderer.drawStringWithShadow(font, getPositionX() + 4, getPositionY() + 1,
                text + ((isFocused() && System.currentTimeMillis() % 1000 > 500) ? "|" : ""), -1, new Vector4f(1.0f));

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public void setTransparent(boolean isTransparent) {
        this.isTransparent = isTransparent;
    }

    public boolean isPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }
}
