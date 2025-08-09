package com.example.notes.utils.composableSaver

import android.content.Context
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.notes.utils.media.MediaItem
import com.example.notes.utils.media.loadMedia
import com.example.notes.utils.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyViewModel: ViewModel() {
    val selectedUris = mutableStateListOf<MediaItem>()
    val allUri = mutableStateListOf<MediaItem>()
    val notes = mutableStateListOf<Note>()
    var modalBottomSheet by mutableStateOf(false)
    private var isMediaLoaded by mutableStateOf(false) // Добавляем флаг загрузки

    suspend fun loadMediaIfNeeded(context: Context) {
        if (!isMediaLoaded) {
            allUri.clear()
            allUri.addAll(withContext(Dispatchers.IO) { loadMedia(context) })
            isMediaLoaded = true
        }
    }

    fun openModalBottomSheet() {
        modalBottomSheet = true
    }

    fun closeModalBottomSheet() {
        modalBottomSheet = false
    }

    fun addNote(obj: Note) {
        notes.add(obj)
    }

    fun removeNote(obj: Note) {
        notes.remove(obj)
    }
}