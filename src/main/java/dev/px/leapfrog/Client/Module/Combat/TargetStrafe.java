package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Event.Player.PlayerMoveEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.EntityUtil;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
import dev.px.leapfrog.API.Util.Math.MoveUtil;
import dev.px.leapfrog.API.Util.Math.RotationUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vector3d;

@Module.ModuleInterface(name = "Target Strafe", type = Type.Combat, description = "Circle around target very fast")
public class TargetStrafe extends Module {

    public TargetStrafe() {

    }

    private Setting<Float> range = create(new Setting<>("Range", 2.0f, 0.5f, 4.5f));

    private boolean direction = true;

    @EventHandler
    private Listener<PlayerMoveEvent> moveEventListener = new Listener<>(event -> {
        if(LeapFrog.moduleManager.getModuleByClass(KillAura.class).getCurrentTarget() == null) {
            return;
        }
        this.strafe(event, MoveUtil.getBaseMoveSpeed(), LeapFrog.moduleManager.getModuleByClass(KillAura.class).getCurrentTarget(), range.getValue());
    });

    private void strafe(PlayerMoveEvent event, double speed, Entity entity, double range) {
        if(entity == null) {
            return;
        }
        if(mc.thePlayer.getDistance(entity.posX, entity.posY, entity.posZ) >= range) {
            return;
        }

        if (!PlayerUtil.isBlockUnderPlayer(1))
            direction = !direction;

        if (mc.thePlayer.isCollidedHorizontally)
            direction = !direction;

        float strafe = direction ? 1 : -1;
        float diff = (float)(speed / (range * Math.PI * 2)) * 360 * strafe;
        float[] rotation = RotationUtil.getNeededRotations(entity.posX, entity.posY, entity.posZ, mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

        rotation[0] += diff;
        float dir = rotation[0] * (float)(Math.PI / 180F);

        double x = entity.posX - Math.sin(dir) * range;
        double z = entity.posZ + Math.cos(dir) * range;

        float yaw = RotationUtil.getNeededRotations(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, x, entity.posY, z)[0] * (float)(Math.PI / 180F);

        mc.thePlayer.motionX = -MathHelper.sin(yaw) * speed;
        mc.thePlayer.motionZ = MathHelper.cos(yaw) * speed;
        if (event != null) {
            event.setX(mc.thePlayer.motionX);
            event.setZ(mc.thePlayer.motionZ);
        }
    }

}
