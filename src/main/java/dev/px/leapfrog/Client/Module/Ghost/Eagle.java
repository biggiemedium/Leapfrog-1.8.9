package dev.px.leapfrog.Client.Module.Ghost;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
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

@Module.ModuleInterface(name = "Eagle", type = Type.Ghost, description = "Prevents you from falling off edges")
public class Eagle extends Module {

    public Eagle() {

    }

    private Setting<Boolean> blocksOnly = create(new Setting<>("Blocks Only", false));
    private Setting<Boolean> sneakOnly = create(new Setting<>("Sneak Only", false));

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

            if(getBlockUnderPlayer(mc.thePlayer) instanceof BlockAir) {
                if(mc.thePlayer.onGround) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
                }
            } else {
                if(mc.thePlayer.onGround) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
                }
            }
        }
    });

    public Block getBlock(BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }

    public Block getBlockUnderPlayer(EntityPlayer player) {
        return getBlock(new BlockPos(player.posX , player.posY - 1.0d, player.posZ));
    }
}
