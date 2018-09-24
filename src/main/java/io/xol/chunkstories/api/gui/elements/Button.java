//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.gui.elements;

import io.xol.chunkstories.api.gui.*;
import org.joml.Vector4f;

import io.xol.chunkstories.api.input.Mouse;
import io.xol.chunkstories.api.input.Mouse.MouseButton;

import javax.annotation.Nullable;

/** Provides a scalable button */
public class Button extends FocusableGuiElement implements ClickableGuiElement {
	protected String text;
	protected Font font;

	@Nullable
	protected Runnable action;

	public Button(Layer layer, int x, int y, String text) {
		this(layer, x, y, text, null);
	}

	public Button(Layer layer, int x, int y, int width, String text) {
		this(layer, x, y, text, null);
		this.setWidth(width);
	}

	public Button(Layer layer, int x, int y, String text, @Nullable Runnable action) {
		this(layer, layer.getGui().getFonts().getFont("LiberationSans-Regular", 12), x, y, text);
		this.action = action;
	}

	public Button(Layer layer, Font font, int x, int y, String text) {
		super(layer, 0, 22);
		this.font = font;

		this.setPositionX(x);
		this.setPositionY(y);
		this.text = text;
	}

	public int getWidth() {
		String localizedText = getLayer().getGui().localization().localize(text);
		int width = font.getWidth(localizedText) + 8;

		if (this.getWidth() > width)
			width = this.getWidth();

		return width;
	}

	public boolean isMouseOver(Mouse mouse) {
		return (mouse.getCursorX() >= getPositionX() && mouse.getCursorX() < getPositionX() + getWidth() && mouse.getCursorY() >= getPositionY()
				&& mouse.getCursorY() <= getPositionY() + getHeight());
	}

	@Override
	public void render(GuiDrawer renderer) {
		String localizedText = getLayer().getGui().localization().localize(text);

		String buttonTexture = "./textures/gui/scalableButton2.png";
		if (isFocused() || isMouseOver())
			buttonTexture = "./textures/gui/scalableButtonOver2.png";

		renderer.drawBoxWithCorners(getPositionX(), getPositionY(), getWidth(), getHeight(), 8, buttonTexture);
		renderer.drawString(font, getPositionX() + 4, getPositionY(), localizedText, -1, new Vector4f(0, 0, 0, 1));
	}

	@Override
	public boolean handleClick(MouseButton mouseButton) {
		if (!mouseButton.equals("mouse.left"))
			return false;

		this.getLayer().getGui().getClient().getSoundManager().playSoundEffect("./sounds/gui/gui_click2.ogg");

		if (this.action != null)
			this.action.run();

		return true;
	}

	public void setAction(@Nullable Runnable runnable) {
		this.action = runnable;
	}
}
