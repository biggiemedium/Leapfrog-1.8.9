package dev.px.leapfrog.API.Event.World;

import dev.px.leapfrog.API.Event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldBlockAABBEvent extends Event {

    private World world;
    private Block block;
    private net.minecraft.util.BlockPos blockPos;
    private AxisAlignedBB boundingBox;
    private AxisAlignedBB maskBoundingBox;

    public WorldBlockAABBEvent(World world, Block block, BlockPos blockPos, AxisAlignedBB boundingBox, AxisAlignedBB maskBoundingBox) {
        this.world = world;
        this.block = block;
        this.blockPos = blockPos;
        this.boundingBox = boundingBox;
        this.maskBoundingBox = maskBoundingBox;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }

    public AxisAlignedBB getMaskBoundingBox() {
        return maskBoundingBox;
    }

    public void setMaskBoundingBox(AxisAlignedBB maskBoundingBox) {
        this.maskBoundingBox = maskBoundingBox;
    }
}
