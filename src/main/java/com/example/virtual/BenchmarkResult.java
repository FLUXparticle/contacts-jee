package com.example.virtual;

public record BenchmarkResult(
        String scenarioName,
        String executorLabel,
        int taskCount,
        int platformThreadCount,
        int resourcePermits,
        long elapsedMillis,
        int peakActiveTasks,
        int peakWaitingForPermit,
        int peakPermitsInUse
) {

    public String summaryLine() {
        return "%s [%s] tasks=%d, elapsed=%d ms, peakActive=%d, peakWaitingForPermit=%d, peakPermitsInUse=%d"
                .formatted(
                        scenarioName,
                        executorLabel,
                        taskCount,
                        elapsedMillis,
                        peakActiveTasks,
                        peakWaitingForPermit,
                        peakPermitsInUse
                );
    }
}
