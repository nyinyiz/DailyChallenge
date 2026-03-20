package com.nyinyi.dailychallenge.feature.play.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeColors
import com.nyinyi.dailychallenge.ui.theme.DailyChallengeSpacing

@Composable
fun QuizStateScreen(
    message: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: ImageVector? = null,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    actionLabel: String? = null,
    actionIcon: ImageVector? = null,
    onAction: (() -> Unit)? = null,
    showLoadingIndicator: Boolean = false,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(DailyChallengeSpacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            when {
                showLoadingIndicator -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(DailyChallengeSpacing.xLarge * 2),
                    )
                }
                icon != null -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(DailyChallengeSpacing.xLarge * 2),
                    )
                }
            }

            Spacer(modifier = Modifier.height(DailyChallengeSpacing.large))

            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(DailyChallengeSpacing.small))
            }

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color =
                    if (iconTint == MaterialTheme.colorScheme.error) {
                        DailyChallengeColors.danger
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
            )

            if (actionLabel != null && onAction != null) {
                Spacer(modifier = Modifier.height(DailyChallengeSpacing.xLarge))
                Button(
                    onClick = onAction,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = DailyChallengeColors.success,
                        ),
                ) {
                    if (actionIcon != null) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.width(DailyChallengeSpacing.small))
                    }
                    Text(
                        text = actionLabel,
                    )
                }
            }
        }
    }
}
