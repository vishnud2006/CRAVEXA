package com.cravexa.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.domain.model.FssaiStatus
import com.cravexa.domain.model.SellerAccountStatus

@Composable
fun SellerStatusBanner(
    accountStatus: SellerAccountStatus,
    fssaiStatus: FssaiStatus,
    onCompleteFssaiClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val (accountTitle, accountDesc, accountBg, accountColor, accountIcon) = when (accountStatus) {
            SellerAccountStatus.PENDING -> Tuple5(
                "Your seller account is under review",
                "CRAVEXA operations is reviewing your kitchen profile. You can add dishes and manage inventory in the meantime.",
                Color(0xFFFFF8E1),
                Color(0xFFE65100),
                Icons.Default.Warning
            )
            SellerAccountStatus.APPROVED -> Tuple5(
                "Verified Food Creator",
                "Your kitchen is officially verified by CRAVEXA. Dishes are listed in public marketplace.",
                Color(0xFFE8F5E9),
                Color(0xFF2E7D32),
                Icons.Default.Verified
            )
            SellerAccountStatus.REJECTED -> Tuple5(
                "Account verification requires action",
                "Please check your registered email for details on completing compliance verification.",
                Color(0xFFFFEBEE),
                Color(0xFFC62828),
                Icons.Default.Warning
            )
            SellerAccountStatus.SUSPENDED -> Tuple5(
                "Account Suspended",
                "Your creator kitchen account is currently suspended. Please contact CRAVEXA support.",
                Color(0xFFFFEBEE),
                Color(0xFFC62828),
                Icons.Default.Warning
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(accountBg)
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = accountIcon,
                    contentDescription = null,
                    tint = accountColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = accountTitle,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = accountColor
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = accountDesc,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = accountColor.copy(alpha = 0.9f),
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        if (fssaiStatus != FssaiStatus.VERIFIED) {
            val fssaiText = when (fssaiStatus) {
                FssaiStatus.NOT_PROVIDED -> "Status: FSSAI Not Provided • Tap to update"
                FssaiStatus.PENDING, FssaiStatus.SUBMITTED -> "Status: FSSAI Submitted • Under review"
                FssaiStatus.REJECTED -> "Status: FSSAI Rejected • Tap to re-submit"
                FssaiStatus.EXPIRED -> "Status: FSSAI Expired • Tap to renew"
                else -> ""
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEDE7F6))
                    .clickable { onCompleteFssaiClick() }
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = CravexaPurple800,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "FSSAI Food Safety Compliance",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = CravexaPurple800
                            )
                        )
                        Text(
                            text = fssaiText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CravexaPurple800.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = CravexaPurple800,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

private data class Tuple5<A, B, C, D, E>(
    val a: A,
    val b: B,
    val c: C,
    val d: D,
    val e: E
)
