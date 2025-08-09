package com.example.notes.Screens

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.R
import com.example.notes.utils.composableSaver.MyViewModel
import com.example.notes.utils.creator.CreateFirstNote
import com.example.notes.utils.creator.isFirstNote
import com.example.notes.utils.media.ScaffoldBottomAppBar
import com.example.notes.utils.media.getMediaPermission
import com.example.notes.utils.media.loadMedia
import com.example.notes.utils.models.Note
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Preview(showBackground = true)
@Composable
fun MainScreen() {
    val viewModelData: MyViewModel = viewModel()
    val config = LocalConfiguration.current   // Параметры экрана телефона ( подтягиваем разрешение )
    val context = LocalContext.current
    val density = LocalDensity.current  // Операции с переводом в размерности ( пиксели/dp и т.д. )
    val requiredPermissions = getMediaPermission()
    val localWidth = with (density) { config.screenWidthDp.dp.toPx() }
    val localHeight = with (density) { config.screenWidthDp.dp.toPx() }
    val selectedUri = viewModelData.selectedUris
    var isFloatStart by remember { mutableStateOf(false) }
    var stopAnimation by remember { mutableStateOf(false) }
    val openGallery = viewModelData.modalBottomSheet
    var scaffoldPadding by remember { mutableStateOf(PaddingValues()) }
    val allUri = viewModelData.allUri
    var isFirstOrAlbum by remember { mutableStateOf<Boolean?>(null) }
    var isCreateFirstNote by remember { mutableStateOf(false) }
    val signatureUnderPhoto = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val launcherPermissions =
        rememberLauncherForActivityResult(    // Создаём лаунчер для запрашивания разрешений ( не запускаем )
            contract = ActivityResultContracts.RequestMultiplePermissions() // Указываем что на входе будет массив разрешений
        ) { result ->   // Возвращает ответ в формате: permission:false(true)
            if (result.values.all { it }) {   // .all проверяет являются ли все значение в map true
                scope.launch {
                    val media = loadMedia(context)
                    allUri.clear()
                    allUri.addAll(media)
                }
            }
        }

    val isGranted = requiredPermissions.all { permission ->
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    val startX = animateFloatAsState(if (isFloatStart) localWidth else 0f, tween(1500), label = "")
    val startY = animateFloatAsState(if (isFloatStart) localHeight else 0f, tween(3000), label = "")
    val endX = animateFloatAsState(if (isFloatStart) 0f else localWidth, tween(1500), label = "")
    val endY = animateFloatAsState(if (isFloatStart) 0f else localHeight, tween(3000), label = "")

    LaunchedEffect(stopAnimation) {
        if (!stopAnimation) {
            while(true) {
                delay(3000)
                isFloatStart = !isFloatStart
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModelData.openModalBottomSheet()
                    scope.launch {
                        if (isGranted) {
                            viewModelData.loadMediaIfNeeded(context)
                        } else {
                            launcherPermissions.launch(requiredPermissions.toTypedArray())
                        }
                    }
                },
                containerColor = Color(0xFF3A3A3A),
                contentColor = Color.White
            ) {
                Icon(painter = painterResource(id = R.drawable.add), contentDescription = "Add")
            }
        },
        bottomBar = {
            ScaffoldBottomAppBar()
        }
    ) { innerPadding ->

        scaffoldPadding = innerPadding

        Column(modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF121212), Color(0xFF1E1E1E)),
                    start = Offset(startX.value, startY.value),
                    end = Offset(endX.value, endY.value)
                )
            )
            .padding(innerPadding)
            .verticalScroll(rememberScrollState(0)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            viewModelData.notes.forEach { note ->
                CreateFirstNote(obj = note)
            }

            if (isCreateFirstNote) {
                val element = selectedUri.first()
                val note = Note(
                    mediaUri = element.uri.toString(),
                    isVideo = element.isVideo,
                    isPhoto = element.isPhoto,
                    isAudio = element.isAudio,
                    signature = signatureUnderPhoto.value
                )

                LaunchedEffect(note) {
                    viewModelData.addNote(note)
                    selectedUri.clear()
                    isCreateFirstNote = false
                }
            }
        }

        if (openGallery) {
            stopAnimation = true
            FloatScreen(
                scaffoldPadding,
                onClose = {
                    viewModelData.closeModalBottomSheet()
                    selectedUri.clear()
                },
                selectedUri,
                allUri,
                signatureUnderPhoto,
                onRefresh = {
                    scope.launch {
                        val media = loadMedia(context)
                        allUri.clear()
                        allUri.addAll(media)
                    }
                },
                onCreateNote = {
                    isFirstOrAlbum = isFirstNote(selectedUri)
                    // if first card == true, if album == false, if empty == null
                    Log.d("checking", "isFirstOrAlbum $isFirstOrAlbum")
                    if (isFirstOrAlbum != null) {
                        if (isFirstOrAlbum!!) {
                            isCreateFirstNote = true
                        }
                    }
                }
            )
        } else {
            stopAnimation = false
        }
    }
}