package com.example.notes.Screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notes.R
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen() {
    var isGlowing by remember { mutableStateOf(false) }

    val animatedRadius by animateFloatAsState(
        targetValue = if (isGlowing) 600f else 150f,
        animationSpec = tween(durationMillis = 2000),
        label = "GlowRadius"
    )

    LaunchedEffect(Unit) {
        while (true) {
            isGlowing = !isGlowing
            delay(900)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0E0E0E),
                        Color(0xFF1A0F1F)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFA919FF).copy(alpha = 0.4f),
                            Color(0xFF1A0F1F)
                        ),
                        radius = animatedRadius,
                        center = Offset.Unspecified
                    )
                )
                .border(
                    width = 2.dp,
                    color = Color(0xFFA919FF),
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.note_logo_without_bg),
                contentDescription = "Logo"
            )
        }
    }
}