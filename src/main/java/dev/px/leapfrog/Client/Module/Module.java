package dev.px.leapfrog.Client.Module;

import dev.px.leapfrog.API.Module.Setting.Bind;
import dev.px.leapfrog.API.Module.Toggleable;
import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.Client.GUI.Notifications.Notification;
import dev.px.leapfrog.Client.Module.Render.Notifications;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.Listenable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraftforge.common.MinecraftForge;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

public class Module extends Toggleable implements Listenable, Comparable<Module> {

    private boolean drawn;
    private boolean safeToggle;
    private Type type;

    public ArrayList<Setting<?>> settings = new ArrayList<>();
    public Setting<Bind> keybind = create(new Setting<>("Keybind", new Bind(-1)));
    protected Minecraft mc = Minecraft.getMinecraft();

    private int violations = 0;

    public Module() {
        super("", "", false);
        if(this.getClass().isAnnotationPresent(ModuleInterface.class)) {
            this.name = getModule().name();
            this.description = getModule().description();
            this.keybind.setValue(new Bind(getModule().keyBind()));
            this.toggled = getModule().toggled();
            this.safeToggle = getModule().safeToggle();
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

        if (LeapFrog.moduleManager.isModuleToggled(Notifications.class) && LeapFrog.moduleManager.getModuleByClass(Notifications.class).toggle.getValue()) {
            String titleToggle = "Module toggled";
            String descriptionToggleOn = this.getName() + " was " + "§aenabled\r";
            String descriptionToggleOff = this.getName() + " was " + "§cdisabled\r";

            switch (LeapFrog.moduleManager.getModuleByClass(Notifications.class).mode.getValue()) {
                case Client:
                    break;
                case SuicideX:
                    if (this.isToggled()) {
                        titleToggle = "Enabled Module " + this.getName() + ".";
                    } else {
                        titleToggle = "Disabled Module " + this.getName() + ".";
                    }
                    descriptionToggleOff = "";
                    descriptionToggleOn = "";
                    break;
            }
            if (toggled) {
                LeapFrog.notificationManager.post(titleToggle, descriptionToggleOn, Notification.NotificationType.Enable);
            } else {
                LeapFrog.notificationManager.post(titleToggle, descriptionToggleOff, Notification.NotificationType.Disable);
            }
        }

        if(this.toggled) {
            this.onEnable();
        } else {
            this.onDisable();
        }

    }

    public void safeToggle(S08PacketPlayerPosLook packet, boolean teleport) {
        this.violations++;
        if(violations > 10 && !teleport) {
            violations = 0;
            this.toggle();
            ChatUtil.sendClientSideMessage("Client is flagging too much! Toggling...");
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

    public boolean isSafeToggle() {
        return safeToggle;
    }

    public void setSafeToggle(boolean safeToggle) {
        this.safeToggle = safeToggle;
    }

    @Override
    public int compareTo(Module other) {
        return this.name.compareTo(other.name);
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

        boolean safeToggle() default false;

    }
}
