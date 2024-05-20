package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;

@Module.ModuleInterface(name = "FPS Booster", description = "Boosts Framerate", type = Type.Visual, toggled = true)
public class FPSBooster extends Module {

    public FPSBooster() {

    }

    public Setting<Boolean> displayFocus = create(new Setting<>("Display Focus", true));

}
