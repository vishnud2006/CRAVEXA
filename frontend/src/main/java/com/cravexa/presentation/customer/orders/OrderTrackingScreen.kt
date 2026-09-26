package com.cravexa.presentation.customer.orders

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cravexa.R
import com.cravexa.core.designsystem.components.CravexaBadge
import com.cravexa.core.designsystem.components.CravexaBadgeType
import com.cravexa.core.designsystem.components.CravexaCard
import com.cravexa.core.designsystem.components.CravexaErrorState
import com.cravexa.core.designsystem.components.CravexaLoadingIndicator
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.designsystem.theme.CravexaSuccess
import com.cravexa.domain.model.OrderTracking
import com.cravexa.domain.model.TrackingEvent

@Composable
fun OrderTrackingScreen(
    orderId: String,
    onBack: () -> Unit,
    viewModel: OrderTrackingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(orderId) {
        viewModel.loadTracking(orderId)
    }

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = "Track Order",
                showBackButton = true,
                onBackClick = onBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        when (val state = uiState) {
            is OrderTrackingUiState.Loading -> {
                CravexaLoadingIndicator(modifier = Modifier.padding(innerPadding))
            }
            is OrderTrackingUiState.Error -> {
                CravexaErrorState(
                    message = state.message,
                    onRetry = { viewModel.loadTracking(orderId) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is OrderTrackingUiState.Success -> {
                OrderTrackingContent(
                    tracking = state.tracking,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun OrderTrackingContent(
    tracking: OrderTracking,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Status & Delivery Header Card
        CravexaCard(
            modifier = Modifier.fillMaxWidth(),
            containerColor = CravexaPurple800,
            contentPadding = 20.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Estimated Delivery",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = tracking.estimatedDelivery ?: "In Progress",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_track),
                        contentDescription = null,
                        tint = CravexaOrange500,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tracking ID: ${tracking.trackingNumber ?: "Pending"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Text(
                    text = tracking.carrierName ?: "CRAVEXA Logistics",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = CravexaOrange500
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Visual Milestone Timeline Card
        CravexaCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = 20.dp
        ) {
            Text(
                text = "Live Order Journey",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            tracking.events.forEachIndexed { index, event ->
                val isLast = index == tracking.events.size - 1
                TimelineNode(
                    event = event,
                    isLast = isLast
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun TimelineNode(
    event: TrackingEvent,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Node Indicator and Connecting Vertical Line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Node Icon / Circle
            when {
                event.isCompleted -> {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(CravexaSuccess),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check_circle),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                event.isCurrent -> {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(CravexaOrange500),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                }
            }

            // Connecting Vertical Line
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.5.dp)
                        .height(44.dp)
                        .background(
                            if (event.isCompleted) CravexaSuccess else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text Milestone Details
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = if (event.isCurrent || event.isCompleted) FontWeight.Bold else FontWeight.Medium,
                        color = when {
                            event.isCurrent -> CravexaOrange500
                            event.isCompleted -> CravexaPurple800
                            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        }
                    )
                )

                if (event.isCurrent) {
                    CravexaBadge(
                        text = "IN PROGRESS",
                        type = CravexaBadgeType.FOOD_TAG
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = event.description,
                style = MaterialTheme.typography.bodySmall,
                color = if (event.isCurrent || event.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )

            if (event.timestamp != null && (event.isCompleted || event.isCurrent)) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = event.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = CravexaOrange500
                )
            }
        }
    }
}
