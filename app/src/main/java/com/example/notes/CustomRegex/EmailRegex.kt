package com.example.notes.CustomRegex

fun emailIsValid(email: String): Boolean {
    return email.trim().contains(Regex("^[a-zA-Zа-яА-Я0-9._%+-]+@[a-zA-Zа-яА-Я0-9]+.[a-zA-Zа-яА-Я0-9]{2,}\$"))
}