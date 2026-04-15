package com.example.virtual;

import java.util.*;
import java.util.concurrent.*;

public class CarrierThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        int taskCount = 100;
        CountDownLatch latch = new CountDownLatch(taskCount);
        Set<String> carrierThreads = Collections.newSetFromMap(new ConcurrentHashMap<>());

        for (int i = 0; i < taskCount; i++) {
            Thread.startVirtualThread(() -> {
                String threadName = Thread.currentThread().toString();
                // Virtual threads are typically represented as VirtualThread[#N,name]/ForkJoinPool-1-worker-1
                // We extract the carrier thread part if present
                if (threadName.contains("@")) {
                   // Some JVMs might show it differently, let's just use the name
                }
                
                // A better way to see the carrier thread is using a hack or just observing the ForkJoinPool names
                String name = Thread.currentThread().toString();
                int workerIndex = name.indexOf("worker-");
                if (workerIndex != -1) {
                    carrierThreads.add(name.substring(workerIndex));
                } else {
                    carrierThreads.add(name);
                }
                
                try {
                    // Busy wait to force carrier thread usage and prevent quick yielding
                    long start = System.nanoTime();
                    while (System.nanoTime() - start < 10_000_000) { // 10ms
                        // spin
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        System.out.println("Anzahl der verschiedenen Carrier Threads: " + carrierThreads.size());
        System.out.println("Verwendete Carrier Threads (oder VT Namen):");
        carrierThreads.stream().sorted().forEach(System.out::println);
        
        System.out.println("\nHinweis: Die Parallelität wird über -Djdk.virtualThreadScheduler.parallelism gesteuert.");
    }
}
