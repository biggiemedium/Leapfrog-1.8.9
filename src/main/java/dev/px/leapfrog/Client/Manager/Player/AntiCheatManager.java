package dev.px.leapfrog.Client.Manager.Player;

import dev.px.leapfrog.API.Event.Network.PacketReceiveEvent;
import dev.px.leapfrog.API.Event.Player.PlayerTeleportEvent;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class AntiCheatManager {

    private int violations = 0;
    private boolean teleport = false;

    public AntiCheatManager() {

    }

    @EventHandler
    private Listener<PacketReceiveEvent> violationListener = new Listener<>(event -> {
        if(event.getPacket() instanceof S08PacketPlayerPosLook) {
            if(teleport) {
                this.teleport = false;
            } else {
                this.violations++;
            }

        }
    });

    @EventHandler
    private Listener<PlayerTeleportEvent> teleportEventListener = new Listener<>(event -> {
        this.teleport = true;
    });

    public void resetViolations() {
        this.violations = 0;
    }

}
