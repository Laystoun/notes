package com.example.notes.utils.media

import android.graphics.Bitmap
import android.net.Uri

data class MediaItem(    // Только для видео/фото
    val uri: Uri,
    val name: String = "false",
    val duration: Long = 0,
    val isVideo: Boolean = false,
    val isAudio: Boolean = false,
    val isPhoto: Boolean = false,
    val thumbnail: Bitmap? = null,
    val dateAdded: Long
)