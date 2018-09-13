//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.gui.elements;

import io.xol.chunkstories.api.gui.Font;
import io.xol.chunkstories.api.gui.Layer;

public class ThinButton extends BaseButton {

    public ThinButton(Layer layer, Font font, int x, int y, String text) {
        super(layer, font, x, y, text);
        this.setHeight(18);
    }

    public ThinButton(Layer layer, int x, int y, String text) {
        super(layer, x, y, text);
        this.setHeight(18);
    }

}
