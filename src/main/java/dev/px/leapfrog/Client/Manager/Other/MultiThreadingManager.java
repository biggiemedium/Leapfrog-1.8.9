package dev.px.leapfrog.Client.Manager.Other;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.API.Module.ServiceModule;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.Listenable;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author 3arthQu4ke
 */
public class MultiThreadingManager implements Listenable {

    public ThreadFactory FACTORY = newDaemonThreadFactoryBuilder().setNameFormat("LeapFrog-Thread-%d").build();
    public ExecutorService EXECUTOR = Executors.newCachedThreadPool(FACTORY);
    /** For tasks that can go out of Hand quickly. */
    public ExecutorService FIXED_EXECUTOR = newFixedThreadPool((int)(Runtime.getRuntime().availableProcessors() / 1.5));
    private static final Queue<Runnable> clientProcesses = new ArrayDeque<>();

    public MultiThreadingManager() {
        LeapFrog.EVENT_BUS.subscribe(this);

        /*                          Experimental, I have no need for this yet. I will when we port to 1.20
        EXECUTOR.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (!clientProcesses.isEmpty()) {
                        clientProcesses.poll().run();
                    }
                    for (Module module : LeapFrog.moduleManager.getModules()) {
                        if (module.isToggled() && module instanceof ServiceModule) {
                            try {
                                ((ServiceModule) module).onThread();
                            } catch (Exception e) {
                                LeapFrog.LOGGER.error("Error in ServiceModule onThread execution", e);
                            }
                        }
                    }
                    Thread.sleep(10);
                } catch (Exception e) {
                    LeapFrog.LOGGER.error("Unexpected error in MultiThreadingManager", e);
                }
            }
        });

         */
    }

    /**
     * Submits the given Runnable to an {@link ExecutorService}.
     * The Future that results from the call is returned.
     *
     * @param runnable the runnable to run on a separate thread.
     * @return the Future returned by the ExecutorService.
     */
    public Future<?> submitRunnable(Runnable runnable) {
        return EXECUTOR.submit(runnable);
    }

    public void submit(Runnable runnable) {
        clientProcesses.add(runnable);
    }

    /**
     * Shuts down Executor.
     */
    public void shutDown() {
        EXECUTOR.shutdown();
        FIXED_EXECUTOR.shutdown();
    }

    private ScheduledExecutorService newDaemonScheduledExecutor(String name) {
        ThreadFactoryBuilder factory = newDaemonThreadFactoryBuilder();
        factory.setNameFormat("LeapFrog-" + name + "-%d");
        return Executors.newSingleThreadScheduledExecutor(factory.build());
    }

    private ThreadFactoryBuilder newDaemonThreadFactoryBuilder() {
        ThreadFactoryBuilder factory = new ThreadFactoryBuilder();
        factory.setDaemon(true);
        return factory;
    }

    private ExecutorService newFixedThreadPool(int size) {
        ThreadFactoryBuilder factory = newDaemonThreadFactoryBuilder();
        factory.setNameFormat("LeapFrog-Fixed-%d");
        return Executors.newFixedThreadPool(Math.max(size, 1), factory.build());
    }
}
