package com.example.virtual;

import java.util.*;

public record MixedWorkloadResult(
        String scenarioName,
        String executorLabel,
        long totalElapsedMillis,
        DoubleSummaryStatistics quickTaskLatencies,
        int peakActiveTasks
) {
    public String summary() {
        return """
               [%s] Total elapsed: %d ms, Peak active: %d
               Quick Tasks (Latency): avg=%.2f ms, max=%.2f ms, count=%d
               """.formatted(
                executorLabel,
                totalElapsedMillis,
                peakActiveTasks,
                quickTaskLatencies.getAverage(),
                quickTaskLatencies.getMax(),
                quickTaskLatencies.getCount()
        );
    }
}
