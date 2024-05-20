package dev.px.leapfrog.Client.Manager.Player;

import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.Minecraft;

public class PositionManager {

    private double x, y, z;
    private Minecraft mc = Minecraft.getMinecraft();

    public PositionManager() {
        this.x = mc.thePlayer.posX;
        this.y = mc.thePlayer.posY;
        this.z = mc.thePlayer.posZ;
    }

    @EventHandler
    private Listener<PlayerMotionEvent> event = new Listener<>(event -> {
        this.updatePosition(event.getX(), event.getY(), event.getZ());
    });

    public void updatePosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
