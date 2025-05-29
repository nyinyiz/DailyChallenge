package com.nyinyi.dailychallenge.ui.screens.play.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.data.model.Category

@Composable
fun AnimatedCategoryText(
    selectedCategory: Category,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "scale",
    )

    val offsetY by animateFloatAsState(
        targetValue = 0f,
        label = "offset",
    )

    val shimmerColors =
        listOf(
            Color(0xFF4CC9F0), // Electric Blue
            Color(0xFFF72585), // Electric Pink
            Color(0xFF7209B7), // Electric Purple
            Color(0xFF3A0CA3), // Deep Blue
            Color(0xFF4CC9F0), // Electric Blue (repeating)
        )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim =
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "shimmer",
        )

    Box(
        modifier =
            Modifier
                .clip(RoundedCornerShape(8.dp))
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationY = offsetY
                },
    ) {
        Box(
            modifier =
                Modifier
                    .matchParentSize()
                    .background(
                        brush =
                            Brush.horizontalGradient(
                                colors = shimmerColors,
                                startX = translateAnim.value - 1000f,
                                endX = translateAnim.value,
                            ),
                    ).alpha(0f),
        )

        Text(
            text = selectedCategory.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier =
                Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null, // Removed rememberRipple
                        onClick = onClick,
                    ).padding(horizontal = 8.dp, vertical = 4.dp)
                    .align(Alignment.Center),
            textDecoration = TextDecoration.None,
        )
    }
}
