package com.nyinyi.dailychallenge.ui.screens.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.data.model.getDailyChallengeList
import com.nyinyi.dailychallenge.ui.theme.ThemeColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsList(
    onClick: (DailyChallengeObj) -> Unit = {},
    onToggleTheme: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Daily Challenges",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        // Show moon/sun icon based on theme
                        Text(
                            text = if (ThemeColors.isDarkTheme) "☀️" else "🌙",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        var dailyChallenge by remember { mutableStateOf<List<DailyChallengeObj>>(emptyList()) }
        val listState = rememberLazyListState()

        LaunchedEffect(Unit) {
            dailyChallenge = getDailyChallengeList()
        }

        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            ) {
                // Add extra padding at the top
                item { Spacer(modifier = Modifier.height(8.dp)) }

                itemsIndexed(dailyChallenge) { index, challenge ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                                scaleIn(
                                    initialScale = 0.92f,
                                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                                ),
                        exit = fadeOut()
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            onClick = {
                                onClick(challenge)
                            }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AssistChip(
                                        onClick = { },
                                        label = { Text("Challenge ${challenge.id}") },
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    challenge.question,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                // Add some padding at the bottom
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
