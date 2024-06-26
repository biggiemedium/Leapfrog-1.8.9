package dev.px.leapfrog.Client.Module.Ghost;

import dev.px.leapfrog.API.Event.Entity.EntityCollisionBorderSize;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

@Module.ModuleInterface(name = "Hitbox", type = Type.Ghost, description = "Change hitboxs")
public class Hitbox extends Module {

    public Hitbox() {

    }

    private Setting<Float> hitboxSize = create(new Setting<>("Hitbox Size", 0.2F, 0F, 1F));

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    private Listener<EntityCollisionBorderSize> borderSizeListener = new Listener<>(event -> {
        event.cancel();
        event.setBorderSize(hitboxSize.getValue());
    });

}
