package src;

import java.util.concurrent.CountDownLatch;

class TestJavaThreads {


    public static void main(String[] args) throws InterruptedException {
        //int numThreads = 9900;
        //
        //int numThreads = 100000;
        //int numThreads = 200000;
        int numThreads = 100_000;
        timeToRunVirtualThreads(numThreads);
        //timeToRunThreads(numThreads);
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

}