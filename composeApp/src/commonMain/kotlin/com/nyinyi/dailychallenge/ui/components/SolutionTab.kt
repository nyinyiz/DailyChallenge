package com.nyinyi.dailychallenge.ui.components

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
        // Solution view toggle
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                ),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SolutionViewToggleButton(
                    title = "Solution Code",
                    isSelected = currentView == SolutionView.CODE,
                    onClick = { currentView = SolutionView.CODE },
                    modifier = Modifier.weight(1f),
                )
                SolutionViewToggleButton(
                    title = "Design Output",
                    isSelected = currentView == SolutionView.DESIGN_OUTPUT,
                    onClick = { currentView = SolutionView.DESIGN_OUTPUT },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (currentView) {
            SolutionView.DESIGN_OUTPUT -> DesignOutputView(question)
            SolutionView.CODE -> CodeSolutionView(question)
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
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
            ),
        modifier = modifier.padding(4.dp),
    ) {
        Text(
            text = title,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color =
                if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                },
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun DesignOutputView(question: DailyChallengeObj) {
    Column(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        Text(
            "Interactive Demo",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            shape = RoundedCornerShape(16.dp),
            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 8.dp,
                ),
        ) {
            when (question.id) {
                "1" -> DailyChallenge1(modifier = Modifier.fillMaxSize())
//                "2" -> DailyChallenge2(modifier = Modifier.fillMaxSize())
                "3" -> DailyChallenge3(modifier = Modifier.fillMaxSize())
                // Add more cases as more challenges are added
                else ->
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "Interactive demo for Challenge ${question.id} is not available yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
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
                .verticalScroll(rememberScrollState()),
    ) {
        Text(
            "Solution Code",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (question.answerCode.isNotBlank()) {
            CodeBlock(
                code = question.answerCode,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Solution code for Challenge ${question.id} is not available yet.",
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
