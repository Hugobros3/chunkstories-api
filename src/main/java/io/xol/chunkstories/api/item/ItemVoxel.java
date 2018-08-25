//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.item;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.content.Content;
import io.xol.chunkstories.api.entity.Controller;
import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.traits.TraitVoxelSelection;
import io.xol.chunkstories.api.entity.traits.serializable.TraitCreativeMode;
import io.xol.chunkstories.api.events.player.voxel.PlayerVoxelModificationEvent;
import io.xol.chunkstories.api.events.voxel.WorldModificationCause;
import io.xol.chunkstories.api.exceptions.world.WorldException;
import io.xol.chunkstories.api.input.Input;
import io.xol.chunkstories.api.item.inventory.ItemPile;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.sound.SoundSource.Mode;
import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.world.WorldMaster;
import io.xol.chunkstories.api.world.cell.CellData;
import io.xol.chunkstories.api.world.cell.FutureCell;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * An item that contains voxels
 */
public class ItemVoxel extends Item implements WorldModificationCause {
    private final Content.Voxels store;

    @Nullable
    public Voxel voxel = null;
    public int voxelMeta = 0;

    public ItemVoxel(ItemDeclaration type) {
        super(type);
        store = type.store().parent().voxels();
    }

    @Override
    public String getTextureName(ItemPile pile) {
        // ItemDataVoxel idv = (ItemDataVoxel) pile.data;
        if (voxel != null)
            return "./items/icons/" + voxel.getName() + ".png";
        return "./items/icons/notex.png";
    }

    @Nullable
    public Voxel getVoxel() {
        return voxel;
        // ((ItemDataVoxel) pile.getData()).voxel;
    }

    public int getVoxelMeta() {
        return voxelMeta;
        // ((ItemDataVoxel) pile.getData()).voxelMeta;
    }

    @Override
    public boolean onControllerInput(Entity entity, ItemPile pile, Input input, Controller controller) {
        try {
            if (entity.getWorld() instanceof WorldMaster && input.getName().equals("mouse.right")) {
                // Require entities to be of the right kind
                if (!(entity instanceof WorldModificationCause)) {
                    return true;
                }

                WorldModificationCause modifierEntity = (WorldModificationCause) entity;

                boolean isEntityCreativeMode = entity.getTraits().tryWithBoolean(TraitCreativeMode.class, ecm -> ecm.get());

                Location blockLocation = entity.getTraits().tryWith(TraitVoxelSelection.class, tvs -> tvs.getBlockLookingAt(false, true));

                if (blockLocation != null) {
                    FutureCell fvc = new FutureCell(entity.getWorld().peekSafely(blockLocation));
                    fvc.setVoxel(voxel);

                    // Opaque blocks overwrite the original light with zero.
                    if (voxel.getOpaque()) {
                        fvc.setBlocklight(0);
                        fvc.setSunlight(0);
                    }

                    // Glowy stuff should glow
                    // if(voxel.getDeclaration().getEmittedLightLevel() > 0)
                    fvc.setBlocklight(voxel.getEmittedLightLevel(fvc));

                    // Player events mod
                    if (controller instanceof Player) {
                        Player player = (Player) controller;
                        CellData ctx = entity.getWorld().peek(blockLocation);
                        PlayerVoxelModificationEvent event = new PlayerVoxelModificationEvent(ctx, fvc,
                                isEntityCreativeMode ? TraitCreativeMode.CREATIVE_MODE : this, player);

                        // Anyone has objections ?
                        entity.getWorld().getGameContext().getPluginManager().fireEvent(event);

                        if (event.isCancelled())
                            return true;

                        entity.getWorld().getSoundManager().playSoundEffect("sounds/gameplay/voxel_place.ogg", Mode.NORMAL, fvc.getLocation(), 1.0f, 1.0f);
                    }

                    entity.getWorld().poke(fvc, modifierEntity);

                    // Decrease stack size
                    if (!isEntityCreativeMode) {
                        int currentAmount = pile.getAmount();
                        currentAmount--;
                        pile.setAmount(currentAmount);
                    }
                } else {
                    // No space found :/
                    return true;
                }
            }

        } catch (WorldException e) {

        }

        return false;

    }

    @Override
    public void load(DataInputStream stream) throws IOException {
        voxel = store.getVoxel(stream.readUTF()); // store.getVoxelById(stream.readInt());
        voxelMeta = stream.readByte();
    }

    @Override
    public void save(DataOutputStream stream) throws IOException {
        if (voxel == null) {
            stream.writeUTF("air");
        } else {
            stream.writeUTF(voxel.getName());
        }
        stream.writeByte(voxelMeta);
    }

    @Override
    /** Two ItemVoxel can merge if they represent the same voxel & they share the
     * same 8 bits of metadata */
    public boolean canStackWith(Item item) {
        if (item instanceof ItemVoxel) {
            ItemVoxel itemVoxel = (ItemVoxel) item;
            return super.canStackWith(itemVoxel) && itemVoxel.getVoxel().sameKind(getVoxel()) && itemVoxel.getVoxelMeta() == this.getVoxelMeta();
        }
        return false;
    }

    @Override
    public String getName() {
        if (voxel != null) {
            return voxel.getName();
        }
        return "novoxel!";
    }
}
