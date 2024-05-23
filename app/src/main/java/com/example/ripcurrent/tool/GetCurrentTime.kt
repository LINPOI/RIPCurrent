package com.example.ripcurrent.tool

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class GetCurrentTime(
    val timeFormatter1: String= CurrentTime(),
    val timeFormatter2: String= CurrentTime2()
)
fun CurrentTime(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return current.format(formatter)
}
fun CurrentTime2(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    return current.format(formatter)
}