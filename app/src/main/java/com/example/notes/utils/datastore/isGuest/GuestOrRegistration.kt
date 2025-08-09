package com.example.notes.utils.datastore.isGuest

data class GuestOrRegistration(
    val isGuest: Boolean = true,
    val showChoice: Boolean = true
)