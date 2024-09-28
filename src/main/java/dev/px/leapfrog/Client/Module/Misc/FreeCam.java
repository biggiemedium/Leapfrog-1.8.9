package dev.px.leapfrog.Client.Module.Misc;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Network.PacketSendEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMoveEvent;
import dev.px.leapfrog.API.Event.Player.PlayerStrafeEvent;
import dev.px.leapfrog.API.Event.World.WorldBlockAABBEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.*;

/**
 * @author Flux b39
 * Why reinvent the wheel? just im just going to use flux clients freecam
 */
@Module.ModuleInterface(name = "Freecam", type = Type.Misc, description = "Puts you in spectator (Client side)")
public class FreeCam extends Module {

    public FreeCam() {

    }

    @Override
    public void onEnable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.capabilities.allowFlying = true;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.capabilities.allowFlying = false;
            mc.thePlayer.capabilities.isFlying = false;
        }
        super.onDisable();
    }

    @EventHandler
    private Listener<PacketSendEvent> sendEventListener = new Listener<>(event -> {
        if (event.getPacket() instanceof C03PacketPlayer) {
            event.cancel();
        }
    });

    @EventHandler
    private Listener<WorldBlockAABBEvent> aabbEventListener = new Listener<>(event -> {
        if(mc.thePlayer != null) {
            event.cancel();
        }
    });
}
