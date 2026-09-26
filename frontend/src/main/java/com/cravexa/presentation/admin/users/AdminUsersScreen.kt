package com.cravexa.presentation.admin.users

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.domain.model.AdminUser
import com.cravexa.domain.model.AdminUserStatus
import com.cravexa.domain.model.UserRole

@Composable
fun AdminUsersScreen(
    viewModel: AdminUsersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearToast()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF9FAFB)
    ) { innerPadding ->
        when (val state = uiState) {
            is AdminUsersUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CravexaPurple800)
                }
            }
            is AdminUsersUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is AdminUsersUiState.Success -> {
                AdminUsersContent(
                    state = state,
                    onSearchQueryChanged = viewModel::onSearchQueryChanged,
                    onRoleFilterSelected = viewModel::onRoleFilterSelected,
                    onStatusFilterSelected = viewModel::onStatusFilterSelected,
                    onToggleUserStatus = viewModel::toggleUserStatus,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun AdminUsersContent(
    state: AdminUsersUiState.Success,
    onSearchQueryChanged: (String) -> Unit,
    onRoleFilterSelected: (UserRole?) -> Unit,
    onStatusFilterSelected: (AdminUserStatus?) -> Unit,
    onToggleUserStatus: (AdminUser) -> Unit,
    modifier: Modifier = Modifier
) {
    var userToConfirmAction by remember { mutableStateOf<AdminUser?>(null) }

    if (userToConfirmAction != null) {
        val user = userToConfirmAction!!
        val isSuspending = user.status == AdminUserStatus.ACTIVE
        AlertDialog(
            onDismissRequest = { userToConfirmAction = null },
            title = {
                Text(
                    text = if (isSuspending) "Suspend User?" else "Reactivate User?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Are you sure you want to ${if (isSuspending) "suspend" else "reactivate"} account for ${user.name} (${user.email})?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onToggleUserStatus(user)
                        userToConfirmAction = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSuspending) Color(0xFFD32F2F) else Color(0xFF2E7D32)
                    )
                ) {
                    Text(if (isSuspending) "Suspend" else "Reactivate", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { userToConfirmAction = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search TextField
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = onSearchQueryChanged,
            placeholder = { Text("Search by name, email, phone...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (state.searchQuery.isNotBlank()) {
                    IconButton(onClick = { onSearchQueryChanged("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = CravexaPurple800
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Role & Status Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = state.selectedRoleFilter == null,
                    onClick = { onRoleFilterSelected(null) },
                    label = { Text("All Users (${state.users.size})") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedRoleFilter == UserRole.CUSTOMER,
                    onClick = { onRoleFilterSelected(UserRole.CUSTOMER) },
                    label = { Text("Customers") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedRoleFilter == UserRole.SELLER,
                    onClick = { onRoleFilterSelected(UserRole.SELLER) },
                    label = { Text("Creators (Sellers)") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatusFilter == AdminUserStatus.ACTIVE,
                    onClick = {
                        onStatusFilterSelected(if (state.selectedStatusFilter == AdminUserStatus.ACTIVE) null else AdminUserStatus.ACTIVE)
                    },
                    label = { Text("Active Only") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatusFilter == AdminUserStatus.SUSPENDED,
                    onClick = {
                        onStatusFilterSelected(if (state.selectedStatusFilter == AdminUserStatus.SUSPENDED) null else AdminUserStatus.SUSPENDED)
                    },
                    label = { Text("Suspended") }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Users List
        if (state.filteredUsers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No matching users found.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(state.filteredUsers, key = { it.id }) { user ->
                    AdminUserCard(
                        user = user,
                        onActionClick = { userToConfirmAction = user }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminUserCard(
    user: AdminUser,
    onActionClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            if (user.role == UserRole.SELLER) CravexaOrange500.copy(alpha = 0.15f)
                            else CravexaPurple800.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (user.role == UserRole.SELLER) Icons.Default.Store else Icons.Default.Person,
                        contentDescription = null,
                        tint = if (user.role == UserRole.SELLER) CravexaOrange500 else CravexaPurple800,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF1E1E2E)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // Role badge
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (user.role == UserRole.SELLER) CravexaOrange500.copy(alpha = 0.12f) else Color(0xFFEDE7F6)
                        ) {
                            Text(
                                text = if (user.role == UserRole.SELLER) "Creator" else "Customer",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                                color = if (user.role == UserRole.SELLER) CravexaOrange500 else CravexaPurple800,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${user.email} • ${user.phone}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF6B7280), fontSize = 11.sp)
                    )
                }

                // Status Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (user.status == AdminUserStatus.ACTIVE) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                ) {
                    Text(
                        text = user.status.displayTitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (user.status == AdminUserStatus.ACTIVE) Color(0xFF2E7D32) else Color(0xFFC62828)
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Joined: ${user.createdAt} • ${user.totalOrders} Orders",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                )

                TextButton(
                    onClick = onActionClick,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (user.status == AdminUserStatus.ACTIVE) "Suspend" else "Reactivate",
                        color = if (user.status == AdminUserStatus.ACTIVE) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
