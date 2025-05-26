package com.nyinyi.dailychallenge.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.data.model.DailyChallengeObj
import com.nyinyi.dailychallenge.ui.screens.challenge.DailyChallenge1
import com.nyinyi.dailychallenge.ui.screens.challenge.DailyChallenge3

enum class SolutionView {
    DESIGN_OUTPUT,
    CODE,
}

@Composable
fun SolutionTab(question: DailyChallengeObj) {
    var currentView by remember { mutableStateOf(SolutionView.CODE) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
        ) {
            SolutionViewToggleButton(
                title = "Code",
                isSelected = currentView == SolutionView.CODE,
                onClick = { currentView = SolutionView.CODE },
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.padding(8.dp))
            SolutionViewToggleButton(
                title = "Preview",
                isSelected = currentView == SolutionView.DESIGN_OUTPUT,
                onClick = { currentView = SolutionView.DESIGN_OUTPUT },
                modifier = Modifier.weight(1f),
            )
        }

        ElevatedCard(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            colors =
                CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
        ) {
            when (currentView) {
                SolutionView.CODE -> CodeSolutionView(question)
                SolutionView.DESIGN_OUTPUT -> DesignOutputView(question)
            }
        }
    }
}

@Composable
fun SolutionViewToggleButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .height(40.dp),
        shape = RoundedCornerShape(20.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = if (isSelected) 4.dp else 1.dp,
            ),
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                    contentColor =
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        },
                ),
            elevation = null,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            )
        }
    }
}

@Composable
fun DesignOutputView(question: DailyChallengeObj) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                text = "Solution Preview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }

        when (question.id) {
            "1" -> DailyChallenge1()
            "3" -> DailyChallenge3()
            else -> {
                Text(
                    text = "Preview not available for this challenge",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                )
            }
        }
    }
}

@Composable
fun CodeSolutionView(question: DailyChallengeObj) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
    ) {
        if (question.answerCode.isNotBlank()) {
            CodeBlock(
                code = question.answerCode,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            Text(
                text = "No solution code available for this challenge",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
            )
        }
    }
}
