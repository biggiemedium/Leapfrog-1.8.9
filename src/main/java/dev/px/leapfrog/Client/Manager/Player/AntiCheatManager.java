package dev.px.leapfrog.Client.Manager.Player;

import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class AntiCheatManager {

    private int violations = 0;

    public AntiCheatManager() {

    }

    @EventHandler
    private Listener<PacketReceiveEvent> violationListener = new Listener<>(event -> {
        if(event.getPacket() instanceof S08PacketPlayerPosLook) {
            this.violations++;
        }
    });

    public void resetViolations() {
        this.violations = 0;
    }

}
