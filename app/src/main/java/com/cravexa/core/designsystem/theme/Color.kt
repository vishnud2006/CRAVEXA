package com.cravexa.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Brand Deep Purple Palette
val CravexaPurple900 = Color(0xFF1A0826)
val CravexaPurple800 = Color(0xFF2D114D)
val CravexaPurple700 = Color(0xFF3B1E54)
val CravexaPurple600 = Color(0xFF4A148C)
val CravexaPurple500 = Color(0xFF6A1B9A)
val CravexaPurple300 = Color(0xFF9C4DCC)
val CravexaPurple200 = Color(0xFFD1C4E9)
val CravexaPurple100 = Color(0xFFEDE7F6)
val CravexaPurple50  = Color(0xFFF7F3FB)

// Brand Vibrant Food Orange Palette
val CravexaOrange900 = Color(0xFFE65100)
val CravexaOrange800 = Color(0xFFEF6C00)
val CravexaOrange700 = Color(0xFFF57C00)
val CravexaOrange600 = Color(0xFFFF6F00)
val CravexaOrange500 = Color(0xFFFF7A00)
val CravexaOrange400 = Color(0xFFFFA000)
val CravexaOrange300 = Color(0xFFFFB74D)
val CravexaOrange200 = Color(0xFFFFE082)
val CravexaOrange100 = Color(0xFFFFE8D6)
val CravexaOrange50  = Color(0xFFFFF8F2)

// Warm Food Gold / Honey Accent
val CravexaGold600 = Color(0xFFD97706)
val CravexaGold500 = Color(0xFFF59E0B)
val CravexaGold100 = Color(0xFFFEF3C7)

// Neutral & Surface
val CravexaBackgroundLight = Color(0xFFFAF8F5)
val CravexaSurfaceLight    = Color(0xFFFFFFFF)
val CravexaSurfaceVariant  = Color(0xFFF3EEF8)
val CravexaCardLight       = Color(0xFFFFFFFF)

val CravexaBackgroundDark  = Color(0xFF140E1B)
val CravexaSurfaceDark     = Color(0xFF1E1627)
val CravexaSurfaceDarkVar  = Color(0xFF2A2035)
val CravexaCardDark        = Color(0xFF22192D)

// Typography & Content
val CravexaTextPrimaryLight   = Color(0xFF1C1B1F)
val CravexaTextSecondaryLight = Color(0xFF49454F)
val CravexaTextTertiaryLight  = Color(0xFF79747E)
val CravexaDividerLight       = Color(0xFFE7E0EC)

val CravexaTextPrimaryDark    = Color(0xFFE6E1E5)
val CravexaTextSecondaryDark  = Color(0xFFCAC4D0)
val CravexaTextTertiaryDark   = Color(0xFF938F99)
val CravexaDividerDark        = Color(0xFF49454F)

// Semantic Colors
val CravexaSuccess = Color(0xFF2E7D32)
val CravexaSuccessContainer = Color(0xFFE8F5E9)
val CravexaError = Color(0xFFB3261E)
val CravexaErrorContainer = Color(0xFFF9DEDC)
val CravexaWarning = Color(0xFFED6C02)
val CravexaWarningContainer = Color(0xFFFFF3E0)
val CravexaInfo = Color(0xFF0288D1)
val CravexaInfoContainer = Color(0xFFE1F5FE)

@Immutable
data class CravexaCustomColors(
    val brandPurple: Color,
    val brandOrange: Color,
    val brandGold: Color,
    val foodTagBackground: Color,
    val foodTagText: Color,
    val shimmerHighlight: Color,
    val starRating: Color,
    val badgeSuccess: Color,
    val badgeWarning: Color
)

val LocalCravexaColors = staticCompositionLocalOf {
    CravexaCustomColors(
        brandPurple = CravexaPurple800,
        brandOrange = CravexaOrange500,
        brandGold = CravexaGold500,
        foodTagBackground = CravexaOrange50,
        foodTagText = CravexaOrange800,
        shimmerHighlight = Color(0xFFF0EBF5),
        starRating = Color(0xFFFFB300),
        badgeSuccess = CravexaSuccess,
        badgeWarning = CravexaWarning
    )
}

