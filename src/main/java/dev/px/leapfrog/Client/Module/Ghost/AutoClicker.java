
package dev.px.leapfrog.Client.Module.Ghost;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Input.ClickMouseEvent;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerUpdateEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Entity.PlayerUtil;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.ThreadLocalRandom;

@Module.ModuleInterface(name = "Auto Clicker", type = Type.Ghost, description = "Automates click speed")
public class AutoClicker extends Module {

    public AutoClicker() {

    }

    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.Normal));
    //private Setting<Boolean> jitter = create(new Setting<>("Jitter", false));

    // Normal mode
    private Setting<Integer> minCPS = create(new Setting<>("Min CPS", 8, 1, 20, v -> mode.getValue() == Mode.Normal));
    private Setting<Integer> maxCPS = create(new Setting<>("Max CPS", 14, 1, 20, v -> mode.getValue() == Mode.Normal));
    private Setting<Boolean> rightClick = create(new Setting<>("Right Click", false, v -> mode.getValue() == Mode.Normal));
    private Setting<Boolean> leftClick = create(new Setting<>("Left Click", true, v -> mode.getValue() == Mode.Normal));
    private Setting<Boolean> hitSelect = create(new Setting<>("Hit Select", false, v -> mode.getValue() == Mode.Normal));

    // Drag mode
    private Setting<Integer> minLength = create(new Setting<>("Min Drag Length", 17, 1, 50, v -> mode.getValue() == Mode.Drag));
    private Setting<Integer> maxLength = create(new Setting<>("Max Drag Length", 18, 1, 50, v -> mode.getValue() == Mode.Drag));
    private Setting<Integer> minDelay = create(new Setting<>("Min Delay", 1, 1, 20));
    private Setting<Integer> maxDelay = create(new Setting<>("Max Delay", 6, 1, 20));

    // General
    private TimerUtil stopWatch = new TimerUtil();
    private double directionX, directionY;

    // Drag
    private int nextLength, nextDelay;

    // Normal
    private final TimerUtil clickStopWatch = new TimerUtil();
    private int ticksDown, attackTicks;
    private long nextSwing;


    @EventHandler
    private Listener<ClickMouseEvent> clickMouseEventListener = new Listener<>(event -> {
        stopWatch.reset();

        directionX = (Math.random() - 0.5) * 4;
        directionY = (Math.random() - 0.5) * 4;
    });

    @EventHandler
    private Listener<Render3DEvent> render3DEventListener = new Listener<>(event -> {
        //if (!stopWatch.passed(100) && this.jitter.getValue() && mc.gameSettings.keyBindUseItem.isKeyDown()) {
        //    EntityRenderer.mouseAddedX = (float) (((Math.random() - 0.5) * 400 / Minecraft.getDebugFPS()) * directionX);
        //    EntityRenderer.mouseAddedY = (float) (((Math.random() - 0.5) * 400 / Minecraft.getDebugFPS()) * directionY);
        //}
    });

    @EventHandler
    private Listener<TickEvent.PlayerTickEvent> tickEvent = new Listener<>(event -> {

        switch (mode.getValue()) {
            case Normal:
                this.attackTicks++;

                if (clickStopWatch.passed(this.nextSwing) && (!hitSelect.getValue() || ((hitSelect.getValue() && attackTicks >= 10) ||
                        (mc.thePlayer.hurtTime > 0 && clickStopWatch.passed(this.nextSwing)))) && mc.currentScreen == null) {
                    final long clicks = (long) (Math.round(MathUtil.getRandom(minCPS.getValue(), maxCPS.getValue())) * 1.5);

                    if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                        ticksDown++;
                    } else {
                        ticksDown = 0;
                    }

                    this.nextSwing = 1000 / clicks;

                    if (rightClick.getValue() && mc.gameSettings.keyBindUseItem.isKeyDown() && !mc.gameSettings.keyBindAttack.isKeyDown()) {
                        PlayerUtil.sendClick(1, true);

                        if (Math.random() > 0.9) {
                            PlayerUtil.sendClick(1, true);
                        }
                    }

                    if (leftClick.getValue() && ticksDown > 1 && (Math.sin(nextSwing) + 1 > Math.random() || Math.random() > 0.25 || clickStopWatch.passed(4 * 50)) && !mc.gameSettings.keyBindUseItem.isKeyDown() && (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)) {
                        PlayerUtil.sendClick(0, true);
                    }

                    this.clickStopWatch.reset();
                }
                break;

            case Drag:
                if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                    if (nextLength < 0) {
                        nextDelay--;

                        if (nextDelay < 0) {
                            nextDelay = getRandomBetween(minDelay.getValue(), maxDelay.getValue()).intValue();
                            nextLength = getRandomBetween(minLength.getValue(), maxLength.getValue()).intValue();
                        }
                    } else if (Math.random() < 0.95) {
                        nextLength--;
                        PlayerUtil.sendClick(0, true);
                    }
                }
                break;
        }

    });

    public Number getRandomBetween(int numMin, int numMax) {
        long min = (long) numMin;
        long max = (long) numMax;

        if (min == max) {
            return min;
        } else if (min > max) {
            final long d = min;
            min = max;
            max = d;
        }

        long random = ThreadLocalRandom.current().nextLong(min, max);
        return new Number() {
            @Override
            public int intValue() {
                return (int) random;
            }

            @Override
            public long longValue() {
                return random;
            }

            @Override
            public float floatValue() {
                return (float) random;
            }

            @Override
            public double doubleValue() {
                return (double) random;
            }
        };
    }

    private enum Mode {
        Normal,
        Drag
    }
}
