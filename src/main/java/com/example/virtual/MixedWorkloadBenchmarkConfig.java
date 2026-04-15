package com.example.virtual;

import java.time.*;

public record MixedWorkloadBenchmarkConfig(
        int dbTaskCount,
        int quickTaskCount,
        Duration dbWaitDuration,
        Duration quickWaitDuration,
        int platformThreadCount,
        int resourcePermits
) {
    public MixedWorkloadBenchmarkConfig {
        if (dbTaskCount < 1 || quickTaskCount < 1) {
            throw new IllegalArgumentException("Task counts must be positive");
        }
        if (dbWaitDuration == null || quickWaitDuration == null) {
            throw new IllegalArgumentException("Wait durations must not be null");
        }
        if (platformThreadCount < 1 || resourcePermits < 1) {
            throw new IllegalArgumentException("Thread and permit counts must be positive");
        }
    }
}
