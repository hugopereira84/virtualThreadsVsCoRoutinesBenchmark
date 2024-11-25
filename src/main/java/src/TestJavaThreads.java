package src;

import java.util.concurrent.CountDownLatch;

class TestJavaThreads {


    public static void main(String[] args) throws InterruptedException {
        int warmUpThreads_part1 = 1000; // Number of threads for warm-up
        int warmUpThreads_part2 = 10_000; // Number of threads for warm-up
        int warmUpThreads_part3 = 100_000; // Number of threads for warm-up
        int numThreads = 400_000; // Number of threads for actual test

        // Warm-up phase, part 1
        System.out.println("Warming up the JVM, Part 1...");
        timeToRunThreads(warmUpThreads_part1);
        timeToRunVirtualThreads(warmUpThreads_part1);

        // Warm-up phase, part 2
        System.out.println("Warming up the JVM, Part 2...");
        timeToRunThreads(9000);
        timeToRunVirtualThreads(warmUpThreads_part2);

        // Warm-up phase, part 2
        System.out.println("Warming up the JVM, Part 3...");
        timeToRunVirtualThreads(warmUpThreads_part3);

        // Actual test phase
        System.out.println("Running the actual test...");
        timeToRunThreads(9100); // this will fail, need to decrease to 9100 or less to make it work
        timeToRunVirtualThreads(numThreads);
    }


    private static void timeToRunThreads(int numThreads) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(numThreads);
        long start = System.currentTimeMillis();
        for(int i =0; i < numThreads; i++) {
            startThreads(latch);
        }
        latch.await(); // wait for all threads to finish
        long end = System.currentTimeMillis();
        System.out.println("Time taken with regular threads: " + (end - start) + "ms");
    }

    private static void startThreads(CountDownLatch latch) {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown(); // decrement the count of the latch
            }
        }).start();
    }


    private static void timeToRunVirtualThreads(int numThreads) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(numThreads); // reset the latch
        long start = System.currentTimeMillis();
        for(int i =0; i < numThreads; i++) {
            startVirtualThreads(latch);
        }
        latch.await(); // wait for all virtual threads to finish
        long end = System.currentTimeMillis();
        System.out.println("Time taken with virtual threads: " + (end - start) + "ms");
    }

    private static void startVirtualThreads(CountDownLatch latch) {
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown(); // decrement the count of the latch
            }
        });
    }

}