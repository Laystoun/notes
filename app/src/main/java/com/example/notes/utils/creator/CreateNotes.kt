package com.example.notes.utils.creator

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.notes.R
import com.example.notes.Screens.VideoThumbnail
import com.example.notes.utils.media.MediaItem
import com.example.notes.utils.media.loadThumbnail
import com.example.notes.utils.models.Note
import java.util.UUID

@Composable
fun CreateFirstNote(
    obj: Note
) {
    Card(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .padding(15.dp),
        elevation = CardDefaults.cardElevation(15.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (obj.isPhoto) {
                AsyncImage(
                    model = obj.mediaUri,
                    contentDescription = "",
                    modifier = Modifier
                        .height(150.dp)
                        .widthIn(200.dp)
                        .padding(top = 10.dp)
                )

                Text(
                    obj.signature
                )
            } else if (obj.isVideo) {
                VideoThumbnailForNotes(
                    uri = Uri.parse(obj.mediaUri),
                    modifier = Modifier
                        .height(150.dp)
                        .widthIn(200.dp)
                        .padding(top = 10.dp)
                )
            }
        }
    }
}

fun isFirstNote(selectedUri: List<MediaItem>): Boolean? {
    var videoCount = 0
    var photoCount = 0

    selectedUri.forEach { item ->
        if (item.isVideo) videoCount++
        else if (item.isPhoto) photoCount++
    }

    val result = videoCount + photoCount

    return if (result == 1) {
        true
    } else if (result > 1) {
        false
    } else null
}

@Composable
fun VideoThumbnailForNotes(uri: Uri, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null, uri) {
        value = loadThumbnail(context, uri)
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = "Thumbnail video",
            modifier = modifier,
            contentScale = ContentScale.FillBounds
        )
    } else {
        Image(
            modifier = modifier,
            painter = painterResource(R.drawable.download_photo),
            contentDescription = "Thumbnail for don't download video",
            contentScale = ContentScale.FillBounds
        )
    }
}