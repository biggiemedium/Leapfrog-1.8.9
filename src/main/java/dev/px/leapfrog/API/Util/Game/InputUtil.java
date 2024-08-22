package dev.px.leapfrog.API.Util.Game;

import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class InputUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static void clickMouse() {
        if (((IMixinMinecraft) mc).getLeftClickCounter() <= 0) {
            mc.thePlayer.swingItem();
            if (mc.objectMouseOver == null) {
                //logger.error("Null returned as 'hitResult', this shouldn't happen!");
                if (mc.playerController.isNotCreative()) {
                    ((IMixinMinecraft) mc).setLeftClickCounter(10);
                }
            } else {
                switch(mc.objectMouseOver.typeOfHit) {
                    case ENTITY:
                        mc.playerController.attackEntity(mc.thePlayer, mc.objectMouseOver.entityHit);
                        break;
                    case BLOCK:
                        BlockPos blockpos = mc.objectMouseOver.getBlockPos();
                        if (mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                            mc.playerController.clickBlock(blockpos, mc.objectMouseOver.sideHit);
                            break;
                        }
                    case MISS:
                    default:
                        if (mc.playerController.isNotCreative()) {
                            ((IMixinMinecraft) mc).setLeftClickCounter(10);
                        }
                }
            }
        }

    }

}
