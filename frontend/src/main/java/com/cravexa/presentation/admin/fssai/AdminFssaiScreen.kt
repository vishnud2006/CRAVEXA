package com.cravexa.presentation.admin.fssai

import androidx.compose.foundation.background
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
import com.cravexa.domain.model.AdminSeller
import com.cravexa.domain.model.FssaiStatus

@Composable
fun AdminFssaiScreen(
    viewModel: AdminFssaiViewModel = hiltViewModel()
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
            is AdminFssaiUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CravexaPurple800)
                }
            }
            is AdminFssaiUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is AdminFssaiUiState.Success -> {
                AdminFssaiContent(
                    state = state,
                    onStatusFilterSelected = viewModel::onStatusFilterSelected,
                    onVerifyFssai = viewModel::verifyFssai,
                    onRejectFssai = viewModel::rejectFssai,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun AdminFssaiContent(
    state: AdminFssaiUiState.Success,
    onStatusFilterSelected: (FssaiStatus?) -> Unit,
    onVerifyFssai: (String) -> Unit,
    onRejectFssai: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Compliance Info Card
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFE8F5E9),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "FSSAI Food Safety Verification Desk",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    )
                    Text(
                        text = "Ensure valid 14-digit registration license and matching business entity address.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF388E3C), fontSize = 11.sp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Status Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = state.selectedFssaiStatus == null,
                    onClick = { onStatusFilterSelected(null) },
                    label = { Text("All Submissions") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedFssaiStatus == FssaiStatus.SUBMITTED,
                    onClick = { onStatusFilterSelected(FssaiStatus.SUBMITTED) },
                    label = { Text("Submitted / Review Queue") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedFssaiStatus == FssaiStatus.VERIFIED,
                    onClick = { onStatusFilterSelected(FssaiStatus.VERIFIED) },
                    label = { Text("Verified") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedFssaiStatus == FssaiStatus.REJECTED,
                    onClick = { onStatusFilterSelected(FssaiStatus.REJECTED) },
                    label = { Text("Rejected") }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (state.filteredSellers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No FSSAI certification records found.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(state.filteredSellers, key = { it.id }) { seller ->
                    AdminFssaiCard(
                        seller = seller,
                        onVerify = { onVerifyFssai(seller.id) },
                        onReject = { onRejectFssai(seller.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminFssaiCard(
    seller: AdminSeller,
    onVerify: () -> Unit,
    onReject: () -> Unit
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
                        .background(Color(0xFFEDE7F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = CravexaPurple800,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = seller.businessName,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1E1E2E)
                    )
                    Text(
                        text = "FSSAI No: ${seller.fssaiNumber ?: "Not Provided"}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = CravexaPurple800)
                    )
                    Text(
                        text = "Location: ${seller.location}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                    )
                }

                // Verification Status Badge
                val (color, bg) = when (seller.fssaiStatus) {
                    FssaiStatus.VERIFIED -> Pair(Color(0xFF2E7D32), Color(0xFFE8F5E9))
                    FssaiStatus.SUBMITTED, FssaiStatus.PENDING -> Pair(Color(0xFFE65100), Color(0xFFFFF8E1))
                    FssaiStatus.REJECTED -> Pair(Color(0xFFC62828), Color(0xFFFFEBEE))
                    else -> Pair(Color.Gray, Color(0xFFF3F4F6))
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = bg
                ) {
                    Text(
                        text = seller.fssaiStatus.displayTitle,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = color),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            if (seller.fssaiStatus == FssaiStatus.SUBMITTED || seller.fssaiStatus == FssaiStatus.PENDING) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFF3F4F6))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onReject) {
                        Text("Reject FSSAI", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onVerify,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("Verify & Approve", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
