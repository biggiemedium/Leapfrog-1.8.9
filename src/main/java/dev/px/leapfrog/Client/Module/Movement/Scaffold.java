package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.API.Util.Math.BlockUtil;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

@Module.ModuleInterface(name = "Scaffold", type = Type.Movement, description = "Places blocks under you to prevent falling")
public class Scaffold extends Module {

    public Scaffold() {

    }

    private Setting<Boolean> tower = create(new Setting<>("Tower", true));
    private Setting<TowerMode> towerMode = create(new Setting<>("Tower Mode", TowerMode.Normal, v -> tower.getValue()));
    private Setting<Boolean> scaffold = create(new Setting<>("Scaffold", true));
    private Setting<ScaffoldMode> scaffoldMode = create(new Setting<>("Scaffold Mode", ScaffoldMode.Normal, v -> scaffold.getValue()));


    @EventHandler
    private Listener<PlayerMotionEvent> towerMotion = new Listener<>(event -> {
        switch (event.getStage()) {
            case Pre:

                if(tower.getValue()) {
                    switch (towerMode.getValue()) {
                        case Normal:
                            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                                if (mc.thePlayer.onGround) {
                                    mc.thePlayer.motionY = 0.42F;
                                }
                            }
                            break;

                        case Packet:
                            if(mc.gameSettings.keyBindJump.isKeyDown()) {
                                if(mc.thePlayer.onGround) {
                                   // mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer());
                                }
                            }
                            break;
                        case WatchDog:
                            // idk why but C08 will usually bypass watchdog movements
                            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                                if (mc.thePlayer.onGround) {
                                    mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                                    mc.thePlayer.motionY = 0.42f;
                                }
                            }
                            break;
                    }
                }

                break;
            case Post:

                break;
        }
    });

    @EventHandler
    private Listener<PacketSendEvent> towerPacketEvent = new Listener<>(event -> {
        if(event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            C08PacketPlayerBlockPlacement packet = (C08PacketPlayerBlockPlacement) event.getPacket();
            switch (towerMode.getValue()) {
                case Normal:
                    if(mc.thePlayer.motionY > -MoveUtil.GRAVITY_SPEED) {
                        if (packet.getPosition().equals(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.4, mc.thePlayer.posZ))) {
                            mc.thePlayer.motionY = -MoveUtil.GRAVITY_SPEED;
                        }
                    }
                    break;
            }
        }
    });

    private enum TowerMode {
        Normal,
        Packet,
        WatchDog
    }

    private enum ScaffoldMode {
        Normal
    }

}
