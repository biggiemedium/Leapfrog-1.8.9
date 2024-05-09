package dev.px.leapfrog.Client.Module;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.Listenable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

public class Module implements Listenable {

    private String name;
    private String description;
    private int keyBind;
    private boolean toggled;
    private boolean drawn;
    private Type type;

    public ArrayList<Setting<?>> settings = new ArrayList<>();
    protected Minecraft mc = Minecraft.getMinecraft();

    public Module() {
        if(this.getClass().isAnnotationPresent(ModuleInterface.class)) {
            this.name = getModule().name();
            this.description = getModule().description();
            this.keyBind = getModule().keyBind();
            this.toggled = getModule().toggled();
            this.drawn = getModule().drawn();
            this.type = getModule().type();
        } else {throw new RuntimeException("Not annotation in class " + this.getClass().getName());}
    }

    protected ModuleInterface getModule() {
        return getClass().getAnnotation(ModuleInterface.class);
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;

        if(this.toggled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public void toggle() {
        this.toggled = !this.toggled;

        if(this.toggled) {
            this.onEnable();
        } else {
            this.onDisable();
        }

    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        LeapFrog.EVENT_BUS.subscribe(this);
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        LeapFrog.EVENT_BUS.unsubscribe(this);
    }

    protected <T> Setting<T> create(Setting<T> hudSetting) {
        this.settings.add(hudSetting);
        return hudSetting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public boolean isToggled() {
        return toggled;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDrawn() {
        return drawn;
    }

    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }

    public ArrayList<Setting<?>> getSettings() {
        return settings;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ModuleInterface {

        String name();

        String description() default "";

        int keyBind() default -1;

        boolean toggled() default false;

        Type type();

        boolean drawn() default false;

    }
}
