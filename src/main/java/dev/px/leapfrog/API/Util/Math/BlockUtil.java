package dev.px.leapfrog.API.Util.Math;

import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BlockUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isValidBlock(Block block, boolean placing) {
        if (block instanceof BlockCarpet
                || block instanceof BlockSnow
                || block instanceof BlockContainer
                || block instanceof BlockBasePressurePlate
                || block.getMaterial().isLiquid()) {
            return false;
        }
        if (placing && (block instanceof BlockSlab
                || block instanceof BlockStairs
                || block instanceof BlockLadder
                || block instanceof BlockStainedGlassPane
                || block instanceof BlockWall
                || block instanceof BlockWeb
                || block instanceof BlockCactus
                || block instanceof BlockFalling
                || block == Blocks.glass_pane
                || block == Blocks.iron_bars)) {
            return false;
        }
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block.isFullBlock());
    }

    public static ArrayList<BlockPos> getBlockInSphere(Block block, int range) {
        ArrayList<BlockPos> pos = new ArrayList<>();

        for(int x = -range; x < range; x++) {
            for(int y = -range; y < range; y++) {
                for(int z = -range; z < range; z++) {
                    if(mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == block) {
                        pos.add(new BlockPos(x, y, z));
                    }
                }
            }
        }

        return pos;
    }
}
