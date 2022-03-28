package com.storm.comminicate

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

//fun main() = runBlocking {
//    repeat(100) { // 启动大量的协程
//        launch {
//            delay(5000L)
//            print(".")
//        }
//    }
//}


fun main() {
    repeat(100) {
        Thread(Runnable {
            TimeUnit.SECONDS.sleep(5)
            print(".")
        }).start()
    }
}