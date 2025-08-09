package com.example.notes.utils.media

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import com.example.notes.Screens.formattedTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun loadMedia(context: Context): List<MediaItem> = withContext(Dispatchers.IO) {
    val mediaUri = mutableListOf<MediaItem>()
    val projectionVideoPhoto = arrayOf(
        MediaStore.MediaColumns._ID, // Вытягиваем айдишники media
        MediaStore.MediaColumns.DATE_ADDED,
        MediaStore.MediaColumns.DURATION
    )
    val projectionAudio = arrayOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.ARTIST,
        MediaStore.MediaColumns.DISPLAY_NAME,
        MediaStore.MediaColumns.DURATION,
        MediaStore.MediaColumns.DATE_ADDED
    )
    val imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val videoCollection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    val audioCollection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    val audioCursor = context.contentResolver.query(
        audioCollection,
        projectionAudio,
        null,
        null,
        "${MediaStore.Audio.Media.DATE_ADDED} DESC"
    )

    val photoCursor = context.contentResolver.query(
        imageCollection, // Указываем курсору откуда будем считывать данные ( EXTERNAL_CONTENT_URI указывает на хранилище всех фотографий )
        projectionVideoPhoto, // Указываем данные для вытягивания из бд
        null,
        null,
        "${MediaStore.Images.Media.DATE_ADDED} DESC" // По убыванию ( от начала последней фото )
    )

    val videoCursor = context.contentResolver.query(
        videoCollection,
        projectionVideoPhoto,
        null,
        null,
        "${MediaStore.Video.Media.DATE_ADDED} DESC"
    )

    photoCursor?.use { cursor ->
        val columnId = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)    // Вытаскиваем айдишник из текущей фотки
        val columnAdded = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)
        while (cursor.moveToNext()) {
            val thisMediaId = cursor.getLong(columnId)  // Получаем идентификатор текущей фотки
            val dataAdded = cursor.getLong(columnAdded)
            val thisUri = ContentUris.withAppendedId(
                imageCollection,
                thisMediaId
            )    // Формируем рабочий URI для ссылки на фото
            mediaUri.add(
                MediaItem(
                    uri = thisUri,
                    isVideo = false,
                    dateAdded = dataAdded,
                    isPhoto = true
                )
            )
        }
    }

    videoCursor?.use { cursor ->
        val columnId = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
        val columnAdded = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)
        val columnDuration = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION)
        while (cursor.moveToNext()) {
            val thisMediaId = cursor.getLong(columnId)
            val dateAdded = cursor.getLong(columnAdded)
            val duration = cursor.getLong(columnDuration)
            val thisUri = ContentUris.withAppendedId(videoCollection, thisMediaId)

            mediaUri.add(
                MediaItem(
                    uri = thisUri,
                    isVideo = true,
                    dateAdded = dateAdded,
                    duration = duration
                )
            )
        }
    }

    audioCursor?.use { cursor ->
        val columnId = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
        val columnAdded = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)
        val musicNameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
        val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION)
        while (cursor.moveToNext()) {
            val thisMediaId = cursor.getLong(columnId)
            val dateAdded = cursor.getLong(columnAdded)
            val thisUri = ContentUris.withAppendedId(audioCollection, thisMediaId)
            val musicName = cursor.getString(musicNameColumn)
            val duration = cursor.getLong(durationColumn)
            mediaUri.add(
                MediaItem(
                    uri = thisUri,
                    name = musicName.toString(),
                    duration = duration,
                    isVideo = false,
                    isPhoto = false,
                    isAudio = true,
                    dateAdded = dateAdded,
                    thumbnail = null)
            )
        }
    }

    mediaUri.forEach {item ->
        Log.d("CHECKING", "весь список: $item")
    }

    mediaUri
}

suspend fun loadThumbnail(context: Context, uri: Uri): Bitmap? =
    withContext(Dispatchers.IO) {
        try {
            context.contentResolver.loadThumbnail(uri, Size(250, 250), null)
        } catch (e: Exception) {
            null
        }
    }