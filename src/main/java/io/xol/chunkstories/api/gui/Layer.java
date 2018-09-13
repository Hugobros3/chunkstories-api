//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.gui;

import io.xol.chunkstories.api.gui.elements.InputText;
import io.xol.chunkstories.api.input.Input;
import io.xol.chunkstories.api.input.Mouse.MouseButton;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class Layer {
    protected final Gui gui;
    @Nullable
    protected final Layer parentLayer;

    protected float xPosition, yPosition;
    protected float width, height;

    protected List<GuiElement> elements = new LinkedList<GuiElement>();
    @Nullable
    protected FocusableGuiElement focusedElement = null;

    /**
     * parentLayer might be null, but gui can't be !
     */
    public Layer(Gui gui, @Nullable Layer parentLayer) {
        this.gui = gui;
        this.parentLayer = parentLayer;

        if (gui == null)
            throw new RuntimeException("Fuck off");

        xPosition = 0;
        yPosition = 0;

        width = gui.getViewportWidth();
        height = gui.getViewportHeight();
    }

    public Gui getGui() {
        return gui;
    }

    @Nullable
    public Layer getParentLayer() {
        return parentLayer;
    }

    /**
     * Override this to implement your own drawing routines.
     * You may render the parent layer to have an overlay effect, but it's not mandatory.
     */
    public void render(GuiDrawer drawer) {
        //parentLayer.render(drawer)
    }

    public boolean handleInput(Input input) {
        if (focusedElement != null)
            if (focusedElement.handleInput(input))
                return true;

        if (input instanceof MouseButton) {
            MouseButton mb = (MouseButton) input;
            // System.out.println(mb.getMouse().getCursorX());
            for (GuiElement ge : elements) {
                if (ge.isMouseOver()) {

                    if (ge instanceof FocusableGuiElement)
                        this.setFocusedElement((FocusableGuiElement) ge);

                    if (ge instanceof ClickableGuiElement && ((ClickableGuiElement) ge).handleClick(mb))
                        return true;
                }
            }
        }

        // Forward to parent if not handled
        /* Layer parent = this.parentLayer; if(parent != null) return
         * parent.handleInput(input); */

        return false;
    }

    public boolean handleTextInput(char c) {
        if (focusedElement != null && focusedElement instanceof InputText)
            return ((InputText) focusedElement).handleTextInput(c);

        return false;
    }

    /**
     * Frees and closes ressources
     */
    public void destroy() {

    }

    public float getxPosition() {
        return xPosition;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public float getyPosition() {
        return yPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Nullable
    public FocusableGuiElement getFocusedElement() {
        return focusedElement;
    }

    public void setFocusedElement(@Nullable FocusableGuiElement focusedElement) {
        this.focusedElement = focusedElement;
    }

    public final Layer getRootLayer() {
        if (parentLayer == null)
            return this; // this is the root layer !
        else
            return parentLayer.getRootLayer();
    }

    public void onResize(int newWidth, int newHeight) {
        if (parentLayer != null)
            parentLayer.onResize(newWidth, newHeight);

        this.setWidth(newWidth);
        this.setHeight(newHeight);
    }
}
