package com.example.notes.Screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.notes.R
import com.example.notes.utils.composableSaver.MyViewModel
import com.example.notes.utils.media.MediaItem
import com.example.notes.utils.media.loadThumbnail
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun FloatScreen(
    innerPadding: PaddingValues,
    onClose: () -> Unit,
    selectedUri: SnapshotStateList<MediaItem>,
    allUri: SnapshotStateList<MediaItem>,
    signatureState: MutableState<String>,
    onRefresh: () -> Unit,
    onCreateNote: () -> Unit
) {
    val localViewModel: MyViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current  // Получаем контекст ( какой композобл/Activity,  т.д. )
    val config = LocalConfiguration.current // Получаем конфигурации телефона ( экран, плотность и т.д )
    val modalState = rememberModalBottomSheetState()
    val widthDpLocal = config.screenWidthDp.dp
    val spaceThumbnail = 4.dp   // Отступы от медиа
    val adaptiveThumbnail = (widthDpLocal - spaceThumbnail * 4) / 3 // Ровно распределяем пространство между видео/фото
    var currentVideo by remember { mutableStateOf<Uri?>(null) } // Какое видео сейчас воспроизводить?
    val isSelected = remember { derivedStateOf { selectedUri.isNotEmpty() } }   // Выбрано ли вообще что то из медиа
    val isScrollEnabled by remember { derivedStateOf { selectedUri.isEmpty() } }
    val pagerState = rememberPagerState { 2 }
    var isClickRefresh by remember { mutableStateOf(false) }

    val visibilityArrowAnim = animateFloatAsState(      // animation appearance arrow
        targetValue = if (isSelected.value) 1f else 0f,
        animationSpec = tween(350), label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        LaunchedEffect(modalState) {
            modalState.expand()
        }

        ModalBottomSheet(
            onDismissRequest = { onClose() },
            sheetState = modalState,
            containerColor = Color(0xFF1A1A1A),
            dragHandle = {
                val refreshRotate by animateFloatAsState(
                    targetValue = if (isClickRefresh) 360f else 0f,
                    animationSpec = tween(500), label = ""
                )

                LaunchedEffect(refreshRotate) {
                    if (refreshRotate == 360f) isClickRefresh = false
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .width(35.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.Gray)
                    )

                    IconButton(
                        onClick = {
                            isClickRefresh = true
                            onRefresh()
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .graphicsLayer {
                                rotationZ = refreshRotate
                            }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.refresh_reverse),
                            contentDescription = "refresh media",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                @Composable
                fun PhotoAndVideoPage() {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 100.dp)
                    ) {
                        items(allUri.size) { index ->
                            val uri = allUri[index].uri
                            val isVideo = allUri[index].isVideo
                            if (isVideo) {
                                Box(
                                    modifier = Modifier
                                        .size(adaptiveThumbnail)
                                        .padding(spaceThumbnail)
                                        .background(Color.Transparent)
                                        .clickable {
                                            currentVideo = uri
                                        }
                                ) {

                                    VideoThumbnail(uri, adaptiveThumbnail = adaptiveThumbnail)

                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(
                                                start = 5.dp,
                                                bottom = 5.dp
                                            )  // отступы для позиционирования
                                            .background(
                                                brush = Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color(0xFF1A1A1A),
                                                        Color.Transparent
                                                    )
                                                ),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(
                                                horizontal = 5.dp,
                                                vertical = 2.dp
                                            )
                                    ) {
                                        Text(
                                            text = allUri[index].duration.formattedTime(),
                                            color = Color.White
                                        )
                                    }

                                    IconButton(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(35.dp),
                                        onClick = {
                                            val currentMedia = allUri[index]
                                            if (selectedUri.contains(currentMedia)) {
                                                selectedUri.remove(currentMedia)
                                            } else {
                                                selectedUri.add(currentMedia)
                                                scope.launch { modalState.expand() }
                                            }
                                        }
                                    ) {
                                        val currentMedia = allUri[index]

                                        androidx.compose.animation.AnimatedVisibility(
                                            visible = selectedUri.contains(currentMedia),
                                            modifier = Modifier.align(Alignment.Center)
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    painter = painterResource(R.drawable.blue_circle_svgrepo_com_1_),
                                                    contentDescription = "if video selected",
                                                    tint = Color(0xFF00F5FF)
                                                )

                                                val indexFirst = selectedUri.indexOfFirst { it.uri == allUri[index].uri }
                                                if (indexFirst != -1) {
                                                    Text(
                                                        text = (indexFirst + 1).toString(),
                                                        color = Color.Black,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp,
                                                    )
                                                }
                                            }
                                        }

                                        androidx.compose.animation.AnimatedVisibility(
                                            visible = !selectedUri.contains(currentMedia)
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.circle),
                                                contentDescription = "if video selected",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            } else if (allUri[index].isPhoto) {
                                Box(
                                    modifier = Modifier
                                        .padding(spaceThumbnail)
                                ) {
                                    AsyncImage(
                                        model = allUri[index].uri,
                                        contentDescription = "Image from gallery",
                                        contentScale = ContentScale.FillBounds,
                                        modifier = Modifier
                                            .size(adaptiveThumbnail)
                                    )

                                    IconButton(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(35.dp),
                                        onClick = {
                                            val currentUri = allUri[index]
                                            if (selectedUri.contains(currentUri)) {
                                                selectedUri.remove(currentUri)
                                            } else {
                                                selectedUri.add(currentUri)
                                                scope.launch { modalState.expand() }
                                            }
                                        }
                                    ) {
                                        val currentMedia = allUri[index]

                                        androidx.compose.animation.AnimatedVisibility(
                                            visible = selectedUri.contains(currentMedia),
                                            modifier = Modifier.align(Alignment.Center)
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    painter = painterResource(R.drawable.blue_circle_svgrepo_com_1_),
                                                    contentDescription = "if video selected",
                                                    tint = Color(0xFF00F5FF)
                                                )

                                                val indexFirst = selectedUri.indexOfFirst { it.uri == allUri[index].uri }
                                                if (indexFirst != -1) {
                                                    Text(
                                                        text = (indexFirst + 1).toString(),
                                                        color = Color.Black,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp,
                                                    )
                                                }
                                            }
                                        }

                                        androidx.compose.animation.AnimatedVisibility(
                                            visible = !selectedUri.contains(currentMedia)
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.circle),
                                                contentDescription = "if video selected",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                @Composable
                fun AudioPage() {
                    var currentMusic by remember { mutableStateOf<Uri?>(null) } // Какую музыку щас воспроизводить?
                    val mediaPlayer = remember { MediaPlayer() }    // Медиа-плеер
                    val barHost = remember { SnackbarHostState() }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        LazyColumn {
                            items(allUri.filter { it.isAudio }) { item ->
                                AudioItem(
                                    item = item,
                                    isPlaying = currentMusic == item.uri,
                                    onPlayPauseLogic = {
                                        try {
                                            if (currentMusic == item.uri) {
                                                mediaPlayer.stop()
                                                mediaPlayer.reset()
                                                currentMusic = null
                                            } else {
                                                mediaPlayer.reset()
                                                mediaPlayer.setDataSource(
                                                    context,
                                                    item.uri
                                                )
                                                mediaPlayer.prepare()
                                                mediaPlayer.start()
                                                currentMusic = item.uri

                                                mediaPlayer.setOnCompletionListener {
                                                    currentMusic = null
                                                }
                                            }
                                        } catch (e: Exception) {
                                            scope.launch {
                                                barHost.showSnackbar(
                                                    message = "Файл повреждён."
                                                )

                                            }
                                        }
                                    },
                                    selectedUri = selectedUri
                                )
                            }
                        }

                        SnackbarHost(
                            hostState = barHost
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = isScrollEnabled
                ) { page ->

                    when (page) {
                        0 -> PhotoAndVideoPage()
                        1 -> AudioPage()
                    }
                }

                ConstraintLayout(   // Нижние иконки и стрелочка
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                ) {
                    val (bottomIcons, sendArrow, textField) = createRefs()
                    val sizeBottomIcons = 80.dp
                    val isHideBottomIcon = selectedUri.isEmpty()

                    androidx.compose.animation.AnimatedVisibility(
                        visible = selectedUri.isNotEmpty(),
                        modifier = Modifier
                            .constrainAs(sendArrow) {
                                bottom.linkTo(
                                    if (isHideBottomIcon) bottomIcons.top
                                    else textField.top
                                )
                                end.linkTo(parent.end)
                            }
                    ) {
                        Box(modifier = Modifier     // Стрелочка
                            .alpha(visibilityArrowAnim.value)
                            .size(90.dp)
                            .padding(
                                end = 20.dp,
                                bottom = 20.dp
                            )
                            .clip(CircleShape)
                            .border(
                                2.dp,
                                Color(0xFFA919FF),
                                shape = CircleShape
                            )
                            .background(Color(0xFF242424))
                            .clickable {
                                scope.launch { modalState.hide() }
                                onClose()
                                onCreateNote()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.send_02),
                                contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }

                    androidx.compose.animation.AnimatedVisibility(
                        visible = !isHideBottomIcon,
                        modifier = Modifier.constrainAs(textField) {
                            bottom.linkTo(parent.bottom)
                        }
                    ) {
                        TextField(
                            value = signatureState.value,
                            onValueChange = { newItem -> signatureState.value = newItem },
                            placeholder = {
                                Text(
                                    text = "Добавьте подпись...",
                                    fontSize = 16.sp
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    androidx.compose.animation.AnimatedVisibility(
                        visible = isHideBottomIcon,
                        modifier = Modifier.constrainAs(bottomIcons) {
                            bottom.linkTo(parent.bottom)
                        }
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(sizeBottomIcons)
                                .background(Color(0xFF292828))
                                .constrainAs(bottomIcons) {
                                    bottom.linkTo(parent.bottom)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {

                            val colorItems = listOf(
                                Color(0xFF4CAF50),
                                Color(0xFF9C27B0)
                            )

                            val iconItems = listOf(
                                R.drawable.gallery,
                                R.drawable.music_note_2
                            )

                            val textItems = listOf(
                                "Галерея",
                                "Аудио"
                            )

                            items(colorItems.size) { index ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    IconButton(
                                        modifier = Modifier
                                            .size(sizeBottomIcons / 2)
                                            .clip(CircleShape)
                                            .background(Color(0xFF1A1A1A))
                                            .border(
                                                2.dp,
                                                if (pagerState.currentPage == index) Color(
                                                    0xFFA919FF
                                                )
                                                else Color.Transparent,
                                                shape = CircleShape
                                            ),
                                        onClick = {
                                            scope.launch { pagerState.animateScrollToPage(index) }
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(iconItems[index]),
                                            contentDescription = "Icons Bottom",
                                            modifier = Modifier.size((65 / 2).dp),
                                            tint = Color.White
                                        )
                                    }

                                    Text(
                                        textItems[index],
                                        color = Color.White,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (currentVideo != null) {
        Dialog(
            onDismissRequest = { currentVideo = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {

                var thisVideo by remember { mutableStateOf<VideoView?>(null) } // Текущее видео
                var currentPosition by remember { mutableStateOf(0) }  // Текущая позиция ползунка
                var duration by remember { mutableIntStateOf(1) }

                LaunchedEffect(thisVideo) { // Если переменная thisVideo изменилась - запускается LaunchedEffect
                    while (true) {
                        if (thisVideo != null && thisVideo!!.isPlaying) {
                            currentPosition =
                                thisVideo!!.currentPosition   // Изменяем текущую позицию слайдера
                        }
                        delay(500)
                    }
                }

                AndroidView(
                    factory = { factoryContext ->
                        VideoView(factoryContext).apply {   // Позволяем настроить VideoView
                            setVideoURI(currentVideo)  // Устанавливаем URI
                            setOnPreparedListener { mediaPlayer ->
                                mediaPlayer.start()
                                duration = mediaPlayer.duration
                            }  // Запускаем mediaPlayer и видео ( парралельно )
                            setOnCompletionListener {
                                currentVideo = null
                            }    // При окончании видео удаляем его из состояния
                            thisVideo = this    // Записываем текущее видео в состояние
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                Slider(
                    value = currentPosition.toFloat(),  // Указываем текущее значение слайдеру
                    onValueChange = { newValue ->   // Обновляем значение ползунка слайдеру
                        currentPosition = newValue.toInt()
                        thisVideo!!.seekTo(newValue.toInt())    // Двигаем слайдер
                    },
                    valueRange = 0f..duration.toFloat(),    // Указываем максимальное значение
                    modifier = Modifier.align(Alignment.BottomCenter)
                )

                IconButton(onClick = {
                    currentVideo = null
                }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun AudioItem(
    item: MediaItem,
    isPlaying: Boolean,
    onPlayPauseLogic: () -> Unit,
    selectedUri: SnapshotStateList<MediaItem>
) {
    var isSelected by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isPlaying) 180f else 0f,
        animationSpec = tween(
            durationMillis = 400,
        ),
        label = "rotation icon"
    )


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(1.dp, Color(0xFFA919FF), RoundedCornerShape(16.dp))
            .clickable {
                if (selectedUri.contains(item)) selectedUri.remove(item)
                else selectedUri.add(item)
                isSelected = !isSelected
            }
            .background(
                if (isSelected) Color(0xFFB98AFF)
                else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(65.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF6650a4))
                .clickable(onClick = onPlayPauseLogic)
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = isPlaying,
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        rotationZ = rotation
                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.pause),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color.White
                )
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = !isPlaying,
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        rotationZ = rotation
                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.play),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color.White
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.8f)
            ) {
                Text(
                    item.name,
                    color = Color.White,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    item.duration.formattedTime(),
                    color = Color.White,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            AnimatedVisibility(
                visible = isSelected
            ) {
                Box() {
                    Icon(
                        painter = painterResource(R.drawable.blue_circle_svgrepo_com_1_),
                        contentDescription = "Choise",
                        tint = Color.White,
                        modifier = Modifier
                            .size(35.dp)
                            .padding(end = 5.dp)
                    )
                    val index = selectedUri.indexOfFirst { it.uri == item.uri }
                    if (index != -1) {
                        Text(
                            text = (index + 1).toString(),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(end = 5.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VideoThumbnail(uri: Uri, adaptiveThumbnail: Dp) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null, uri) {
        value = loadThumbnail(context, uri)
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = "Thumbnail for video",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(adaptiveThumbnail)
        )
    } else {
        Image(
            modifier = Modifier
                .size(adaptiveThumbnail)
                .background(Color.DarkGray),
            painter = painterResource(R.drawable.download_photo),
            contentDescription = "Thumbnail for video",
            contentScale = ContentScale.FillBounds
        )
    }
}

@SuppressLint("DefaultLocale")
fun Long.formattedTime(): String {
    if (this <= 0) return "0:00"

    val totalSecond = this / 1000
    val hour = totalSecond / 3600
    val minutes = (totalSecond % 3600) / 60
    val seconds = totalSecond % 60

    return if (hour > 0) {
        String.format("%d:%02d:%02d", hour, minutes, seconds)
    } else {
        String.format("%d:%02d", minutes, seconds)
    }
}