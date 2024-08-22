package dev.px.leapfrog.Client.Module.Ghost;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerUpdateEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Game.InputUtil;
import dev.px.leapfrog.API.Util.Math.MathUtil;
import dev.px.leapfrog.API.Util.Math.TimerUtil;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Misc.Timer;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.ThreadLocalRandom;

@Module.ModuleInterface(name = "Auto Clicker", type = Type.Ghost, description = "Automates click speed")
public class AutoClicker extends Module {

    private Setting<Integer> minSpeed = create(new Setting<>("Min CPS", 4, 0, 15));
    private Setting<Integer> maxSpeed = create(new Setting<>("Max CPS", 10, 0, 20));
    private Setting<Boolean> noBlocking = create(new Setting<>("Blocking Check", true));
    private Setting<Boolean> autoBlock = create(new Setting<>("Auto Block", true, v -> !noBlocking.getValue()));

    private TimerUtil timer = new TimerUtil();
    private TimerUtil blockTimer = new TimerUtil();

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
            if(this.isMiss()) {
                return;
            }
            if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                return;
            }
            if (timer.passed(1000 / ThreadLocalRandom.current().nextInt(minSpeed.getValue(), maxSpeed.getValue() + 1))) {
                if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                // autoblock
                if (mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemTool || mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
                    if(mc.thePlayer.isBlocking() && noBlocking.getValue()) {
                        return;
                    } else if (autoBlock.getValue() && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit.isEntityAlive()){
                        if (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && blockTimer.passed(100)) {
                            mc.thePlayer.getCurrentEquippedItem().useItemRightClick(mc.theWorld, mc.thePlayer);
                            blockTimer.reset();
                        }
                    }

                        if (mc.objectMouseOver.entityHit != null) {
                            mc.thePlayer.swingItem();
                            mc.playerController.attackEntity(mc.thePlayer, mc.objectMouseOver.entityHit);
                        }

                        if(!LeapFrog.moduleManager.isModuleToggled(AntiClickDelay.class)) {
                            ((IMixinMinecraft) mc).setLeftClickCounter(0);
                        }
                        timer.reset();
                    }
                }
            }
        } else {

        }
    });

    private boolean isMiss() {
        AntiMiss miss = LeapFrog.moduleManager.getModuleByClass(AntiMiss.class);
        if(miss != null) {
            if(miss.isToggled()) {
                if(mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
                    return true;
                }
            }
        }
        return false;
    }

    private long getCpsDelay() {
        int minCPS = minSpeed.getValue();
        int maxCPS = maxSpeed.getValue();
        int cps = ThreadLocalRandom.current().nextInt(minCPS, maxCPS + 1);
        return (long) Math.ceil(1000.0 / cps);
    }
}
