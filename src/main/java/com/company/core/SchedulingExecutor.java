package com.company.core;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;

/**
 * This executor awaits before running tasks scheduled for future.
 */
public class SchedulingExecutor {
    public static int CORES = Runtime.getRuntime().availableProcessors() * 2;
    private BlockingQueue<PrioritizedTask> timeBasedPriorityQueue = new LinkedBlockingQueue<>();
    private ExecutorService pool = Executors.newFixedThreadPool(CORES);

    public SchedulingExecutor(int numberOfWorkers) {
        for (int counter = 0; counter < numberOfWorkers; counter++) {
            pool.submit(new LingeringWorker());
        }
    }

    public SchedulingExecutor() {
        this(CORES);
    }

    /**
     * @param toStartAt scheduled start time
     * @param callable  task to run
     */
    public void submit(LocalDateTime toStartAt, Callable callable) {
        timeBasedPriorityQueue.add(new PrioritizedTask(callable, toStartAt));
    }

    /**
     * This worker never sleep but it waits for actionable items from a timeBasedPriorityQueue of the enclosing class. <br>
     * In case it is to early to start on a given task this worker just yields to other threads.
     */
    private class LingeringWorker implements Callable {

        @Override
        public Object call() throws Exception {
            while (!pool.isShutdown()) {
                PrioritizedTask task = timeBasedPriorityQueue.take();
                if (LocalDateTime.now().compareTo(task.getDateTime()) <= 0) {
                    Thread.sleep(ChronoUnit.MILLIS.between(task.getDateTime(), LocalDateTime.now()));
                }
                while (LocalDateTime.now().compareTo(task.getDateTime()) <= 0) {
                    Thread.yield();//be on standby in case we have to wait for some microseconds
                }
                task.getCallable().call();
            }
            return null;
        }
    }

    /**
     * Initiates internal pool shutdown.
     */
    public void terminate() {
        pool.shutdownNow();
    }

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

}
