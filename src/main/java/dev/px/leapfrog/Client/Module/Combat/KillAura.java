package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.EntityUtil;
import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;

import javax.activity.ActivityRequiredException;

@Module.ModuleInterface(name = "Kill aura", type = Type.Combat, description = "Auto targets enemies with sword")
public class KillAura extends Module {

    public KillAura() {

    }

    private Entity target = null;

    private Setting<Boolean> players = create(new Setting<>("Players", true));
    private Setting<Boolean> invisibles = create(new Setting<>("Invisible", true, v -> players.getValue()));
    private Setting<Boolean> hostiles = create(new Setting<>("Hostiles", false));
    private Setting<Boolean> passive = create(new Setting<>("Passives", false));

    // CPS
    private Setting<Integer> minCPS = create(new Setting<>("Min CPS", 6, 0, 20));
    private Setting<Integer> maxCPS = create(new Setting<>("Max CPS", 10, 0, 20));

    // rotate
    private Setting<Boolean> rayTrace = create(new Setting<>("Ray Trace Only", false));
    private Setting<RotationMode> rotateMode = create(new Setting<>("Rotate Mode", RotationMode.Smooth));

    // Render

    // block
    // click mode
    // raycast

    private TimerUtil attackTimer = new TimerUtil();

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.target = null;
        super.onDisable();
    }

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        switch (event.getStage()) {
            case Pre:
                this.target = getTarget();
                if(minCPS.getValue() > maxCPS.getValue()) {
                    minCPS.setValue(minCPS.getValue() - 1);
                }

                break;
            case Post:

                break;
        }
    });

    @EventHandler
    private Listener<Render3DEvent> render3DEventListener = new Listener<>(event -> {
        if(target != null) {

        }
    });


    private Entity getTarget() {
        if(mc.theWorld.loadedEntityList.isEmpty()) {
            return null;
        }
        for(Entity e : mc.theWorld.loadedEntityList) {
            if(e == null) {
                continue;
            }
            if(e == mc.thePlayer) {
                continue;
            }

            if(!players.getValue() && e instanceof EntityPlayer) {
                continue;
            }

            if(!hostiles.getValue() && e instanceof EntityMob) {
                continue;
            }

            if(!passive.getValue() && EntityUtil.isPassive(e)) {
                continue;
            }

            if(e.isDead) {
                continue;
            }

            if(mc.thePlayer.getDistance(e.posX, e.posY, e.posZ) > 8) {
                continue;
            }

            return e;
        }

        return null;
    }

    private enum RenderMode {
        Circle
    }

    private enum RotationMode {
        Vanilla,
        Smooth
    }

    public Entity getCurrentTarget() {
        return this.target;
    }

}
