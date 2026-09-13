package com.cravexa.presentation.auth.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.R
import com.cravexa.core.designsystem.components.CravexaBadge
import com.cravexa.core.designsystem.components.CravexaBadgeType
import com.cravexa.core.designsystem.components.CravexaButton
import com.cravexa.core.designsystem.components.CravexaButtonStyle
import com.cravexa.core.designsystem.components.CravexaCard
import com.cravexa.core.designsystem.components.CravexaChip
import com.cravexa.core.designsystem.components.CravexaLoadingIndicator
import com.cravexa.core.designsystem.components.CravexaTextField
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.theme.CravexaOrange50
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple100
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.domain.model.UserRole

@Composable
fun ProfileSetupScreen(
    onFinish: (UserRole) -> Unit,
    viewModel: ProfileSetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val role by viewModel.role.collectAsState()
    val businessName by viewModel.businessName.collectAsState()
    val foodCategory by viewModel.foodCategory.collectAsState()
    val address by viewModel.address.collectAsState()

    val nameError by viewModel.nameError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val phoneError by viewModel.phoneError.collectAsState()
    val businessNameError by viewModel.businessNameError.collectAsState()
    val addressError by viewModel.addressError.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val isSaving = uiState is ProfileSetupUiState.Saving
    val isSeller = role == UserRole.SELLER

    val categories = listOf(
        "Handmade Pickles & Chutneys",
        "Traditional Sweets & Mithai",
        "Fresh Stone-ground Spices",
        "Artisanal Snacks & Namkeen",
        "Home Bakery & Cakes",
        "Regional Specialty Dishes"
    )

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is ProfileSetupUiState.Success -> {
                onFinish(state.user.role)
            }
            is ProfileSetupUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetError()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = if (isSeller) "Creator Profile Setup" else "Profile Setup"
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (uiState is ProfileSetupUiState.Loading) {
            CravexaLoadingIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Avatar Placeholder
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(if (isSeller) CravexaOrange50 else CravexaPurple100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = if (isSeller) R.drawable.ic_store else R.drawable.ic_person),
                        contentDescription = "Avatar",
                        tint = if (isSeller) CravexaOrange500 else CravexaPurple800,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                CravexaBadge(
                    text = if (isSeller) "FOOD CREATOR ACCOUNT" else "CUSTOMER ACCOUNT",
                    type = if (isSeller) CravexaBadgeType.FOOD_TAG else CravexaBadgeType.PRIMARY
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isSeller) "Set Up Your Home Kitchen" else "Complete Your Profile",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isSeller) {
                        "Provide your culinary business details to start selling authentic foods"
                    } else {
                        "Help us personalize your authentic food experience"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Full Name Input
                CravexaTextField(
                    value = name,
                    onValueChange = viewModel::onNameChanged,
                    label = "Your Full Name",
                    placeholder = "e.g. Meera Krishnan",
                    errorMessage = nameError,
                    enabled = !isSaving,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Email Input
                CravexaTextField(
                    value = email,
                    onValueChange = viewModel::onEmailChanged,
                    label = "Email Address",
                    placeholder = "name@example.com",
                    errorMessage = emailError,
                    enabled = !isSaving,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Mobile Number Input
                CravexaTextField(
                    value = phone,
                    onValueChange = viewModel::onPhoneChanged,
                    label = "Mobile Number",
                    placeholder = "9876543210",
                    errorMessage = phoneError,
                    enabled = !isSaving,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = if (isSeller) ImeAction.Next else ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) },
                        onDone = {
                            if (!isSeller) {
                                focusManager.clearFocus()
                                viewModel.saveProfile()
                            }
                        }
                    )
                )

                if (isSeller) {
                    Spacer(modifier = Modifier.height(20.dp))

                    // Seller Business / Kitchen Name
                    CravexaTextField(
                        value = businessName,
                        onValueChange = viewModel::onBusinessNameChanged,
                        label = "Kitchen or Brand Name",
                        placeholder = "e.g. Grandma's Heritage Pickles",
                        errorMessage = businessNameError,
                        enabled = !isSaving,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Food Category Selector
                    Text(
                        text = "Primary Food Specialty",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { category ->
                            CravexaChip(
                                text = category,
                                selected = foodCategory == category,
                                onClick = { viewModel.onFoodCategoryChanged(category) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Kitchen Address
                    CravexaTextField(
                        value = address,
                        onValueChange = viewModel::onAddressChanged,
                        label = "Kitchen Location / Address",
                        placeholder = "e.g. 14, 5th Cross, Malleshwaram, Bengaluru",
                        errorMessage = addressError,
                        enabled = !isSaving,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                viewModel.saveProfile()
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Seller Verification Note
                    CravexaCard(
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = CravexaOrange50,
                        contentPadding = 14.dp
                    ) {
                        Text(
                            text = "ℹ️ Seller Verification Notice",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = CravexaPurple800
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Your seller profile will be submitted in 'Pending' status. CRAVEXA operations will verify your kitchen and FSSAI details before listing your delicacies.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Save & Continue Button
                CravexaButton(
                    text = "Save & Continue",
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.saveProfile()
                    },
                    isLoading = isSaving,
                    enabled = !isSaving,
                    style = CravexaButtonStyle.PRIMARY
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
