package com.nyinyi.dailychallenge.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyinyi.dailychallenge.data.model.UserProfile
import com.nyinyi.dailychallenge.ui.screens.profile.components.FailedExercisesSection
import com.nyinyi.dailychallenge.ui.screens.profile.components.ProfileEditCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreenContent(viewModel: ProfileViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (val state = uiState) {
            is ProfileUiState.Loading -> CircularProgressIndicator()
            is ProfileUiState.Error -> ErrorMessage(state.message)
            is ProfileUiState.Success ->
                ProfileContent(
                    userProfile = state.userProfile,
                    onNameChanged = viewModel::updateUserName,
                    onEmailChanged = viewModel::updateUserEmail,
                    onClearFailedExercises = viewModel::clearFailedExercises,
                )
        }
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = "Error: $message",
        color = MaterialTheme.colorScheme.error,
    )
}

@Composable
private fun ProfileContent(
    userProfile: UserProfile,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onClearFailedExercises: () -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ProfileHeader()
            ProfileEditCard(
                userProfile = userProfile,
                onNameChanged = onNameChanged,
                onEmailChanged = onEmailChanged,
            )
        }

        item {
            FailedExercisesSection(
                userProfile = userProfile,
                onClearFailedExercises = onClearFailedExercises,
            )
        }
    }
}

@Composable
private fun ProfileHeader() {
    Text(
        text = "My Profile",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        textAlign = TextAlign.Center,
    )
}
