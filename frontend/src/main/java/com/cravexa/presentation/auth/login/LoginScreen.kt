package com.cravexa.presentation.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.R
import com.cravexa.core.designsystem.components.CravexaButton
import com.cravexa.core.designsystem.components.CravexaButtonStyle
import com.cravexa.core.designsystem.components.CravexaCard
import com.cravexa.core.designsystem.components.CravexaTextField
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.navigation.Screen

@Composable
fun LoginScreen(
    onNavigate: (Screen) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val isLoading = uiState is LoginUiState.Loading

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is LoginUiState.Success -> {
                if (state.profileCompleted) {
                    when (state.role) {
                        com.cravexa.domain.model.UserRole.SELLER -> onNavigate(Screen.SellerHome)
                        com.cravexa.domain.model.UserRole.ADMIN -> onNavigate(Screen.AdminMain)
                        com.cravexa.domain.model.UserRole.CUSTOMER -> onNavigate(Screen.CustomerMain)
                    }
                } else {
                    onNavigate(Screen.ProfileSetup)
                }
            }
            is LoginUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetError()
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Official CRAVEXA Logo & Branding (Neatly adjusted)
            Image(
                painter = painterResource(id = R.drawable.cravexa_logo),
                contentDescription = "CRAVEXA Logo",
                modifier = Modifier.size(76.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                ),
                color = CravexaPurple800
            )

            Text(
                text = stringResource(id = R.string.tagline),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                ),
                color = CravexaOrange500
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Header Texts
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Sign in to access your homemade favorites",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email Input
            CravexaTextField(
                value = email,
                onValueChange = viewModel::onEmailChanged,
                label = "Email Address",
                placeholder = "name@example.com",
                errorMessage = emailError,
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            CravexaTextField(
                value = password,
                onValueChange = viewModel::onPasswordChanged,
                label = "Password",
                placeholder = "Enter your password",
                isPassword = true,
                errorMessage = passwordError,
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewModel.login()
                    }
                )
            )

            // Forgot Password Link
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Forgot Password?",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = CravexaPurple800,
                    modifier = Modifier.clickable(enabled = !isLoading) {
                        onNavigate(Screen.ForgotPassword)
                    }
                )
            }

            // Login Button
            CravexaButton(
                text = "Login",
                onClick = {
                    focusManager.clearFocus()
                    viewModel.login()
                },
                isLoading = isLoading,
                enabled = !isLoading,
                style = CravexaButtonStyle.PRIMARY
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Or Continue With Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = "  OR CONTINUE WITH  ",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Social Login Buttons: Google & Phone
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CravexaCard(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        // Google sign-in triggers Google credential manager
                        viewModel.loginWithGoogle("mock_google_id_token")
                    },
                    contentPadding = 12.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Google Sign In",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Google",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                CravexaCard(
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(Screen.PhoneLogin) },
                    contentPadding = 12.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_phone),
                            contentDescription = "Phone Login",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Phone OTP",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign Up Navigation Link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = CravexaOrange500,
                    modifier = Modifier.clickable(enabled = !isLoading) {
                        onNavigate(Screen.Signup)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Admin Portal Access Link
            Text(
                text = "CRAVEXA Operations Portal",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier.clickable(enabled = !isLoading) {
                    onNavigate(Screen.AdminLogin)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
