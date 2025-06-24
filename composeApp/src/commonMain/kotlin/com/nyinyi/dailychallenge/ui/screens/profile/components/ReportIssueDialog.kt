package com.nyinyi.dailychallenge.ui.screens.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private const val GITHUB_REPO_URL = "https://github.com/nyinyiz/DailyChallenge"

@Composable
fun ReportIssueDialog(
    question: String,
    questionType: String,
    onDismiss: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Report Issue") },
        text = {
            Column {
                Text("To report an issue with this question:")
                Spacer(modifier = Modifier.height(8.dp))

                ReportInstructions()

                Spacer(modifier = Modifier.height(8.dp))

                QuestionInfoCard(
                    question = question,
                    questionType = questionType,
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text("Thank you for helping improve the app!")
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    uriHandler.openUri(GITHUB_REPO_URL)
                    onDismiss()
                },
            ) {
                Text("Go to GitHub")
            }
        },
    )
}

@Composable
private fun ReportInstructions() {
    listOf(
        "1. Click the 'Go to GitHub' button below to visit:",
        "2. Go to the Issues tab",
        "3. Click 'New Issue'",
        "4. Include the following information:",
    ).forEach { instruction ->
        Text(instruction)
    }

    Text(
        GITHUB_REPO_URL,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
    )
}

@Composable
private fun QuestionInfoCard(
    question: String,
    questionType: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
        ) {
            Text(
                text = "Question: $question",
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Question Type: $questionType",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
