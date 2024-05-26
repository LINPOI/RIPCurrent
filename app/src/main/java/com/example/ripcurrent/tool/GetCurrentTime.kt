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
fun formatDateTime(input: String): String {
    // Define the input and output formats
    if(input.length<14){
        return input
    }
    val inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")

    // Parse the input string to LocalDateTime
    val dateTime = LocalDateTime.parse(input, inputFormatter)

    // Format the LocalDateTime to the desired output format
    return dateTime.format(outputFormatter)
}