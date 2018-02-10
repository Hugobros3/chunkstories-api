package io.xol.chunkstories.api.item;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.client.ClientContent;
import io.xol.chunkstories.api.content.Content;
import io.xol.chunkstories.api.entity.Controller;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.interfaces.EntityControllable;
import io.xol.chunkstories.api.entity.interfaces.EntityCreative;
import io.xol.chunkstories.api.entity.interfaces.EntityWorldModifier;
import io.xol.chunkstories.api.events.player.voxel.PlayerVoxelModificationEvent;
import io.xol.chunkstories.api.events.voxel.WorldModificationCause;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.input.Input;
import io.xol.chunkstories.api.item.inventory.ItemPile;
import io.xol.chunkstories.api.item.renderer.ItemRenderer;
import io.xol.chunkstories.api.item.renderer.VoxelItemRenderer;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.voxel.VoxelFormat;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.WorldMaster;
import io.xol.chunkstories.api.world.cell.CellData;
import io.xol.chunkstories.api.world.cell.FutureCell;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/**
 * An item that contains voxels
 */
public class ItemVoxel extends Item implements WorldModificationCause
{
	private final Content.Voxels store;
	
	public Voxel voxel = null;
	public int voxelMeta = 0;

	public ItemVoxel(ItemDefinition type)
	{
		super(type);
		store = type.store().parent().voxels();
	}
	
	public ItemRenderer getCustomItemRenderer(ItemRenderer fallbackRenderer)
	{
		return new VoxelItemRenderer((ClientContent)this.getType().store().parent(), fallbackRenderer);
	}

	/*@Override
	public void onCreate(ItemPile pile, String[] info)
	{
		//ItemDataVoxel idv = (ItemDataVoxel) pile.data;
		if (info != null && info.length > 0)
			voxel = Voxels.get(Integer.parseInt(info[0]));
		if (info != null && info.length > 1)
			voxelMeta = Integer.parseInt(info[1]) % 16;
	}
*/
	@Override
	public String getTextureName(ItemPile pile)
	{
		//ItemDataVoxel idv = (ItemDataVoxel) pile.data;
		if (voxel != null)
			return "./items/icons/" + voxel.getName() + ".png";
		return "./items/icons/notex.png";
	}

	public Voxel getVoxel()
	{
		return voxel;
		//((ItemDataVoxel) pile.getData()).voxel;
	}

	public int getVoxelMeta()
	{
		return voxelMeta;
		//((ItemDataVoxel) pile.getData()).voxelMeta;
	}

	@Override
	public boolean onControllerInput(Entity entity, ItemPile pile, Input input, Controller controller)
	{
		try {
			if (entity.getWorld() instanceof WorldMaster && input.getName().equals("mouse.right"))
			{
				//Require entities to be of the right kind
				if(!(entity instanceof EntityWorldModifier))
					return true;
				
				if(!(entity instanceof EntityControllable))
					return true;
				
				EntityWorldModifier modifierEntity = (EntityWorldModifier) entity;
				EntityControllable playerEntity = (EntityControllable) entity;
				
				boolean isEntityCreativeMode = (entity instanceof EntityCreative) && (((EntityCreative) entity).isCreativeMode());
	
				Location blockLocation = null;
				blockLocation = playerEntity.getBlockLookingAt(false);

				//int dataToWrite = VoxelFormat.format(entity.getWorld().getContentTranslator().getIdForVoxel(voxel), voxelMeta, 0, 0);
				
				if (blockLocation != null)
				{
					FutureCell fvc = new FutureCell(entity.getWorld().peekSafely(blockLocation));
					
					//Opaque blocks overwrite the original light with zero.
					if (voxel.getDefinition().isOpaque())
					{
						fvc.setBlocklight(0);
						fvc.setSunlight(0);
					}
					
					//Glowy stuff should glow
					if(voxel.getDefinition().getEmittingLightLevel() > 0)
						fvc.setBlocklight(voxel.getLightLevel(fvc));
						//dataToWrite = VoxelFormat.changeBlocklight(dataToWrite, voxel.getLightLevel(dataToWrite));
						
					// Player events mod
					if(controller instanceof Player) {
						Player player = (Player)controller;
						CellData ctx = entity.getWorld().peek(blockLocation);
						PlayerVoxelModificationEvent event = new PlayerVoxelModificationEvent(ctx, fvc, isEntityCreativeMode ? EntityCreative.CREATIVE_MODE : this, player);
						
						//Anyone has objections ?
						entity.getWorld().getGameContext().getPluginManager().fireEvent(event);
						
						if(event.isCancelled())
							return true;
					}
					
					entity.getWorld().poke(fvc, modifierEntity);
					
					// Decrease stack size
					if(!isEntityCreativeMode) {
						int currentAmount = pile.getAmount();
						currentAmount--;
						pile.setAmount(currentAmount);
					}
				}
				else
				{
					//No space found :/
					return true;
				}
			}
			
		}
		catch(WorldException e) {
			
		}
		
		return false;

	}

	@Override
	public void load(DataInputStream stream) throws IOException
	{
		voxel = store.getVoxelByName(stream.readUTF()); //store.getVoxelById(stream.readInt());
		voxelMeta = stream.readByte();
	}

	@Override
	public void save(DataOutputStream stream) throws IOException
	{
		/*if(((ItemDataVoxel) itemPile.data).voxel != null)
			stream.writeInt(((ItemDataVoxel) itemPile.data).voxel.getId());
		else
			stream.writeInt(1);
		stream.writeByte((byte) ((ItemDataVoxel) itemPile.data).voxelMeta);*/
		
		//System.out.println("saved my bits");
		
		/*if(voxel != null)
			stream.writeInt(voxel.getId());
		else
			stream.writeInt(1);*/
		if(voxel == null) {
			stream.writeUTF("air");
		} else {
			stream.writeUTF(voxel.getDefinition().getName());
		}
		stream.writeByte(voxelMeta);
	}

	@Override
	/** Two ItemVoxel can merge if they represent the same voxel & they share the same 8 bits of metadata */
	public boolean canMergeWith(Item item)
	{
		if(item instanceof ItemVoxel)
		{
			ItemVoxel itemVoxel = (ItemVoxel)item;
			return super.canMergeWith(itemVoxel) && itemVoxel.getVoxel().sameKind(getVoxel()) && itemVoxel.getVoxelMeta() == this.getVoxelMeta();
		}
		return false;
	}
	
	@Override
	public String getName()
	{
		if(voxel != null)
		{
			return voxel.getName();
		}
		return "novoxel!";
	}
}
