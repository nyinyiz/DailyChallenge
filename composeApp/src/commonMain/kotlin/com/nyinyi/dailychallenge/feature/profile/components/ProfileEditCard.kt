package com.nyinyi.dailychallenge.feature.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.feature.profile.ProfileEditorState

@Composable
fun ProfileEditCard(
    editorState: ProfileEditorState,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Personal details",
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(8.dp))

        NameTextField(
            value = editorState.name,
            onValueChange = onNameChanged,
        )

        Spacer(modifier = Modifier.height(20.dp))

        EmailTextField(
            value = editorState.email,
            onValueChange = onEmailChanged,
        )

        if (editorState.isDirty) {
            Spacer(modifier = Modifier.height(20.dp))
            ActionButtons(
                onCancel = onCancel,
                onConfirm = onConfirm,
                confirmLabel = "Save Changes",
            )
        }
    }
}
