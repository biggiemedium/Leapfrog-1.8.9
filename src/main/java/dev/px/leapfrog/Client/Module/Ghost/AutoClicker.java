package dev.px.leapfrog.Client.Module.Ghost;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerUpdateEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Math.MathUtil;
import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.Client.Module.Misc.Timer;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.ThreadLocalRandom;

@Module.ModuleInterface(name = "Auto Clicker", type = Type.Ghost, description = "Automates click speed")
public class AutoClicker extends Module {

    private Setting<Integer> minSpeed = create(new Setting<>("Min CPS", 4, 0, 15));
    private Setting<Integer> maxSpeed = create(new Setting<>("Max CPS", 10, 0, 20));
    private Setting<Boolean> noBlocking = create(new Setting<>("Blocking Check", true));

    private TimerUtil timer = new TimerUtil();

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if (event.getStage() == Event.Stage.Pre) {
            if(minSpeed.getValue() > maxSpeed.getValue()) {
                minSpeed.setValue(minSpeed.getValue() - 1);
            }
            if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                if (mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemTool || mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
                    if(mc.thePlayer.isBlocking() && noBlocking.getValue()) {
                        return;
                    }
                    if (timer.passed(getCpsDelay())) {
                        mc.thePlayer.swingItem();
                        if (mc.objectMouseOver.entityHit != null) {
                            mc.playerController.attackEntity(mc.thePlayer, mc.objectMouseOver.entityHit);
                        }
                        timer.reset();
                    }
                }
            }
        } else {

        }
    });

    private long getCpsDelay() {
        int minCPS = minSpeed.getValue();
        int maxCPS = maxSpeed.getValue();
        int cps = ThreadLocalRandom.current().nextInt(minCPS, maxCPS + 1);
        return 1000L / cps;
    }
}
