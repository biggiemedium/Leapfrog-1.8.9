package dev.px.leapfrog.Client.Module.Misc;

import dev.px.leapfrog.API.Module.Setting.Bind;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@Module.ModuleInterface(name = "MCP", type = Type.Misc, description = "Throws enderpearl on keybind press")
public class MiddleClickPearl extends Module {

    public MiddleClickPearl() {

    }

    private Setting<Bind> bind = create(new Setting<>("Throw button", null));

}
