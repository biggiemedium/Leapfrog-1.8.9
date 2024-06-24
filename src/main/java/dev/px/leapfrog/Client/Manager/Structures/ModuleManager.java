package dev.px.leapfrog.Client.Manager.Structures;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Combat.FastBow;
import dev.px.leapfrog.Client.Module.Combat.KillAura;
import dev.px.leapfrog.Client.Module.Combat.TestModule;
import dev.px.leapfrog.Client.Module.Combat.Velocity;
import dev.px.leapfrog.Client.Module.Misc.*;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Movement.*;
import dev.px.leapfrog.Client.Module.Render.*;

import java.util.ArrayList;

public class ModuleManager {

    private ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        Add(new TestModule());
        Add(new FastBow());
        Add(new KillAura());
        Add(new Velocity());

        Add(new AntiClickDelay());
        Add(new AntiVoid());
        Add(new ClientSpoofer());
        Add(new FakePlayer());
        Add(new FastPlace());
        Add(new FreeCam());
        Add(new NoRotate());
        Add(new Timer());

        Add(new AirJump());
        Add(new FastLadder());
        Add(new Flight());
        Add(new Jesus());
        Add(new NoSlow());
        Add(new PacketSneak());
        Add(new Spider());
        Add(new Strafe());

        Add(new ChatModification());
        Add(new ChinaHat());
        Add(new ESP());
        Add(new FPSBooster());
        Add(new FullBright());
        Add(new HotbarModification());
        Add(new ItemPhysics());
        Add(new NameTags());
        Add(new NoShake());
        Add(new ThanosSnapEffect());
    }

    private void Add(Module module) {
        this.modules.add(module);
    }

    public Module getModuleByName(String name) {
        for(Module m : modules) {
            if(m.getName().equals(name)) {
                return m;
            }
        }
        return null;
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

    public boolean isModuleToggled(Class module) {
        Module mod = modules.stream().filter(m -> m.getClass() == module).findFirst().orElse(null);
        return mod != null && mod.isToggled();
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

}
