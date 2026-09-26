package com.cravexa.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cravexa.R
import com.cravexa.core.designsystem.theme.CravexaOrange500

enum class SellerTab(val title: String, val iconRes: Int) {
    DASHBOARD("Dashboard", R.drawable.ic_dashboard),
    ORDERS("Orders", R.drawable.ic_orders),
    PRODUCTS("Products", R.drawable.ic_inventory),
    EARNINGS("Earnings", R.drawable.ic_earnings),
    PROFILE("Profile", R.drawable.ic_store)
}

@Composable
fun SellerBottomBar(
    selectedTab: SellerTab,
    onTabSelected: (SellerTab) -> Unit,
    modifier: Modifier = Modifier,
    pendingOrdersCount: Int = 0,
    lowStockCount: Int = 0
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SellerTab.values().forEach { tab ->
                val isSelected = selectedTab == tab
                val badgeCount = when (tab) {
                    SellerTab.ORDERS -> pendingOrdersCount
                    SellerTab.PRODUCTS -> lowStockCount
                    else -> 0
                }

                SellerBottomBarItem(
                    tab = tab,
                    isSelected = isSelected,
                    badgeCount = badgeCount,
                    onClick = { onTabSelected(tab) }
                )
            }
        }
    }
}

@Composable
private fun SellerBottomBarItem(
    tab: SellerTab,
    isSelected: Boolean,
    badgeCount: Int,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .height(32.dp)
                .then(
                    if (isSelected) {
                        Modifier
                            .clip(CircleShape)
                            .background(CravexaOrange500.copy(alpha = 0.15f))
                            .padding(horizontal = 16.dp)
                    } else {
                        Modifier.padding(horizontal = 8.dp)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            BadgedBox(
                badge = {
                    if (badgeCount > 0) {
                        Badge(
                            containerColor = CravexaOrange500,
                            contentColor = Color.White
                        ) {
                            Text(text = "$badgeCount", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = tab.iconRes),
                    contentDescription = tab.title,
                    tint = if (isSelected) CravexaOrange500 else Color(0xFF6B7280),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = tab.title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) CravexaOrange500 else Color(0xFF6B7280)
            )
        )
    }
}
