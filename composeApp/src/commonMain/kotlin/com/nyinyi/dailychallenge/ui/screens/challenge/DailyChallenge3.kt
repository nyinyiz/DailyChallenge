package com.nyinyi.dailychallenge.ui.screens.challenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

/*
🗣️ This grid layout looks great on your device, but you’ve been tasked to ensure that it looks great on many form factors, like a tablet.
 Are you confident that this code will do just that? If not, then what would you change about it to make it truly adaptive?*/

@Composable
fun DailyChallenge3(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Column {
            Text(
                "Sample Items",
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                modifier = Modifier.padding(bottom = 8.dp),
            )

            val shoes =
                remember {
                    listOf(
                        "Shoe 1",
                        "Shoe 2",
                        "Shoe 3",
                        "Shoe 4",
                        "Shoe 5",
                        "Shoe 6",
                        "Shoe 7",
                        "Shoe 8",
                        "Shoe 9",
                        "Shoe 10",
                        "Shoe 11",
                        "Shoe 12",
                    )
                }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(16.dp),
            ) {
                items(shoes) { shoe ->
                    ShopItemWithImage(shoe)
                }
            }
        }
    }
}

@Composable
fun ShopItemWithImage(shoes: String) {
    Column {
        AsyncImage(
            model = "https://picsum.photos/200/300",
            contentDescription = null,
            modifier = Modifier.fillMaxSize().height(150.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
        )
        Text(
            text = "Sample Item",
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                ),
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}
