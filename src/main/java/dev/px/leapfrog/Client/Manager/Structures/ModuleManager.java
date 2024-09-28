package dev.px.leapfrog.Client.Manager.Structures;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Combat.*;
import dev.px.leapfrog.Client.Module.Ghost.*;
import dev.px.leapfrog.Client.Module.Misc.*;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Movement.*;
import dev.px.leapfrog.Client.Module.Render.*;

import java.util.ArrayList;
import java.util.Collections;

public class ModuleManager {

    private ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        Add(new ExtraKnockBack());
        Add(new TestModule());
        Add(new FastBow());
        Add(new KillAura());
        Add(new TargetStrafe());
        Add(new Velocity());

        // Ghost
        Add(new AimAssist());
        Add(new AntiClickDelay());
        Add(new AntiJumpDelay());
        Add(new AntiMiss());
        Add(new AutoClicker());
        Add(new Disabler());
        Add(new FastPlace());
        Add(new Hitbox());
        Add(new Reach());
        Add(new SafeBridge());

        // Misc
        Add(new AntiDesync());
        Add(new AntiFireball());
        Add(new AntiVoid());
        Add(new AutoArmor());
        Add(new AutoInventory());
        Add(new ChestStealer());
        Add(new ClientSpoofer());
        Add(new Debugger());
        Add(new FakePlayer());
        Add(new FreeCam());
        Add(new NoRotate());
        Add(new Timer());

        // Movement
        Add(new AirJump());
        Add(new FastLadder());
        Add(new FastWeb());
        Add(new Flight());
        Add(new Jesus());
        Add(new NoSlow());
        Add(new PacketSneak());
        Add(new Scaffold());
        Add(new Spider());
        Add(new Strafe());

        // Render
        Add(new Animations());
        Add(new ChatModification());
        Add(new ChinaHat());
        Add(new ESP());
        Add(new FPSBooster());
        Add(new FullBright());
        Add(new HotbarModification());
        Add(new HUD());
        Add(new ItemPhysics());
        Add(new NameTags());
        Add(new NoRender());
        Add(new NoShake());
        Add(new Notifications());
        Add(new TargetHUD());
        Add(new ThanosSnapEffect());

        Collections.sort(modules);
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
