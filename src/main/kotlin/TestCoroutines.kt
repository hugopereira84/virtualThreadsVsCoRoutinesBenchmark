package org.example

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

fun main() = runBlocking {
    val warmUpThreads_part1 = 1000 // Number of threads for warm-up
    val warmUpThreads_part2 = 10_000 // Number of threads for warm-up
    val warmUpThreads_part3 = 100_000 // Number of threads for warm-up
    val numTasks = 400_000

    // Warm-up phase, part 1
    println("Warming up the JVM, Part 1...")
    runCoroutines(warmUpThreads_part1)

    println("Warming up the JVM, Part 2...")
    runCoroutines(warmUpThreads_part2)

    println("Warming up the JVM, Part 3...")
    runCoroutines(warmUpThreads_part3)

    println("Running the actual test...")
    runCoroutines(numTasks)
}

fun runCoroutines(numTasks: Int) = runBlocking {
    val latch = CompletableDeferred<Unit>()
    val mutex = Mutex()
    var count = numTasks

    val startTime = System.currentTimeMillis()

    val jobs = List(numTasks) {
        launch {
            delay(5000L)
            mutex.withLock {
                count--
                if (count == 0) {
                    latch.complete(Unit)
                }
            }
        }
    }

    jobs.forEach { it.join() } // Wait for all tasks to complete

    val endTime = System.currentTimeMillis()
    println("All tasks are completed.")
    println("Total running time: ${endTime - startTime}ms")
}