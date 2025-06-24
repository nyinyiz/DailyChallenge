package com.nyinyi.dailychallenge.ui.screens.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nyinyi.dailychallenge.data.model.UserProfile

@Composable
fun ProfileEditCard(
    userProfile: UserProfile,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
) {
    var name by remember { mutableStateOf(userProfile.name) }
    var email by remember { mutableStateOf(userProfile.email) }
    var originalName by remember { mutableStateOf(userProfile.name) }
    var originalEmail by remember { mutableStateOf(userProfile.email) }

    val hasChanges = name != originalName || email != originalEmail

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
        ) {
            NameTextField(
                value = name,
                onValueChange = { name = it },
            )

            Spacer(modifier = Modifier.height(20.dp))

            EmailTextField(
                value = email,
                onValueChange = { email = it },
            )

            if (hasChanges) {
                Spacer(modifier = Modifier.height(20.dp))
                ActionButtons(
                    onCancel = {
                        name = originalName
                        email = originalEmail
                    },
                    onConfirm = {
                        onNameChanged(name)
                        onEmailChanged(email)
                        originalName = name
                        originalEmail = email
                    },
                )
            }
        }
    }
}
