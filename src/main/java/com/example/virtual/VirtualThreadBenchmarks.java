package com.example.virtual;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public final class VirtualThreadBenchmarks {

    private static final String BLOCKING_SCENARIO = "blocking-sleep";
    private static final String LIMITED_RESOURCE_SCENARIO = "limited-resource";

    private VirtualThreadBenchmarks() {
    }

    public static BenchmarkComparison compareBlockingTasks(BlockingBenchmarkConfig config) {
        BenchmarkResult fixedPool = runBlockingScenario(
                BLOCKING_SCENARIO,
                config.taskCount(),
                config.waitDuration(),
                config.platformThreadCount(),
                false
        );
        BenchmarkResult virtualThreads = runBlockingScenario(
                BLOCKING_SCENARIO,
                config.taskCount(),
                config.waitDuration(),
                config.platformThreadCount(),
                true
        );
        return new BenchmarkComparison(BLOCKING_SCENARIO, fixedPool, virtualThreads);
    }

    public static BenchmarkComparison compareLimitedResourceTasks(LimitedResourceBenchmarkConfig config) {
        BenchmarkResult fixedPool = runLimitedResourceScenario(config, false);
        BenchmarkResult virtualThreads = runLimitedResourceScenario(config, true);
        return new BenchmarkComparison(LIMITED_RESOURCE_SCENARIO, fixedPool, virtualThreads);
    }

    public static BenchmarkComparison compareThroughput(LimitedResourceBenchmarkConfig config) {
        BenchmarkResult fixedPool = runLimitedResourceScenario(config, false);
        BenchmarkResult virtualThreads = runLimitedResourceScenario(config, true);
        return new BenchmarkComparison("throughput-web-server", fixedPool, virtualThreads);
    }

    public static void printMixedWorkloadComparison(MixedWorkloadBenchmarkConfig config) {
        System.out.println("--- Mixed Workload Simulation (Resilience Test) ---");
        MixedWorkloadResult fixedPool = compareMixedWorkload(config, false);
        System.out.println("Fixed Pool Results:");
        System.out.println(fixedPool.summary());

        MixedWorkloadResult virtualThreads = compareMixedWorkload(config, true);
        System.out.println("Virtual Thread Results:");
        System.out.println(virtualThreads.summary());
        
        double latencyFactor = fixedPool.quickTaskLatencies().getAverage() / virtualThreads.quickTaskLatencies().getAverage();
        System.out.printf("Relative Latency (Fixed/Virtual): %.2f ms / %.2f ms = %.2fx slower\n\n", 
                fixedPool.quickTaskLatencies().getAverage(), virtualThreads.quickTaskLatencies().getAverage(), latencyFactor);
    }

    private static BenchmarkResult runBlockingScenario(
            String scenarioName,
            int taskCount,
            Duration waitDuration,
            int platformThreadCount,
            boolean virtualThreads
    ) {
        TaskMetrics metrics = new TaskMetrics();
        long elapsedMillis = runScenario(taskCount, platformThreadCount, virtualThreads, () -> {
            metrics.taskStarted();
            try {
                Thread.sleep(waitDuration);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Benchmark interrupted", e);
            } finally {
                metrics.taskFinished();
            }
        });

        return new BenchmarkResult(
                scenarioName,
                label(virtualThreads, platformThreadCount),
                taskCount,
                platformThreadCount,
                taskCount,
                elapsedMillis,
                metrics.peakActiveTasks(),
                metrics.peakWaitingForPermit(),
                metrics.peakPermitsInUse()
        );
    }

    private static BenchmarkResult runLimitedResourceScenario(
            LimitedResourceBenchmarkConfig config,
            boolean virtualThreads
    ) {
        Semaphore semaphore = new Semaphore(config.resourcePermits());
        TaskMetrics metrics = new TaskMetrics();
        int taskCount = config.throughputTaskCount() > 0 ? config.throughputTaskCount() : config.taskCount();

        long elapsedMillis = runScenario(taskCount, config.platformThreadCount(), virtualThreads, () -> {
            metrics.taskStarted();
            try {
                if (config.busyWaitDuration().toNanos() > 0) {
                    busyWait(config.busyWaitDuration());
                }
                if (config.busyLoopIterations() > 0) {
                    busyLoop(config.busyLoopIterations());
                }
                if (config.preparationWaitDuration().toNanos() > 0) {
                    Thread.sleep(config.preparationWaitDuration());
                }
                metrics.waitingForPermitStarted();
                boolean permitAcquired = false;
                try {
                    semaphore.acquire();
                    permitAcquired = true;
                    metrics.permitAcquired();
                    Thread.sleep(config.waitDuration());
                } finally {
                    metrics.permitReleasedIfHeld();
                    if (permitAcquired) {
                        semaphore.release();
                    }
                    metrics.taskFinished();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Benchmark interrupted", e);
            }
        });

        return new BenchmarkResult(
                config.throughputTaskCount() > 0 ? "throughput-web-server" : LIMITED_RESOURCE_SCENARIO,
                label(virtualThreads, config.platformThreadCount()),
                taskCount,
                config.platformThreadCount(),
                config.resourcePermits(),
                elapsedMillis,
                metrics.peakActiveTasks(),
                metrics.peakWaitingForPermit(),
                metrics.peakPermitsInUse()
        );
    }

    private static long runScenario(
            int taskCount,
            int platformThreadCount,
            boolean virtualThreads,
            Runnable task
    ) {
        long startNanos = System.nanoTime();

        try (ExecutorService executor = createExecutor(platformThreadCount, virtualThreads)) {
            List<Future<?>> futures = new ArrayList<>(taskCount);
            for (int i = 0; i < taskCount; i++) {
                futures.add(executor.submit(task));
            }
            for (Future<?> future : futures) {
                awaitCompletion(future);
            }
        }

        return Duration.ofNanos(System.nanoTime() - startNanos).toMillis();
    }

    private static ExecutorService createExecutor(int platformThreadCount, boolean virtualThreads) {
        if (virtualThreads) {
            return Executors.newVirtualThreadPerTaskExecutor();
        }

        ThreadFactory threadFactory = Thread.ofPlatform()
                .name("benchmark-platform-", 0)
                .factory();
        return Executors.newFixedThreadPool(platformThreadCount, threadFactory);
    }

    public static MixedWorkloadResult compareMixedWorkload(MixedWorkloadBenchmarkConfig config, boolean virtualThreads) {
        Semaphore semaphore = new Semaphore(config.resourcePermits());
        TaskMetrics metrics = new TaskMetrics();
        DoubleSummaryStatistics quickTaskLatencies = new DoubleSummaryStatistics();
        
        long startNanos = System.nanoTime();
        
        try (ExecutorService executor = createExecutor(config.platformThreadCount(), virtualThreads)) {
            List<Future<?>> futures = new ArrayList<>();
            
            // Start DB heavy tasks
            for (int i = 0; i < config.dbTaskCount(); i++) {
                executor.execute(() -> {
                    metrics.taskStarted();
                    try {
                        // DB-Blockierung simulieren
                        // VTs geben den Carrier Thread bei acquire() frei, wenn die Semaphore belegt ist.
                        // Plattform-Threads blockieren den Thread im Pool, während sie auf die Semaphore warten.
                        semaphore.acquire();
                        try {
                            Thread.sleep(config.dbWaitDuration());
                        } finally {
                            semaphore.release();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        metrics.taskFinished();
                    }
                });
            }
            
            // Start Quick tasks (delayed to ensure DB tasks are in flight and occupying the pool)
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            
            for (int i = 0; i < config.quickTaskCount(); i++) {
                final int taskId = i;
                long submissionNanos = System.nanoTime();
                futures.add(executor.submit(() -> {
                    long taskExecutionStartNanos = System.nanoTime();
                    metrics.taskStarted();
                    try {
                        Thread.sleep(config.quickWaitDuration());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        // Latency = End-to-End time from submission to completion
                        long latency = (System.nanoTime() - submissionNanos) / 1_000_000;
                        synchronized (quickTaskLatencies) {
                            quickTaskLatencies.accept(latency);
                        }
                        metrics.taskFinished();
                    }
                }));
                // Kurze Pause zwischen Quick Tasks
                if (taskId % 5 == 0) {
                    try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                }
            }
            
            for (Future<?> future : futures) {
                awaitCompletion(future);
            }
        }
        
        long totalElapsedMillis = Duration.ofNanos(System.nanoTime() - startNanos).toMillis();
        
        return new MixedWorkloadResult(
                "Mixed Workload",
                label(virtualThreads, config.platformThreadCount()),
                totalElapsedMillis,
                quickTaskLatencies,
                metrics.peakActiveTasks()
        );
    }

    private static void awaitCompletion(Future<?> future) {
        try {
            future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Benchmark interrupted", e);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Benchmark task failed", e.getCause());
        }
    }

    private static void busyWait(Duration duration) {
        long nanos = duration.toNanos();
        long start = System.nanoTime();
        while (System.nanoTime() - start < nanos) {
            // spin
        }
    }

    private static void busyLoop(long iterations) {
        // Simple calculation to prevent JVM from optimizing away the loop
        double result = 0;
        for (long i = 0; i < iterations; i++) {
            result += Math.sqrt(i);
        }
        if (result < 0) {
            System.out.println("Should not happen: " + result);
        }
    }

    private static String label(boolean virtualThreads, int platformThreadCount) {
        return virtualThreads ? "virtual-threads" : "fixed-pool-" + platformThreadCount;
    }

    private static final class TaskMetrics {
        private final AtomicInteger activeTasks = new AtomicInteger();
        private final AtomicInteger peakActiveTasks = new AtomicInteger();
        private final AtomicInteger waitingForPermit = new AtomicInteger();
        private final AtomicInteger peakWaitingForPermit = new AtomicInteger();
        private final AtomicInteger permitsInUse = new AtomicInteger();
        private final AtomicInteger peakPermitsInUse = new AtomicInteger();
        private final ThreadLocal<Boolean> permitHeld = ThreadLocal.withInitial(() -> false);

        void taskStarted() {
            updatePeak(activeTasks.incrementAndGet(), peakActiveTasks);
        }

        void taskFinished() {
            activeTasks.decrementAndGet();
        }

        void waitingForPermitStarted() {
            updatePeak(waitingForPermit.incrementAndGet(), peakWaitingForPermit);
        }

        void permitAcquired() {
            waitingForPermit.decrementAndGet();
            permitHeld.set(true);
            updatePeak(permitsInUse.incrementAndGet(), peakPermitsInUse);
        }

        void permitReleasedIfHeld() {
            if (permitHeld.get()) {
                permitsInUse.decrementAndGet();
                permitHeld.remove();
            } else {
                waitingForPermit.decrementAndGet();
            }
        }

        int peakActiveTasks() {
            return peakActiveTasks.get();
        }

        int peakWaitingForPermit() {
            return peakWaitingForPermit.get();
        }

        int peakPermitsInUse() {
            return peakPermitsInUse.get();
        }

        private static void updatePeak(int candidate, AtomicInteger peak) {
            peak.accumulateAndGet(candidate, Math::max);
        }
    }
}
