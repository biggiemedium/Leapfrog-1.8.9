package dev.px.leapfrog.Client.Module.Ghost;

import dev.px.leapfrog.API.Event.Input.ClickMouseEvent;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.ASM.Listeners.IMixinItemRenderer;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.util.MovingObjectPosition;

@Module.ModuleInterface(name = "Anti Miss", type = Type.Ghost, description = "Prevents clicking unless hovering over entity/block")
public class AntiMiss extends Module {

    public AntiMiss() {

    }

    private Setting<Boolean> fakeSwing = create(new Setting<>("Fake Swing", true));

    @EventHandler
    private Listener<ClickMouseEvent> attackEntityEventListener = new Listener<>(event -> {
        if(mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
            event.cancel();
            if(fakeSwing.getValue()) {
                mc.thePlayer.swingItem();
            }
        }
    });

}
