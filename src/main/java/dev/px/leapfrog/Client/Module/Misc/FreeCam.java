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

    private Setting<Float> speed = create(new Setting<>("Speed", 1F, 1F, 10F));

    private double oldX, oldY, oldZ;
    private float oldYaw, oldPitch;
    private EntityOtherPlayerMP player;

    @Override
    public void onEnable() {
        if(mc.thePlayer == null || mc.theWorld == null) return;
        mc.thePlayer.noClip = true;
        this.oldX = mc.thePlayer.posX;
        this.oldY = mc.thePlayer.posY;
        this.oldZ = mc.thePlayer.posZ;
        this.oldYaw = mc.thePlayer.rotationYaw;
        this.oldPitch = mc.thePlayer.rotationPitch;
        (player = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile())).clonePlayer(mc.thePlayer, true);
        player.setPosition(this.mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        player.rotationYawHead = this.mc.thePlayer.rotationYaw;
        player.rotationPitch = this.mc.thePlayer.rotationPitch;
        player.setSneaking(this.mc.thePlayer.isSneaking());
        mc.theWorld.addEntityToWorld(-1337, player);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.thePlayer.noClip = false;
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, this.oldYaw, this.oldPitch);
        mc.theWorld.removeEntity(player);
        super.onDisable();
    }

    @EventHandler
    private Listener<PacketSendEvent> sendEventListener = new Listener<>(event -> {
        if(event.getPacket() instanceof C0APacketAnimation
                || event.getPacket() instanceof C03PacketPlayer
                || event.getPacket() instanceof C02PacketUseEntity
                || event.getPacket() instanceof C0BPacketEntityAction
                || event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
                event.cancel();
        }
    });

    @EventHandler
    private Listener<PlayerMoveEvent> motionEventListener = new Listener<>(event -> {
        mc.thePlayer.noClip = true;
        event.setX(0);
        event.setY(0);
        event.setZ(0);
        mc.thePlayer.setVelocity(0, 0, 0);

        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.thePlayer.motionY += speed.getValue();
        }
        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.thePlayer.motionY -= speed.getValue();
        }

        //if (MoveUtil.isMoving()) {
        //    MoveUtil.setMoveSpeed(event, speed.getValue());
        //}
    });

    @EventHandler
    private Listener<WorldBlockAABBEvent> aabbEventListener = new Listener<>(event -> {
        event.setBoundingBox(null);
        event.cancel();
    });
}
