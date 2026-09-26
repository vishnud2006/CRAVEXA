package com.cravexa.presentation.admin.password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.core.designsystem.components.CravexaButton
import com.cravexa.core.designsystem.components.CravexaButtonStyle
import com.cravexa.core.designsystem.components.CravexaTextField
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.theme.CravexaPurple800

@Composable
fun AdminChangePasswordScreen(
    isForced: Boolean = false,
    onSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: AdminChangePasswordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentPassword by viewModel.currentPassword.collectAsState()
    val newPassword by viewModel.newPassword.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AdminChangePasswordUiState.Success -> {
                onSuccess()
            }
            is AdminChangePasswordUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = if (isForced) "Initial Security Setup" else "Change Admin Password",
                showBackButton = !isForced,
                onBackClick = onBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEDE7F6)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isForced) Icons.Default.Security else Icons.Default.Lock,
                    contentDescription = null,
                    tint = CravexaPurple800,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isForced) "Mandatory Password Rotation" else "Update Credentials",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isForced)
                    "For administrator security, initial default credentials must be rotated before accessing the CRAVEXA operations console."
                else
                    "Choose a strong, unique passphrase with at least 8 characters.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            CravexaTextField(
                value = currentPassword,
                onValueChange = viewModel::onCurrentPasswordChange,
                label = "Current Password",
                placeholder = "Enter your current password",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            CravexaTextField(
                value = newPassword,
                onValueChange = viewModel::onNewPasswordChange,
                label = "New Password (min 8 chars)",
                placeholder = "Enter new secure passphrase",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            CravexaTextField(
                value = confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = "Confirm New Password",
                placeholder = "Re-enter new passphrase",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(30.dp))

            CravexaButton(
                text = if (isForced) "Save & Enter Admin Console" else "Update Password",
                onClick = { viewModel.submitChangePassword() },
                isLoading = uiState is AdminChangePasswordUiState.Loading,
                style = CravexaButtonStyle.PRIMARY
            )
        }
    }
}
