package com.cravexa.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cravexa.R
import com.cravexa.core.designsystem.theme.CravexaOrange100
import com.cravexa.core.designsystem.theme.CravexaOrange800
import com.cravexa.core.designsystem.theme.CravexaPurple100
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.designsystem.theme.CravexaSuccess
import com.cravexa.core.designsystem.theme.CravexaSuccessContainer
import com.cravexa.core.designsystem.theme.ShapeFoodTag

enum class CravexaBadgeType {
    FOOD_TAG,
    PRIMARY,
    SUCCESS,
    WARNING,
    RATING
}

@Composable
fun CravexaBadge(
    text: String,
    modifier: Modifier = Modifier,
    type: CravexaBadgeType = CravexaBadgeType.FOOD_TAG,
    icon: ImageVector? = null
) {
    val (backgroundColor, textColor) = when (type) {
        CravexaBadgeType.FOOD_TAG -> CravexaOrange100 to CravexaOrange800
        CravexaBadgeType.PRIMARY -> CravexaPurple100 to CravexaPurple800
        CravexaBadgeType.SUCCESS -> CravexaSuccessContainer to CravexaSuccess
        CravexaBadgeType.WARNING -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.error
        CravexaBadgeType.RATING -> Color(0xFFFFF8E1) to Color(0xFFE65100)
    }

    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = ShapeFoodTag)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (type == CravexaBadgeType.RATING) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            } else if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                ),
                color = textColor
            )
        }
    }
}

