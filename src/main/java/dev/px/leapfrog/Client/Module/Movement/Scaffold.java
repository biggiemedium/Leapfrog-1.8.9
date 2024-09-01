package dev.px.leapfrog.Client.Module.Movement;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;

@Module.ModuleInterface(name = "Scaffold", type = Type.Movement, description = "Places blocks under you to prevent falling")
public class Scaffold extends Module {

    public Scaffold() {

    }

    private Setting<TowerMode> towerMode = create(new Setting<>("Tower Mode", TowerMode.Normal));
    private Setting<ScaffoldMode> scaffoldMode = create(new Setting<>("Scaffold Mode", ScaffoldMode.Normal));


    private enum TowerMode {
        Normal,
        Packet
    }

    private enum ScaffoldMode {
        Normal
    }

}
