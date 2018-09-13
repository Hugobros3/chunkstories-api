//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.gui;

import io.xol.chunkstories.api.input.Input;
import org.jetbrains.annotations.NotNull;

/** Elements that can be focused, either by tab-ing on them, or by clicking */
public abstract class FocusableGuiElement extends GuiElement {

	protected FocusableGuiElement(@NotNull Layer layer, int width, int height) {
		super(layer, width, height);
	}

	public boolean isFocused() {
		return this.equals(getLayer().getFocusedElement());
	}

	/** When focused an element receives input from the keyboard */
	public boolean handleInput(Input input) {
		return false;
	}
}
