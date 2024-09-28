package dev.px.leapfrog.Client.Module.Ghost;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMoveInputEvent;
import dev.px.leapfrog.API.Event.Player.PlayerUpdateEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;

@Module.ModuleInterface(name = "Safe Bridge", type = Type.Ghost, description = "Prevents you from falling off edges")
public class SafeBridge extends Module {

    public SafeBridge() {

    }

    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.Sneak));
    private Setting<Double> slow = create(new Setting<>("Sneak Slow Down Multiplier", 3.0, 0.1, 5.0, v -> mode.getValue() == Mode.Sneak));
    private Setting<Boolean> blocksOnly = create(new Setting<>("Blocks Only", false));
    private Setting<Boolean> sneakOnly = create(new Setting<>("Sneak Only", false));

    private enum Mode {
        Sneak,
        Motion
    }
    @Override
    public void onDisable() {
        if(mc.thePlayer.isSneaking()) {
            mc.thePlayer.setSneaking(false);
        }
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            if(blocksOnly.getValue()) {
                if(mc.thePlayer.getCurrentEquippedItem() == null || !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)) {
                    return;
                }
            }
            if(sneakOnly.getValue()) {
                if(!mc.gameSettings.keyBindSneak.isPressed()) {
                    return;
                }
            }

            if(mode.getValue() == Mode.Sneak) {
                if (getBlockUnderPlayer(mc.thePlayer) instanceof BlockAir) {
                    if (mc.thePlayer.onGround) {
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
                    }
                } else {
                    if (mc.thePlayer.onGround) {
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
                    }
                }
            } else {
                if(getBlockUnderPlayer(mc.thePlayer) instanceof BlockAir) {
                    if(mc.thePlayer.onGround) {
                        mc.thePlayer.motionX = 0.0;
                        mc.thePlayer.motionZ = 0.0;
                        mc.thePlayer.motionY = 0.0;
                    }
                }
            }

        }
    });

    @EventHandler
    private Listener<PlayerMoveInputEvent> eventListener = new Listener<>(event -> {
        if (mode.getValue() == Mode.Sneak) {
            boolean shouldSneak = sneakOnly.getValue() && mc.gameSettings.keyBindSneak.isKeyDown();
            event.setSneak(shouldSneak || (!sneakOnly.getValue() && mc.gameSettings.keyBindSneak.isKeyDown()));

            if (shouldSneak && mc.thePlayer.onGround) {
                event.setMultiplier(slow.getValue());
            }
        } else if (mode.getValue() == Mode.Motion) {
            if (getBlockUnderPlayer(mc.thePlayer) instanceof BlockAir && mc.thePlayer.onGround) {
                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionZ = 0.0;
                mc.thePlayer.motionY = 0.0;
            }
        }
    });

    public Block getBlock(BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }

    public Block getBlockUnderPlayer(EntityPlayer player) {
        return getBlock(new BlockPos(player.posX , player.posY - 1.0d, player.posZ));
    }

    private boolean isOffsetBBEmpty(double x, double y, double z) {
        return mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, y, z)).isEmpty();
    }

    @Override
    public String arrayDetails() {
        return this.mode.getValue().name();
    }
}
