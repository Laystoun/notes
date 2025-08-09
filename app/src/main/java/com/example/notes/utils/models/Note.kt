package com.example.notes.utils.models

import android.graphics.Bitmap
import android.net.Uri
import java.util.UUID

data class Note(
    val id: UUID = UUID.randomUUID(),
    val mediaUri: String,
    val isVideo: Boolean = false,
    val isAudio: Boolean = false,
    val isPhoto: Boolean = false,
    val signature: String = "",
    val createdAt: Long = System.currentTimeMillis()
)