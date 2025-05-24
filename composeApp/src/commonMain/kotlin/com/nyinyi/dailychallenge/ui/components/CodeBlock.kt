package com.nyinyi.dailychallenge.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CodeBlock(
    code: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
            ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = code,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp,
                ),
            modifier = Modifier.padding(16.dp),
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
        )
    }
}
