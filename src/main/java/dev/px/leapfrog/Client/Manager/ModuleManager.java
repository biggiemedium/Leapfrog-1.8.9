package dev.px.leapfrog.Client.Manager;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Combat.KillAura;
import dev.px.leapfrog.Client.Module.Combat.TestModule;
import dev.px.leapfrog.Client.Module.Misc.ClientSpoofer;
import dev.px.leapfrog.Client.Module.Misc.FakePlayer;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Movement.Strafe;
import dev.px.leapfrog.Client.Module.Render.ChinaHat;
import dev.px.leapfrog.Client.Module.Render.FullBright;

import java.util.ArrayList;

public class ModuleManager {

    private ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        Add(new TestModule());
        Add(new KillAura());

        Add(new ClientSpoofer());
        Add(new FakePlayer());

        Add(new Strafe());

        Add(new ChinaHat());
        Add(new FullBright());
    }

    private void Add(Module module) {
        this.modules.add(module);
    }

    public Module getModuleByName(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public ArrayList<Module> getModuleByType(Type type) {
        ArrayList<Module> module = new ArrayList<>();
        modules.stream().forEach(m -> {
            if(m.getType() == type) {
                module.add(m);
            }
        });
        return module;
    }

    public <T extends Module> Module getModule(Class<T> clazz) {
        return modules.stream()
                        .filter(module -> module.getClass() == clazz)
                        .map(module -> module)
                        .findFirst()
                        .orElse(null);
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

    public boolean isModuleToggled(Module module) {
        Module mod = modules.stream().filter(m -> m == module).findFirst().orElse(null);
        return mod != null && mod.isToggled();
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

}
