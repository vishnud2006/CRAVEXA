package com.cravexa.presentation.customer.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.R
import com.cravexa.core.designsystem.components.CravexaBadge
import com.cravexa.core.designsystem.components.CravexaBadgeType
import com.cravexa.core.designsystem.components.CravexaCard
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.theme.CravexaPurple100
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.designsystem.theme.CravexaShapes
import com.cravexa.core.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun CustomerProfileScreen(
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit,
    viewModel: CustomerProfileViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Log Out",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to log out of CRAVEXA?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout(onLogout)
                    }
                ) {
                    Text(
                        text = "Log Out",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
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
                title = "My Account"
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
            // Profile Card Header
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
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profile?.name?.ifBlank { "Food Lover" } ?: "Food Lover",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = profile?.email?.ifBlank { "No email set" } ?: "No email set",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        )
                        if (!profile?.phone.isNullOrBlank()) {
                            Text(
                                text = "+91 ${profile?.phone}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        CravexaBadge(
                            text = "MEMBER SINCE AUG 2026",
                            type = CravexaBadgeType.FOOD_TAG
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Account & Preferences Section
            Text(
                text = "Account & Settings",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            CravexaCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = 0.dp
            ) {
                ProfileMenuItem(
                    icon = painterResource(id = R.drawable.ic_person),
                    title = "Edit Profile",
                    subtitle = "Name, phone number and details",
                    onClick = { onNavigate(Screen.EditProfile) }
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                ProfileMenuItem(
                    icon = painterResource(id = R.drawable.ic_location),
                    title = "Delivery Addresses",
                    subtitle = "Manage saved home and work addresses",
                    onClick = { onNavigate(Screen.AddressList) }
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                ProfileMenuItem(
                    icon = painterResource(id = R.drawable.ic_orders),
                    title = "My Orders & Tracking",
                    subtitle = "View active and past food orders",
                    onClick = { onNavigate(Screen.Orders) }
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                ProfileMenuItem(
                    icon = painterResource(id = R.drawable.ic_heart),
                    title = "Wishlist & Delicacies",
                    subtitle = "Saved home recipes and creator kitchens",
                    onClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Wishlist will be enabled in Phase 4 (Marketplace).")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Help & Legal Section
            Text(
                text = "More Options",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            CravexaCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = 0.dp
            ) {
                ProfileMenuItem(
                    icon = painterResource(id = R.drawable.ic_star),
                    title = "Help & Support",
                    subtitle = "Customer care and safety guidelines",
                    onClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("CRAVEXA Support: support@cravexa.com")
                        }
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                ProfileMenuItem(
                    icon = painterResource(id = R.drawable.ic_lock),
                    title = "Privacy Policy & Terms",
                    subtitle = "Read our terms of service and data policy",
                    onClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("CRAVEXA Terms & Privacy policy v1.0")
                        }
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                ProfileMenuItem(
                    icon = painterResource(id = R.drawable.ic_refresh),
                    title = "Log Out",
                    subtitle = "Sign out from this device",
                    isDestructive = true,
                    onClick = { showLogoutDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: Painter,
    title: String,
    subtitle: String,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CravexaShapes.small)
                .background(if (isDestructive) MaterialTheme.colorScheme.errorContainer else CravexaPurple100),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = if (isDestructive) MaterialTheme.colorScheme.error else CravexaPurple800,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(16.dp)
        )
    }
}
