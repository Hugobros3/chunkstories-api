//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.gui.elements;

import org.joml.Vector4f;

import io.xol.chunkstories.api.gui.ClickableGuiElement;
import io.xol.chunkstories.api.gui.FocusableGuiElement;
import io.xol.chunkstories.api.gui.Layer;
import io.xol.chunkstories.api.input.Mouse;
import io.xol.chunkstories.api.input.Mouse.MouseButton;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.text.FontRenderer.Font;
import io.xol.chunkstories.api.rendering.textures.Texture2D;

import javax.annotation.Nullable;

/** Provides a scalable button */
public class BaseButton extends FocusableGuiElement implements ClickableGuiElement {
	protected String text;
	protected Font font;
	protected float fontScaler = 1.0f;

	@Nullable
	protected Runnable action;

	public BaseButton(Layer layer, int x, int y, String text) {
		this(layer, x, y, text, null);
	}

	public BaseButton(Layer layer, int x, int y, int width, String text) {
		this(layer, x, y, text, null);
		this.setWidth(width);
	}

	public BaseButton(Layer layer, int x, int y, String text, @Nullable Runnable action) {
		this(layer, layer.getGameWindow().getFontRenderer().getFont("LiberationSans-Regular", 12), x, y, text);
		this.action = action;
	}

	public BaseButton(Layer layer, Font font, int x, int y, String text) {
		super(layer);
		this.font = font;

		this.xPosition = x;
		this.yPosition = y;
		this.text = text;
		this.height = 22;
	}

	protected int scale() {
		return layer.getGuiScale();
	}

	public float getWidth() {
		String localizedText = layer.getGameWindow().getClient().getContent().localization().localize(text);
		float width = font.getWidth(localizedText) * fontScaler + 8;

		if (this.width > width)
			width = this.width;

		return (width) * scale();
	}

	public float getHeight() {
		return height * scale();
	}

	public boolean isMouseOver(Mouse mouse) {
		return (mouse.getCursorX() >= xPosition && mouse.getCursorX() < xPosition + getWidth()
				&& mouse.getCursorY() >= yPosition && mouse.getCursorY() <= yPosition + getHeight());
	}

	@Override
	public void render(RenderingInterface renderer) {
		float width = getWidth();
		String localizedText = layer.getGameWindow().getClient().getContent().localization().localize(text);

		Texture2D buttonTexture = renderer.textures().getTexture("./textures/gui/scalableButton2.png");
		if (isFocused() || isMouseOver())
			buttonTexture = renderer.textures().getTexture("./textures/gui/scalableButtonOver2.png");

		buttonTexture.setLinearFiltering(false);
		renderer.getGuiRenderer().drawCorneredBoxTiled(xPosition, yPosition, width, getHeight(), 4 * scale(),
				buttonTexture, 32, scale());
		renderer.getFontRenderer().drawString(font, xPosition + 4 * scale(), yPosition, localizedText,
				fontScaler * scale(), new Vector4f(0, 0, 0, 1));// new Vector4f(76/255f, 76/255f, 76/255f, 1));
	}

	@Override
	public boolean handleClick(MouseButton mouseButton) {
		if (!mouseButton.equals("mouse.left"))
			return false;

		this.layer.getGameWindow().getClient().getSoundManager().playSoundEffect("./sounds/gui/gui_click2.ogg");

		if (this.action != null)
			this.action.run();

		return true;
	}

	public void setAction(@Nullable Runnable runnable) {
		this.action = runnable;
	}
}
