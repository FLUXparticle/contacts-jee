package com.example.virtual;

public record BenchmarkComparison(
        String scenarioName,
        BenchmarkResult fixedPoolResult,
        BenchmarkResult virtualThreadResult
) {

    public double speedup() {
        return fixedPoolResult.elapsedMillis() / (double) virtualThreadResult.elapsedMillis();
    }

    public String prettyPrint() {
        return """
                Scenario: %s
                  %s
                  %s
                  relative speedup (fixed/virtual): %.2fx
                """.formatted(
                scenarioName,
                fixedPoolResult.summaryLine(),
                virtualThreadResult.summaryLine(),
                speedup()
        );
    }
}
