package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    //val numberCoroutines = 100000
    val numberCoroutines = 200000
    val startTime = System.currentTimeMillis()

    val jobs = List(numberCoroutines) {
        launch {
            delay(5000L)
        }
    }

    jobs.forEach { it.join() }

    val endTime = System.currentTimeMillis()

    println("Total running time: ${endTime - startTime} ms")
}