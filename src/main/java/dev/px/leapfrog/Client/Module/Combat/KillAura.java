package dev.px.leapfrog.Client.Module.Combat;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;

@Module.ModuleInterface(name = "Kill aura", type = Type.Combat, description = "Auto targets enemies with sword")
public class KillAura extends Module {

    Setting<Boolean> setting = create(new Setting<>("Boolean setting", true));
    Setting<Boolean> setting2 = create(new Setting<>("Boolean setting 2", true));

}
