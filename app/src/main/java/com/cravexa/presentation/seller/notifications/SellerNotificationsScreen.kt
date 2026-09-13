package com.cravexa.presentation.seller.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cravexa.R
import com.cravexa.core.designsystem.components.CravexaBadge
import com.cravexa.core.designsystem.components.CravexaBadgeType
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800

data class SellerNotification(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: String,
    val iconRes: Int,
    val isUnread: Boolean = false,
    val type: CravexaBadgeType = CravexaBadgeType.PRIMARY
)

@Composable
fun SellerNotificationsScreen(
    onBack: () -> Unit
) {
    val sampleNotifications = listOf(
        SellerNotification(
            id = "notif_1",
            title = "New Kitchen Order Received",
            description = "Order #CRV-89421 placed for 2x Grandma's Andhra Avakaya Mango Pickle. Tap to start preparation.",
            timestamp = "10 mins ago",
            iconRes = R.drawable.ic_orders,
            isUnread = true,
            type = CravexaBadgeType.WARNING
        ),
        SellerNotification(
            id = "notif_2",
            title = "Weekly Payout Transferred",
            description = "₹4,520 has been successfully transferred to your registered bank account via NEFT (Ref: CRX-BNK-98124501).",
            timestamp = "3 days ago",
            iconRes = R.drawable.ic_earnings,
            isUnread = false,
            type = CravexaBadgeType.SUCCESS
        ),
        SellerNotification(
            id = "notif_3",
            title = "Low Stock Alert",
            description = "Traditional Sun-Dried Gongura Thokku is running low (4 units remaining). Update your stock to prevent missed orders.",
            timestamp = "4 days ago",
            iconRes = R.drawable.ic_inventory,
            isUnread = false,
            type = CravexaBadgeType.FOOD_TAG
        ),
        SellerNotification(
            id = "notif_4",
            title = "FSSAI Application Update",
            description = "Your 14-digit FSSAI Registration number was submitted to CRAVEXA Food Safety Compliance team for review.",
            timestamp = "1 week ago",
            iconRes = R.drawable.ic_fssai,
            isUnread = false,
            type = CravexaBadgeType.PRIMARY
        )
    )

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = "Kitchen Alerts & Updates",
                showBackButton = true,
                onBackClick = onBack
            )
        },
        containerColor = Color(0xFFF9F9FB)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleNotifications) { notif ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (notif.isUnread) Color(0xFFFFF8E1) else Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CravexaPurple800.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = notif.iconRes),
                                contentDescription = null,
                                tint = CravexaPurple800,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = notif.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )

                                if (notif.isUnread) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(CravexaOrange500)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = notif.description,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF555555),
                                    lineHeight = 18.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = notif.timestamp,
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                            )
                        }
                    }
                }
            }
        }
    }
}

