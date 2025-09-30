package com.nyinyi.dailychallenge.ui.screens.play.matching.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchingItem(
    text: String,
    isSelected: Boolean,
    isMatched: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scale by animateFloatAsState(
        targetValue =
            when {
                isMatched -> 0.98f
                isSelected -> 1.03f
                else -> 1f
            },
        animationSpec =
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            ),
        label = "scale",
    )

    val alpha by animateFloatAsState(
        targetValue = if (isMatched) 0.7f else 1f,
        label = "alpha",
    )

    val containerColor by animateColorAsState(
        targetValue =
            when {
                isMatched -> MaterialTheme.colorScheme.primaryContainer
                isSelected -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surface
            },
        label = "containerColor",
    )

    val contentColor by animateColorAsState(
        targetValue =
            when {
                isMatched -> MaterialTheme.colorScheme.primary
                isSelected -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.onSurface
            },
        label = "contentColor",
    )

    val borderColor by animateColorAsState(
        targetValue =
            when {
                isMatched -> MaterialTheme.colorScheme.primary
                isSelected -> MaterialTheme.colorScheme.secondary
                else -> Color.Transparent
            },
        label = "borderColor",
    )

    Card(
        onClick = onClick,
        modifier =
            modifier
                .fillMaxWidth()
                .scale(scale)
                .alpha(alpha)
                .padding(4.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = containerColor,
            ),
        border =
            if (isSelected || isMatched) {
                BorderStroke(2.dp, borderColor)
            } else {
                null
            },
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = if (isSelected) 6.dp else 2.dp,
            ),
        enabled = !isMatched,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Status Icon
            Icon(
                imageVector =
                    when {
                        isMatched -> Icons.Rounded.CheckCircle
                        isSelected -> Icons.Rounded.RadioButtonUnchecked
                        else -> Icons.Rounded.RadioButtonUnchecked
                    },
                contentDescription = null,
                tint =
                    when {
                        isMatched -> MaterialTheme.colorScheme.primary
                        isSelected -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                modifier = Modifier.size(20.dp),
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight =
                    when {
                        isSelected -> FontWeight.SemiBold
                        isMatched -> FontWeight.Bold
                        else -> FontWeight.Normal
                    },
                color = contentColor,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
