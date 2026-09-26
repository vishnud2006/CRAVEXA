package com.cravexa.presentation.admin.complaints

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
import com.cravexa.domain.model.AdminComplaint
import com.cravexa.domain.model.ComplaintStatus

@Composable
fun AdminComplaintsScreen(
    viewModel: AdminComplaintsViewModel = hiltViewModel()
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
            is AdminComplaintsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = CravexaPurple800)
                }
            }
            is AdminComplaintsUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is AdminComplaintsUiState.Success -> {
                AdminComplaintsContent(
                    state = state,
                    onStatusFilterSelected = viewModel::onStatusFilterSelected,
                    onUpdateComplaint = viewModel::updateComplaint,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun AdminComplaintsContent(
    state: AdminComplaintsUiState.Success,
    onStatusFilterSelected: (ComplaintStatus?) -> Unit,
    onUpdateComplaint: (String, ComplaintStatus, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedComplaintToManage by remember { mutableStateOf<AdminComplaint?>(null) }

    if (selectedComplaintToManage != null) {
        val ticket = selectedComplaintToManage!!
        var internalNotes by remember { mutableStateOf(ticket.internalNotes) }
        var chosenStatus by remember { mutableStateOf(ticket.status) }

        AlertDialog(
            onDismissRequest = { selectedComplaintToManage = null },
            title = { Text("Dispute Ticket • ${ticket.ticketNumber}", fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Category: ${ticket.category.displayTitle}", fontWeight = FontWeight.Bold, color = CravexaOrange500)
                    Text("Parties: ${ticket.customerName} vs ${ticket.sellerName}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Customer Issue:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    Text(ticket.description, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF374151)))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Internal Admin Notes:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    OutlinedTextField(
                        value = internalNotes,
                        onValueChange = { internalNotes = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Add resolution notes here...") }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Update Status:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChip(
                            selected = chosenStatus == ComplaintStatus.OPEN,
                            onClick = { chosenStatus = ComplaintStatus.OPEN },
                            label = { Text("Open", fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = chosenStatus == ComplaintStatus.IN_REVIEW,
                            onClick = { chosenStatus = ComplaintStatus.IN_REVIEW },
                            label = { Text("In Review", fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = chosenStatus == ComplaintStatus.RESOLVED,
                            onClick = { chosenStatus = ComplaintStatus.RESOLVED },
                            label = { Text("Resolved", fontSize = 11.sp) }
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateComplaint(ticket.id, chosenStatus, internalNotes)
                        selectedComplaintToManage = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CravexaPurple800)
                ) {
                    Text("Save Resolution", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedComplaintToManage = null }) { Text("Cancel") }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = state.selectedStatusFilter == null,
                    onClick = { onStatusFilterSelected(null) },
                    label = { Text("All Tickets (${state.complaints.size})") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatusFilter == ComplaintStatus.OPEN,
                    onClick = { onStatusFilterSelected(ComplaintStatus.OPEN) },
                    label = { Text("Open Tickets") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatusFilter == ComplaintStatus.IN_REVIEW,
                    onClick = { onStatusFilterSelected(ComplaintStatus.IN_REVIEW) },
                    label = { Text("Under Review") }
                )
            }
            item {
                FilterChip(
                    selected = state.selectedStatusFilter == ComplaintStatus.RESOLVED,
                    onClick = { onStatusFilterSelected(ComplaintStatus.RESOLVED) },
                    label = { Text("Resolved") }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (state.filteredComplaints.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("No complaints found.", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
                items(state.filteredComplaints, key = { it.id }) { complaint ->
                    AdminComplaintCard(
                        complaint = complaint,
                        onClick = { selectedComplaintToManage = complaint }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminComplaintCard(
    complaint: AdminComplaint,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
                        .background(Color(0xFFFFEBEE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SupportAgent,
                        contentDescription = null,
                        tint = Color(0xFFC62828),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${complaint.ticketNumber} • ${complaint.category.displayTitle}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1E1E2E)
                    )
                    Text(
                        text = "${complaint.customerName} → ${complaint.sellerName}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF6B7280), fontSize = 11.sp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (complaint.status == ComplaintStatus.RESOLVED) Color(0xFFE8F5E9) else Color(0xFFFFF8E1)
                ) {
                    Text(
                        text = complaint.status.displayTitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (complaint.status == ComplaintStatus.RESOLVED) Color(0xFF2E7D32) else Color(0xFFE65100)
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = complaint.description,
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF374151))
            )
            if (complaint.internalNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Admin Note: ${complaint.internalNotes}",
                    style = MaterialTheme.typography.bodySmall.copy(color = CravexaPurple800, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}
