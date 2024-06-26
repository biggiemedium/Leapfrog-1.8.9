package dev.px.leapfrog.Client.Manager.Player;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Util.Math.MathUtil;
import dev.px.leapfrog.ASM.Listeners.IMixinRenderManager;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class PositionManager implements Listenable {

    private double x, y, z;
    private int offGroundTicks, onGroundTicks;
    private Minecraft mc = Minecraft.getMinecraft();

    public PositionManager() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.offGroundTicks = 0;
        this.onGroundTicks = 0;
        LeapFrog.EVENT_BUS.subscribe(this);
    }

    @EventHandler
    private Listener<PlayerMotionEvent> event = new Listener<>(event -> {
        //this.updatePosition(event.getX(), event.getY(), event.getZ());
        if(event.getStage() == Event.Stage.Pre) {
            if(mc.thePlayer != null) {
                if (mc.thePlayer.onGround) {
                    offGroundTicks = 0;
                    onGroundTicks++;
                } else {
                    onGroundTicks = 0;
                    offGroundTicks++;
                }

                this.updatePosition(event.getX(), event.getY(), event.getZ());
            }
        }
    });

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getOffGroundTicks() {
        return offGroundTicks;
    }

    public void setOffGroundTicks(int offGroundTicks) {
        this.offGroundTicks = offGroundTicks;
    }

    public int getOnGroundTicks() {
        return onGroundTicks;
    }

    public void setOnGroundTicks(int onGroundTicks) {
        this.onGroundTicks = onGroundTicks;
    }

    public void updatePosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


}
