package dev.px.leapfrog.Client.Module.Ghost;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMoveEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

@Module.ModuleInterface(name = "Ghost Bridge", type = Type.Ghost, description = "Fake Legit bridge")
public class GhostBridge extends Module { // god bridging is at yaw 45

    public GhostBridge() {

    }

    private boolean sneak;

    @EventHandler
    private Listener<PlayerMotionEvent> playerMotionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            mc.thePlayer.rotationYaw = adjustYawToNearest45(mc.thePlayer.rotationYaw);
        }

        BlockPos blockPos = mc.objectMouseOver.getBlockPos();
        if (blockPos != null && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
            EnumFacing sideHit = mc.objectMouseOver.sideHit;

            placeBlock(blockPos.offset(sideHit));
        }
    });

    @EventHandler
    private Listener<PlayerMoveEvent> moveEventListener = new Listener<>(event -> {
        double x = event.getX();
        double y = event.getY();
        double z = event.getZ();

        if (mc.thePlayer.onGround && !mc.thePlayer.noClip) {
            double increment = 0.05D;
            x = adjustMovement(x, increment);
            z = adjustMovement(z, increment);
        }

        event.setX(x);
        event.setY(y);
        event.setZ(z);
    });

    private double adjustMovement(double value, double increment) {
        while (value != 0.0D && isOffsetBBEmpty(value, -100, 0.0D)) {
            if (Math.abs(value) < increment) {
                value = 0.0D;
            } else if (value > 0.0D) {
                value -= increment;
            } else {
                value += increment;
            }
        }
        return value;
    }

    private boolean isOffsetBBEmpty(double x, double y, double z) {
        return mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, y, z)).isEmpty();
    }

    private void placeBlock(BlockPos pos) {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, EnumFacing.UP, new Vec3(0.5, 0.5, 0.5));
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
    }

    private float adjustYawToNearest45(float yaw) {
        return Math.round(yaw / 45.0f) * 45.0f;
    }
}
