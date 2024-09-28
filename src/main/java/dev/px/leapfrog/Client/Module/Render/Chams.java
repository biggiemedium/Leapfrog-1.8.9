package dev.px.leapfrog.Client.Module.Render;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;

@Module.ModuleInterface(name = "Chams", type = Type.Visual, description = "See players through walls")
public class Chams extends Module {

    public Chams() {

    }

    private Setting<Boolean> players = create(new Setting<>("Players", true));
    private Setting<Boolean> storage = create(new Setting<>("Storage", false));
    private Setting<Boolean> chests = create(new Setting<>("Chests", true, v -> storage.getValue()));
    private Setting<Boolean> enderChest = create(new Setting<>("Ender Chests", false, v -> storage.getValue()));



}
