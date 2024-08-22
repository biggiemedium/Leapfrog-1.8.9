package dev.px.leapfrog.API.Module;

import dev.px.leapfrog.Client.Module.Module;

public class SubModule<T extends Module> {

    private String name;
    private boolean active;
    private T parentModule;

    public SubModule(String name, T parentModule) {
        this.name = name;
        this.active = false;
        this.parentModule = parentModule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void toggle() {
        this.active = !this.active;
    }

    public T getParentModule() {
        return parentModule;
    }

    public void setParentModule(T parentModule) {
        this.parentModule = parentModule;
    }
}
