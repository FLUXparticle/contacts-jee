package com.example.virtual;

import org.junit.jupiter.api.*;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class VirtualThreadBenchmarksTest {

    @Test
    @DisplayName("Grundlage: Virtuelle Threads vs. Plattform-Threads bei blockierenden Aufgaben (Sleep)")
    void comparesFixedPoolWithVirtualThreadsForBlockingTasks() {
        BlockingBenchmarkConfig config = new BlockingBenchmarkConfig(
                200,
                Duration.ofMillis(200),
                20
        );

        BenchmarkComparison comparison = VirtualThreadBenchmarks.compareBlockingTasks(config);

        System.out.println("--- Grundlagen-Test: Blocking Sleep ---");
        System.out.println(comparison.prettyPrint());

        assertEquals(config.taskCount(), comparison.fixedPoolResult().taskCount());
        assertEquals(config.taskCount(), comparison.virtualThreadResult().taskCount());
        assertTrue(comparison.fixedPoolResult().peakActiveTasks() <= config.platformThreadCount());
        assertTrue(comparison.virtualThreadResult().peakActiveTasks() > comparison.fixedPoolResult().peakActiveTasks());
    }

    @Test
    @DisplayName("Grundlage: Ressourcen-Pool (Semaphore) begrenzt den Durchsatz")
    void comparesFixedPoolWithVirtualThreadsWhenAResourcePoolLimitsThroughput() {
        LimitedResourceBenchmarkConfig config = new LimitedResourceBenchmarkConfig(
                200,
                Duration.ofMillis(100),
                20,
                10,
                Duration.ofMillis(50),
                Duration.ZERO,
                0L,
                0
        );

        BenchmarkComparison comparison = VirtualThreadBenchmarks.compareLimitedResourceTasks(config);

        System.out.println("--- Grundlagen-Test: Semaphore-Begrenzung ---");
        System.out.println(comparison.prettyPrint());

        assertEquals(config.taskCount(), comparison.fixedPoolResult().taskCount());
        assertEquals(config.taskCount(), comparison.virtualThreadResult().taskCount());
        assertTrue(comparison.fixedPoolResult().peakPermitsInUse() <= config.resourcePermits());
        assertTrue(comparison.virtualThreadResult().peakPermitsInUse() <= config.resourcePermits());
    }

    @Test
    @DisplayName("Effekt: Resilienz und niedrige Latenz unter Last (Threads > DB-Verbindungen)")
    void demonstratesEffectWhenThreadsExceedConnections() {
        // Hier zeigen wir, warum VTs in der Praxis so wertvoll sind:
        // Ein Health-Check (schnell) muss nicht warten, nur weil die DB (langsam) gerade voll ist.
        
        MixedWorkloadBenchmarkConfig config = new MixedWorkloadBenchmarkConfig(
                200,                    // 200 DB-Anfragen insgesamt
                20,                     // 20 schnelle Anfragen (Health-Checks)
                Duration.ofMillis(1000),// DB-Anfrage braucht 1s
                Duration.ofMillis(10),  // Health-Check braucht 10ms
                50,                     // Server hat 50 Threads (mehr als die 10 DB-Verbindungen!)
                10                      // Datenbank erlaubt nur 10 gleichzeitige Verbindungen
        );

        System.out.println("--- Der 'Wow'-Effekt: Resilienz-Test ---");
        VirtualThreadBenchmarks.printMixedWorkloadComparison(config);

        // Die VTs gewinnen hier massiv bei der Latenz der schnellen Anfragen.
    }
}
