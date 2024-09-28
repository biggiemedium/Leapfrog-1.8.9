package dev.px.leapfrog.API.Event.Render;

import dev.px.leapfrog.API.Event.Event;
import net.minecraft.tileentity.TileEntityChest;

/**
 * @author Tenacity Client
 */
public class RenderChestEvent extends Event {

    private TileEntityChest chest;
    private Runnable drawChest;

    public RenderChestEvent(TileEntityChest chest, Runnable drawChest) {
        this.chest = chest;
        this.drawChest = drawChest;
    }

    public TileEntityChest getChest() {
        return chest;
    }

    public void setChest(TileEntityChest chest) {
        this.chest = chest;
    }

    public Runnable getDrawChest() {
        return drawChest;
    }

    public void setDrawChest(Runnable drawChest) {
        this.drawChest = drawChest;
    }
}
