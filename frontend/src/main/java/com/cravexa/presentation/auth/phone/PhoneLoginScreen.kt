package com.cravexa.presentation.auth.phone

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.R
import com.cravexa.core.designsystem.components.CravexaButton
import com.cravexa.core.designsystem.components.CravexaButtonStyle
import com.cravexa.core.designsystem.components.CravexaTextField
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple100
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.designsystem.theme.CravexaShapes
import com.cravexa.core.navigation.Screen

@Composable
fun PhoneLoginScreen(
    onNavigate: (Screen) -> Unit,
    onBack: () -> Unit,
    viewModel: PhoneLoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val otp by viewModel.otp.collectAsState()
    val phoneError by viewModel.phoneError.collectAsState()
    val otpError by viewModel.otpError.collectAsState()
    val resendCountdown by viewModel.resendCountdown.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val isLoading = uiState is PhoneLoginUiState.Loading

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is PhoneLoginUiState.Success -> {
                if (state.user.profileCompleted) {
                    onNavigate(Screen.Home)
                } else {
                    onNavigate(Screen.ProfileSetup)
                }
            }
            is PhoneLoginUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetError()
            }
            else -> Unit
        }
    }

    val isOtpStep = uiState is PhoneLoginUiState.OtpSent || (uiState is PhoneLoginUiState.Loading && viewModel.verificationId.value.isNotBlank())

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = if (isOtpStep) "Verify OTP" else "Phone Login",
                showBackButton = true,
                onBackClick = {
                    if (isOtpStep) {
                        viewModel.resetToPhoneInput()
                    } else {
                        onBack()
                    }
                }
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
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isOtpStep) {
                // Step 1: Enter Phone Number
                Image(
                    painter = painterResource(id = R.drawable.ic_phone),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Mobile Verification",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "We will send a 6-digit verification code to your mobile number.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CravexaShapes.medium,
                        color = CravexaPurple100,
                        modifier = Modifier.height(56.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🇮🇳 +91",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = CravexaPurple800
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    CravexaTextField(
                        value = phone,
                        onValueChange = viewModel::onPhoneChanged,
                        label = "Mobile Number",
                        placeholder = "9876543210",
                        errorMessage = phoneError,
                        enabled = !isLoading,
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                viewModel.sendOtp()
                            }
                        )
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                CravexaButton(
                    text = "Send OTP",
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.sendOtp()
                    },
                    isLoading = isLoading,
                    enabled = !isLoading,
                    style = CravexaButtonStyle.PRIMARY
                )
            } else {
                // Step 2: Enter 6-digit OTP
                Image(
                    painter = painterResource(id = R.drawable.ic_lock),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Enter Verification Code",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Code sent to +91 $phone ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Edit",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = CravexaOrange500,
                        modifier = Modifier.clickable { viewModel.resetToPhoneInput() }
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                CravexaTextField(
                    value = otp,
                    onValueChange = viewModel::onOtpChanged,
                    label = "6-Digit OTP Code",
                    placeholder = "123456",
                    errorMessage = otpError,
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            viewModel.verifyOtp()
                        }
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Resend Countdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (resendCountdown > 0) {
                        Text(
                            text = "Resend OTP in ${resendCountdown}s",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "Didn't receive code? ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Resend OTP",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = CravexaOrange500,
                            modifier = Modifier.clickable(enabled = !isLoading) {
                                viewModel.resendOtp()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                CravexaButton(
                    text = "Verify & Continue",
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.verifyOtp()
                    },
                    isLoading = isLoading,
                    enabled = !isLoading,
                    style = CravexaButtonStyle.PRIMARY
                )
            }
        }
    }
}

