package com.nyinyi.dailychallenge.ui.screens.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.ui.screens.profile.utils.QuestionDisplay
import com.nyinyi.dailychallenge.ui.screens.profile.utils.parseQuestionForDisplay

@Composable
fun FailedQuestionItem(
    question: String,
    questionType: String,
) {
    var showReportDialog by remember { mutableStateOf(false) }
    val questionDisplay =
        remember(question, questionType) {
            parseQuestionForDisplay(question, questionType)
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f),
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
        ) {
            QuestionDisplayContent(questionDisplay)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                OutlinedButton(
                    onClick = { showReportDialog = true },
                    modifier = Modifier.padding(top = 4.dp),
                ) {
                    Text(
                        text = "Report Issue",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }

    if (showReportDialog) {
        ReportIssueDialog(
            question = question,
            questionType = questionType,
            onDismiss = { showReportDialog = false },
        )
    }
}

@Composable
private fun QuestionDisplayContent(questionDisplay: QuestionDisplay) {
    Text(
        text = questionDisplay.question,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onErrorContainer,
    )

    questionDisplay.answer?.let { answer ->
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = answer,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
        )
    }

    questionDisplay.explanation?.let { explanation ->
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = explanation,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f),
        )
    }
}
