package com.nyinyi.dailychallenge.ui.screens.challenge

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/*🗣️ You want to show a FloatingActionButton (FAB) only after scrolling past the first few items.
You've got access to scroll state — now you need to connect that to visibility.
How would you do it? Feel free to share code snippets!*/

@Composable
fun DailyChallenge1(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        val listState = rememberLazyListState()

        // Show FAB only after user has scrolled past the first 3 items
        val showFab by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 2
            }
        }

        Column {
            Text(
                "Sample Items",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(50) { index ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                index < 3 -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            }
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Item ${index + 1}${if (index < 3) " (scroll past me)" else ""}",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (index == 49) {
                        Spacer(modifier = Modifier.height(80.dp)) // Extra space at bottom for FAB
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showFab,
            enter = fadeIn(animationSpec = tween(300)) +
                    slideInVertically(animationSpec = tween(300)) { it },
            exit = fadeOut(animationSpec = tween(300)) +
                    slideOutVertically(animationSpec = tween(300)) { it },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = { /* Handle FAB click */ },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Text("↑", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
