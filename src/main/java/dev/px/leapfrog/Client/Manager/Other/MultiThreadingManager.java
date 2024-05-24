package dev.px.leapfrog.Client.Manager.Other;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author 3arthQu4ke
 */
public class MultiThreadingManager {

    public ThreadFactory FACTORY = newDaemonThreadFactoryBuilder().setNameFormat("LeapFrog-Thread-%d").build();
    public ExecutorService EXECUTOR = Executors.newCachedThreadPool(FACTORY);
    /** For tasks that can go out of Hand quickly. */
    public ExecutorService FIXED_EXECUTOR = newFixedThreadPool((int)(Runtime.getRuntime().availableProcessors() / 1.5));

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

    /**
     * Shuts down Executor.
     */
    public void shutDown() {
        EXECUTOR.shutdown();
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
