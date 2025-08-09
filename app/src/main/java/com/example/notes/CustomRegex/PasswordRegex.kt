package com.example.notes.CustomRegex

fun passwordIsValid(passwd: String): Boolean {
    return passwd.contains(Regex("^(?=(.*\\d){6,})(?=.*[A-Z])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+\$"))
}