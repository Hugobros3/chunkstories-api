package io.xol.chunkstories.api.rendering.item;

import org.joml.Matrix4f;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.item.inventory.ItemPile;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.world.World;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class NullItemRenderer extends ItemRenderer {

	public NullItemRenderer(ItemRenderer fallbackRenderer) {
		super(fallbackRenderer);
	}

	@Override
	public void renderItemInInventory(RenderingInterface renderingInterface, ItemPile pile, float f, float g,
			int scaling) {
		//Do absolutely nothing
	}

	@Override
	public void renderItemInWorld(RenderingInterface renderingInterface, ItemPile pile, World world, Location location,
			Matrix4f transformation) {
		//Do absolutely nothing
	}

}
