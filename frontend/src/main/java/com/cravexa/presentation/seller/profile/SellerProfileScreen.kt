package com.cravexa.presentation.seller.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.R
import com.cravexa.core.designsystem.components.CravexaBadge
import com.cravexa.core.designsystem.components.CravexaBadgeType
import com.cravexa.core.designsystem.components.CravexaButton
import com.cravexa.core.designsystem.components.CravexaButtonStyle
import com.cravexa.core.designsystem.components.CravexaCard
import com.cravexa.core.designsystem.components.CravexaTextField
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.domain.model.FssaiStatus
import com.cravexa.domain.model.SellerAccountStatus

@Composable
fun SellerProfileScreen(
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {},
    showBackButton: Boolean = false,
    viewModel: SellerProfileViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val fssaiNumber by viewModel.fssaiNumber.collectAsState()
    val fssaiError by viewModel.fssaiError.collectAsState()

    val businessName by viewModel.businessName.collectAsState()
    val about by viewModel.about.collectAsState()
    val businessAddress by viewModel.businessAddress.collectAsState()
    val city by viewModel.city.collectAsState()
    val state by viewModel.state.collectAsState()
    val pincode by viewModel.pincode.collectAsState()

    val businessNameError by viewModel.businessNameError.collectAsState()
    val addressError by viewModel.addressError.collectAsState()
    val pincodeError by viewModel.pincodeError.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val isSaving = uiState is SellerProfileUiState.Saving

    LaunchedEffect(profile) {
        profile?.let { viewModel.populateFromProfile(it) }
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is SellerProfileUiState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }
            is SellerProfileUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(text = "Log Out", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            },
            text = {
                Text(text = "Are you sure you want to sign out of your creator kitchen?", style = MaterialTheme.typography.bodyMedium)
            },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    viewModel.logout(onLogout)
                }) {
                    Text(text = "Log Out", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(text = "Cancel", color = CravexaPurple800)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = "Creator Kitchen Profile",
                showBackButton = showBackButton,
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
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Seller Header Card
            CravexaCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = CravexaPurple800,
                contentPadding = 20.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_store),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profile?.businessName?.ifBlank { "Home Kitchen" } ?: "Home Kitchen",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Creator: ${profile?.sellerName ?: "Home Chef"}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            CravexaBadge(
                                text = "CREATOR",
                                type = CravexaBadgeType.FOOD_TAG
                            )
                            val statusBadge = when (profile?.accountStatus) {
                                SellerAccountStatus.APPROVED -> CravexaBadgeType.SUCCESS
                                SellerAccountStatus.REJECTED, SellerAccountStatus.SUSPENDED -> CravexaBadgeType.WARNING
                                else -> CravexaBadgeType.PRIMARY
                            }
                            CravexaBadge(
                                text = profile?.accountStatus?.displayTitle?.uppercase() ?: "PENDING REVIEW",
                                type = statusBadge
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Verification Notice Banner
            CravexaCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentPadding = 14.dp
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_verified),
                        contentDescription = null,
                        tint = CravexaOrange500,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Creator Verification & Compliance",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "CRAVEXA performs strict safety verification before public marketplace listing. FSSAI registration ensures compliance under Indian Food Safety laws.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section 1: FSSAI Registration
            Text(
                text = "FSSAI Food License / Registration",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            CravexaCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = 16.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FSSAI Status",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    val fssaiBadge = when (profile?.fssaiStatus) {
                        FssaiStatus.VERIFIED -> CravexaBadgeType.SUCCESS
                        FssaiStatus.PENDING, FssaiStatus.SUBMITTED -> CravexaBadgeType.FOOD_TAG
                        FssaiStatus.REJECTED, FssaiStatus.EXPIRED -> CravexaBadgeType.WARNING
                        else -> CravexaBadgeType.PRIMARY
                    }

                    CravexaBadge(
                        text = profile?.fssaiStatus?.displayTitle?.uppercase() ?: "NOT PROVIDED",
                        type = fssaiBadge
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                CravexaTextField(
                    value = fssaiNumber,
                    onValueChange = viewModel::onFssaiNumberChanged,
                    label = "14-digit FSSAI License Number",
                    placeholder = "e.g. 11223344556677",
                    errorMessage = fssaiError,
                    enabled = !isSaving,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(12.dp))

                CravexaButton(
                    text = "Submit FSSAI for Verification",
                    onClick = viewModel::submitFssai,
                    isLoading = isSaving,
                    enabled = !isSaving,
                    style = CravexaButtonStyle.SECONDARY
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section 2: Business & Kitchen Details
            Text(
                text = "Kitchen & Contact Information",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            CravexaCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = 16.dp
            ) {
                CravexaTextField(
                    value = businessName,
                    onValueChange = viewModel::onBusinessNameChanged,
                    label = "Kitchen / Brand Name",
                    errorMessage = businessNameError,
                    enabled = !isSaving
                )

                Spacer(modifier = Modifier.height(12.dp))

                CravexaTextField(
                    value = about,
                    onValueChange = viewModel::onAboutChanged,
                    label = "About Your Kitchen & Heritage",
                    placeholder = "Tell customers about your family recipes and specialties...",
                    enabled = !isSaving,
                    singleLine = false,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(12.dp))

                CravexaTextField(
                    value = businessAddress,
                    onValueChange = viewModel::onAddressChanged,
                    label = "Kitchen Location / Street Address",
                    errorMessage = addressError,
                    enabled = !isSaving
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CravexaTextField(
                        value = city,
                        onValueChange = viewModel::onCityChanged,
                        label = "City",
                        enabled = !isSaving,
                        modifier = Modifier.weight(1f)
                    )

                    CravexaTextField(
                        value = pincode,
                        onValueChange = viewModel::onPincodeChanged,
                        label = "PIN Code",
                        errorMessage = pincodeError,
                        enabled = !isSaving,
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                CravexaButton(
                    text = "Save Kitchen Information",
                    onClick = viewModel::saveBusinessDetails,
                    isLoading = isSaving,
                    enabled = !isSaving,
                    style = CravexaButtonStyle.PRIMARY
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout Section
            CravexaButton(
                text = "Sign Out from Kitchen Account",
                onClick = { showLogoutDialog = true },
                style = CravexaButtonStyle.OUTLINED
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
