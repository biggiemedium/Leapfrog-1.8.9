package dev.px.leapfrog.Client.Module.Misc;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Math.RotationUtil;
import dev.px.leapfrog.API.Util.Math.Vectors.Vec3d;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;

@Module.ModuleInterface(name = "Anti Fireball", type = Type.Misc, description = "Deflects Fireballs")
public class AntiFireball extends Module {

    public AntiFireball() {

    }

    private Setting<Integer> range = create(new Setting<>("Range", 4, 0, 6));
    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.Ghost));
    private Setting<Boolean> packetSwing = create(new Setting<>("Packet Swing", false));

    private Vec3d throwPos = new Vec3d(0, 0, 0);

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if (event.getStage() == Event.Stage.Pre) {
            for(Entity e : mc.theWorld.loadedEntityList) {
                if(e == null) continue;
                if(!(e instanceof EntityFireball)) continue;
                if(mc.thePlayer.getDistanceToEntity(e) > range.getValue()) continue;
                if(mc.thePlayer.isDead) continue;
                EntityFireball fb = (EntityFireball) e;
                if(fb.shootingEntity == mc.thePlayer) continue;
                if(fb.shootingEntity != null) {
                    if (LeapFrog.socialManager.isFriend(fb.shootingEntity.getName())) continue;
                }
                double[] rotations = RotationUtil.faceTargetNoDelay(e.posX, e.posY, e.posZ, mc.thePlayer);
                float targetYaw = 0;
                float targetPitch = 0;

                switch (mode.getValue()) {
                    case Blatant:
                        targetYaw = (float) rotations[0];
                        targetPitch = (float) rotations[1];
                        break;
                    case Ghost:
                        float[] ghostRotations = RotationUtil.faceTarget(e, 20f, 20f, true);
                        targetYaw = (float) ghostRotations[0];
                        targetPitch = (float) ghostRotations[1];
                        break;
                }

                mc.thePlayer.rotationYaw = targetYaw;
                mc.thePlayer.rotationPitch = targetPitch;

                switch (mode.getValue()) {
                    case Blatant:
                        mc.playerController.attackEntity(mc.thePlayer, e);
                        break;
                    case Ghost:
                        if(packetSwing.getValue()) {
                            mc.getNetHandler().getNetworkManager().sendPacket(new C0APacketAnimation()); // In 1.8 C0A packet comes before
                            mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(mc.thePlayer, C02PacketUseEntity.Action.ATTACK));
                        } else {
                            mc.playerController.attackEntity(mc.thePlayer, e);
                        }
                        break;
                }

            }
        }
    });

    private enum Mode {
        Blatant,
        Ghost
    }
}
