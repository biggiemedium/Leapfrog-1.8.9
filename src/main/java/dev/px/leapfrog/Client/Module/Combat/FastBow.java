package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Mouse;

@Module.ModuleInterface(name = "Fast Bow", type = Type.Combat, description = "Makes you shoot bow faster")
public class FastBow extends Module {

    public FastBow() {

    }

    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.Vanilla));
    private Setting<Integer> tickDelay = create(new Setting<>("Delay", 4, 0, 20, v -> mode.getValue() == Mode.NCP));

    private int ticks = 0;

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ((IMixinMinecraft) mc).setRightClickDelayTimer(4);
    }

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            if(mc.thePlayer.getCurrentEquippedItem() == null) {
                return;
            }
            if(mc.thePlayer.getHeldItem() != null) {
                if (mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
                    if (Mouse.isButtonDown(1)) {
                        switch (mode.getValue()) {
                            case Vanilla:
                                for(int i = 0; i < 20; i++) {
                                    ((IMixinMinecraft) mc).setRightClickDelayTimer(0);
                                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
                                }
                                mc.playerController.onStoppedUsingItem(mc.thePlayer);
                                break;
                            case NCP:
                                if(ticks >= tickDelay.getValue() && mc.thePlayer.getItemInUseCount() >= 3) {
                                    ticks = 0;
                                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.thePlayer.getHorizontalFacing()));
                                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getItemInUse()));
                                    mc.thePlayer.stopUsingItem();
                                }
                                ticks++;
                                break;
                        }

                    }
                }
            }

        }
    });

    private enum Mode {
        Vanilla,
        NCP
    }

}
