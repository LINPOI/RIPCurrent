package com.example.ripcurrent.tool.Check

fun PasswordTextCheck (password: String): Boolean {
    val gmailPattern = Regex("^[a-zA-Z0-9.@$]*$")
    return gmailPattern.matches(password)
}