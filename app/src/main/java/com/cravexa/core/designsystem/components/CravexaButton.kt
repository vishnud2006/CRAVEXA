package com.cravexa.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cravexa.core.designsystem.theme.CravexaOrange500
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.core.designsystem.theme.CravexaShapes

enum class CravexaButtonStyle {
    PRIMARY,
    SECONDARY,
    OUTLINED,
    TEXT
}

@Composable
fun CravexaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: CravexaButtonStyle = CravexaButtonStyle.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    height: Dp = 50.dp,
    fullWidth: Boolean = true,
    containerColor: Color? = null,
    contentColor: Color? = null
) {
    val buttonModifier = if (fullWidth) modifier.fillMaxWidth().height(height) else modifier.height(height)

    when (style) {
        CravexaButtonStyle.PRIMARY -> {
            Button(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled && !isLoading,
                shape = CravexaShapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor ?: CravexaPurple800,
                    contentColor = contentColor ?: Color.White,
                    disabledContainerColor = CravexaPurple800.copy(alpha = 0.4f),
                    disabledContentColor = Color.White.copy(alpha = 0.6f)
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                ButtonContent(text, isLoading, leadingIcon, trailingIcon, contentColor ?: Color.White)
            }
        }

        CravexaButtonStyle.SECONDARY -> {
            Button(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled && !isLoading,
                shape = CravexaShapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor ?: CravexaOrange500,
                    contentColor = contentColor ?: Color.White,
                    disabledContainerColor = CravexaOrange500.copy(alpha = 0.4f),
                    disabledContentColor = Color.White.copy(alpha = 0.6f)
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                ButtonContent(text, isLoading, leadingIcon, trailingIcon, contentColor ?: Color.White)
            }
        }

        CravexaButtonStyle.OUTLINED -> {
            val strokeColor = containerColor ?: CravexaPurple800
            OutlinedButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled && !isLoading,
                shape = CravexaShapes.medium,
                border = BorderStroke(1.5.dp, if (enabled) strokeColor else strokeColor.copy(alpha = 0.3f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = contentColor ?: CravexaPurple800,
                    disabledContentColor = CravexaPurple800.copy(alpha = 0.4f)
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                ButtonContent(text, isLoading, leadingIcon, trailingIcon, contentColor ?: CravexaPurple800)
            }
        }

        CravexaButtonStyle.TEXT -> {
            TextButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled && !isLoading,
                shape = CravexaShapes.medium,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = contentColor ?: CravexaPurple800,
                    disabledContentColor = CravexaPurple800.copy(alpha = 0.4f)
                )
            ) {
                ButtonContent(text, isLoading, leadingIcon, trailingIcon, contentColor ?: CravexaPurple800)
            }
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    isLoading: Boolean,
    leadingIcon: ImageVector?,
    trailingIcon: ImageVector?,
    textColor: Color
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = textColor,
                strokeWidth = 2.5.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    androidx.compose.material3.Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = textColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    color = textColor
                )
                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    androidx.compose.material3.Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = textColor
                    )
                }
            }
        }
    }
}

