package dev.px.leapfrog.API.Util.Math;

import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BlockUtil {



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

}
