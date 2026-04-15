package com.example.virtual;

import java.time.*;

public record BlockingBenchmarkConfig(
        int taskCount,
        Duration waitDuration,
        int platformThreadCount
) {

    public BlockingBenchmarkConfig {
        if (taskCount < 1) {
            throw new IllegalArgumentException("taskCount must be positive");
        }
        if (waitDuration.isNegative() || waitDuration.isZero()) {
            throw new IllegalArgumentException("waitDuration must be positive");
        }
        if (platformThreadCount < 1) {
            throw new IllegalArgumentException("platformThreadCount must be positive");
        }
    }
}
