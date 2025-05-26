package com.nyinyi.dailychallenge.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun UnderConstructionScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "gear_rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "rotation_animation",
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "scale_animation",
    )

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Build,
                contentDescription = "Construction Icon",
                modifier =
                    Modifier
                        .size(120.dp)
                        .rotate(rotation)
                        .graphicsLayer(scaleX = scale, scaleY = scale),
                tint = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Under Construction",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Working hard to bring you something amazing!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )

            Spacer(modifier = Modifier.height(32.dp))

            LinearProgressIndicator(
                modifier =
                    Modifier
                        .width(200.dp)
                        .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Coming Soon",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier =
                    Modifier.graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                    ),
            )
        }
    }
}
