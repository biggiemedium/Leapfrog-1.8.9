package dev.px.leapfrog.API.Module;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Manager.Other.MultiThreadingManager;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.LeapFrog;

import java.util.concurrent.Future;

public abstract class ServiceModule extends Module {

    private Future<?> serviceTask;

    public ServiceModule() {
        super();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        serviceTask = LeapFrog.threadManager.submitRunnable(this::onThread);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (serviceTask != null && !serviceTask.isDone()) {
            serviceTask.cancel(true);
        }
    }

    public abstract void onThread();

}
