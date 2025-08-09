package com.example.notes.utils.media

import android.Manifest
import android.os.Build

fun getMediaPermission(): List<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}