package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.EntityUtil;
import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;

import javax.activity.ActivityRequiredException;
import java.util.List;

@Module.ModuleInterface(name = "Kill aura", type = Type.Combat, description = "Auto targets enemies with sword")
public class KillAura extends Module {

    public KillAura() {

    }

    private Entity target = null;

    private Setting<AttackMode> targetMode = create(new Setting<>("Target Mode", AttackMode.Single));
    private Setting<Boolean> invisibles = create(new Setting<>("Invisible", true));
    private Setting<Float> range = create(new Setting<>("Range", 4.0f, 0.0f, 10.0f));

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

    private enum AttackMode {
        Single,
        Multiple
    }
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
                if(minCPS.getValue() > maxCPS.getValue()) {
                    minCPS.setValue(minCPS.getValue() - 1);
                }
                List<EntityPlayer> targets = LeapFrog.targetManager.getTargets(range.getValue());
                if(targets == null || targets.isEmpty()) {
                    this.target = null;
                    return;
                }

                switch (targetMode.getValue()) {
                    case Single:
                        this.target = targets.get(0);
                        break;
                    case Multiple:
                        targets.removeIf(target -> mc.thePlayer.getDistanceSq(target.posX, target.posY, target.posZ) > range.getValue());

                        if (!targets.isEmpty()) {
                            targets.forEach(this::attack);
                        }
                        break;
                }

                break;
            case Post:

                break;
        }
    });

    private void attack(EntityPlayer player) {

    }

    @EventHandler
    private Listener<Render3DEvent> render3DEventListener = new Listener<>(event -> {
        if(target != null) {

        }
    });

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
