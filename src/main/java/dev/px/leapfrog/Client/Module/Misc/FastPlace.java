package dev.px.leapfrog.Client.Module.Misc;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.ASM.Listeners.IMixinMinecraft;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.item.ItemBlock;

@Module.ModuleInterface(name = "Fast Place", type = Type.Misc, description = "Decreases place delay")
public class FastPlace extends Module {

    public FastPlace() {

    }

    private Setting<Mode> mode = create(new Setting<>("Mode", Mode.Pre));
    private Setting<Integer> delay = create(new Setting<>("Delay", 1, 0, 3));

    @EventHandler
    private Listener<PlayerMotionEvent> motionEventListener = new Listener<>(event -> {
        if(event.getStage() == Event.Stage.Pre) {
            if(mode.getValue() == Mode.Pre) {
            if(mc.thePlayer.getHeldItem() != null) {
                if (mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
                    ((IMixinMinecraft) mc).setRightClickDelayTimer(Math.min(((IMixinMinecraft) mc).getRightClickDelayTimer(), delay.getValue()));
                    }
                }
            }
        } else {
            if(mode.getValue() == Mode.Post) {
                if(mc.thePlayer.getHeldItem() != null) {
                    if (mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
                        ((IMixinMinecraft) mc).setRightClickDelayTimer(Math.min(((IMixinMinecraft) mc).getRightClickDelayTimer(), delay.getValue()));
                    }
                }
            }
        }
    });

    private enum Mode {
        Pre,
        Post
    }

}
