//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.gui;

import io.xol.chunkstories.api.input.Mouse;
import io.xol.chunkstories.api.rendering.RenderingInterface;

public abstract class GuiElement {
	protected final Layer layer;

	protected float xPosition = 0;
	protected float yPosition = 0;

	protected float width, height;

	protected GuiElement(Layer layer) {
		this.layer = layer;
	}

	public float getPositionX() {
		return xPosition;
	}

	public float getPositionY() {
		return yPosition;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public void setPositionX(float xPosition) {
		this.xPosition = xPosition;
	}

	public void setPositionY(float yPosition) {
		this.yPosition = yPosition;
	}

	public void setPosition(float x, float y) {
		setPositionX(x);
		setPositionY(y);
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	/** Is the mouse over this object */
	public boolean isMouseOver(Mouse mouse) {
		return mouse.getCursorX() >= this.getPositionX() && mouse.getCursorY() >= this.getPositionY()
				&& mouse.getCursorX() <= this.getPositionX() + this.getWidth()
				&& mouse.getCursorY() <= this.getPositionY() + this.getHeight();
	}

	public final boolean isMouseOver() {
		return isMouseOver(layer.getGameWindow().getClient().getInputsManager().getMouse());
	}

	public abstract void render(RenderingInterface renderer);
}
