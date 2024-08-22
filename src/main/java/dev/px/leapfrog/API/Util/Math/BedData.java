package dev.px.leapfrog.API.Util.Math;

import net.minecraft.util.BlockPos;

public class BedData {

    private BlockPos pos;

    public BedData(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }
}
