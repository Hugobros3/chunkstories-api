//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.gui.elements;

import xyz.chunkstories.api.gui.ClickableGuiElement;
import xyz.chunkstories.api.gui.FocusableGuiElement;
import xyz.chunkstories.api.gui.GuiDrawer;
import xyz.chunkstories.api.gui.Layer;
import xyz.chunkstories.api.input.Mouse;
import xyz.chunkstories.api.input.Mouse.MouseButton;
import org.joml.Vector4f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ScrollableContainer extends FocusableGuiElement implements ClickableGuiElement {

	protected ScrollableContainer(Layer layer) {
		super(layer, 480, 1024);
	}

	public List<ContainerElement> elements = new ArrayList<ContainerElement>();
	protected int scroll = 0;

	public boolean isMouseOver(int mx, int my) {
		return mx >= getPositionX() && mx <= getPositionX() + getWidth() && my >= getPositionY() && my <= getPositionY() + getHeight();
	}

	public void render(GuiDrawer renderer) {
		int startY = this.getPositionY() + getHeight();
		int i = scroll;

		while (true) {
			if (i >= elements.size())
				break;
			ContainerElement element = elements.get(i);
			startY -= element.height;
			if (startY < this.getPositionY())
				break;
			i++;

			element.setPosition(this.getPositionX(), startY);
			element.render(renderer);
			startY -= 4;
		}

		// return r;
	}

	public void scroll(boolean sign) {
		if (sign) {
			// Scroll up
			scroll--;
			if (scroll < 0)
				scroll = 0;
		} else {
			// Scroll up
			scroll++;
			if (scroll >= elements.size())
				scroll = elements.size() - 1;
		}
	}

	public abstract class ContainerElement {

		public ContainerElement(@Nullable String name, @Nullable String descriptionLines) {
			this.name = name;
			this.descriptionLines = descriptionLines;
		}

		@Nullable
		public String name, topRightString = "";
		@Nullable
		public String descriptionLines;

		public String iconTextureLocation = "textures/gui/info.png";
		protected int positionX, positionY;
		protected int width = 480, height = 72;

		public void setPosition(int positionX, int positionY) {
			this.positionX = positionX;
			this.positionY = positionY;
		}

		public void render(GuiDrawer drawer) {
			// Setup textures
			String bgTexture = isMouseOver(drawer.getGui().getMouse()) ? "textures/gui/genericOver.png" : "textures/gui/generic.png";

			// Render graphical base
			drawer.drawBox(positionX, positionY, width, height, 0, 1, 1, 0, bgTexture, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
			// Render icon
			drawer.drawBox(positionX + 4, positionY + 4, 64, 64, 0, 1, 1, 0, iconTextureLocation, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
			// Text !
			if (name != null)
				drawer.drawString(drawer.getFonts().getFont("LiberationSans-Regular", 12), positionX + 70, positionY + 54, name, -1,
						new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));

			if (topRightString != null) {
				int dekal = width - drawer.getFonts().getFont("LiberationSans-Regular", 12).getWidth(topRightString) - 4;
				drawer.drawString(drawer.getFonts().getFont("LiberationSans-Regular", 12), positionX + dekal, positionY + 54, topRightString, -1,
						new Vector4f(0.25f, 0.25f, 0.25f, 1.0f));
			}

			if (descriptionLines != null)
				drawer.drawString(drawer.getFonts().getFont("LiberationSans-Regular", 12), positionX + 70, positionY + 38, descriptionLines, -1,
						new Vector4f(0.25f, 0.25f, 0.25f, 1.0f));

		}

		public abstract boolean handleClick(MouseButton mouseButton);

		public boolean isMouseOver(Mouse mouse) {
			int s = 1;
			double mx = mouse.getCursorX();
			double my = mouse.getCursorY();
			return mx >= positionX && mx <= positionX + width * s && my >= positionY && my <= positionY + height * s;
		}
	}

	@Override
	public boolean handleClick(MouseButton mouseButton) {

		float startY = this.getPositionY() + getHeight();
		int i = scroll;

		while (true) {
			if (i >= elements.size())
				break;
			ContainerElement element = elements.get(i);
			if (startY - element.height < this.getPositionY())
				break;
			startY -= element.height;
			startY -= 4;
			i++;

			if (element.isMouseOver(mouseButton.getMouse())) {
				element.handleClick(mouseButton);
				return true;
			}
		}

		return false;
	}
}
