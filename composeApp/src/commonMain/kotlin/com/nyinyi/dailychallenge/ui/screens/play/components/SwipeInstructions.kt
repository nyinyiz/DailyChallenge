package com.nyinyi.dailychallenge.ui.screens.play.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SwipeInstructions() {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        SwipeIndicatorButton(
            text = "FALSE",
            icon = Icons.Default.Close,
            color = MaterialTheme.colorScheme.error,
        )

        SwipeIndicatorButton(
            text = "TRUE",
            icon = Icons.Default.Check,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
