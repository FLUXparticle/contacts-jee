package com.example.virtual;

import java.time.*;

public record LimitedResourceBenchmarkConfig(
        int taskCount,
        Duration waitDuration,
        int platformThreadCount,
        int resourcePermits,
        Duration busyWaitDuration,
        Duration preparationWaitDuration,
        long busyLoopIterations,
        int throughputTaskCount
) {

    public LimitedResourceBenchmarkConfig(
            int taskCount,
            Duration waitDuration,
            int platformThreadCount,
            int resourcePermits
    ) {
        this(taskCount, waitDuration, platformThreadCount, resourcePermits, Duration.ZERO, Duration.ZERO, 0L, 0);
    }

    public LimitedResourceBenchmarkConfig {
        if (taskCount < 1) {
            throw new IllegalArgumentException("taskCount must be positive");
        }
        if (waitDuration.isNegative() || waitDuration.isZero()) {
            throw new IllegalArgumentException("waitDuration must be positive");
        }
        if (platformThreadCount < 1) {
            throw new IllegalArgumentException("platformThreadCount must be positive");
        }
        if (resourcePermits < 1) {
            throw new IllegalArgumentException("resourcePermits must be positive");
        }
        if (busyWaitDuration == null || busyWaitDuration.isNegative()) {
            throw new IllegalArgumentException("busyWaitDuration must not be negative");
        }
        if (preparationWaitDuration == null || preparationWaitDuration.isNegative()) {
            throw new IllegalArgumentException("preparationWaitDuration must not be negative");
        }
        if (busyLoopIterations < 0) {
            throw new IllegalArgumentException("busyLoopIterations must not be negative");
        }
        if (throughputTaskCount < 0) {
            throw new IllegalArgumentException("throughputTaskCount must not be negative");
        }
    }
}
