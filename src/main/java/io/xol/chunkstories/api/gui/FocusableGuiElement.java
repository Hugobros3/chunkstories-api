//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.gui;

import io.xol.chunkstories.api.input.Input;

/** Elements that can be focused, either by tab-ing on them, or by clicking */
public abstract class FocusableGuiElement extends GuiElement {

	protected FocusableGuiElement(Layer layer) {
		super(layer);
	}

	public boolean isFocused() {
		return this.equals(layer.getFocusedElement());
	}

	/** When focused an element receives input from the keyboard */
	public boolean handleInput(Input input) {
		return false;
	}
}
