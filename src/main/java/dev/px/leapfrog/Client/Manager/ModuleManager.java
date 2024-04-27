package dev.px.leapfrog.Client.Manager;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Combat.TestModule;
import dev.px.leapfrog.Client.Module.Module;

import java.util.ArrayList;

public class ModuleManager {

    private ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        Add(new TestModule());
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
        return modules.stream().filter(module -> module.getClass() == clazz).map(module -> module).findFirst().orElse(null);
    }

    public boolean isModuleToggled(Module module) {
        Module mod = modules.stream().filter(m -> m == module).findFirst().orElse(null);
        return mod != null && mod.isToggled();
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

}
