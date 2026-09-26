package com.cravexa.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = CravexaPurple800,
    onPrimary = Color.White,
    primaryContainer = CravexaPurple100,
    onPrimaryContainer = CravexaPurple900,
    secondary = CravexaOrange600,
    onSecondary = Color.White,
    secondaryContainer = CravexaOrange100,
    onSecondaryContainer = CravexaOrange900,
    tertiary = CravexaGold500,
    onTertiary = Color.White,
    tertiaryContainer = CravexaGold100,
    onTertiaryContainer = CravexaGold600,
    background = CravexaBackgroundLight,
    onBackground = CravexaTextPrimaryLight,
    surface = CravexaSurfaceLight,
    onSurface = CravexaTextPrimaryLight,
    surfaceVariant = CravexaSurfaceVariant,
    onSurfaceVariant = CravexaTextSecondaryLight,
    outline = CravexaDividerLight,
    error = CravexaError,
    onError = Color.White,
    errorContainer = CravexaErrorContainer,
    onErrorContainer = CravexaError
)

private val DarkColorScheme = darkColorScheme(
    primary = CravexaPurple200,
    onPrimary = CravexaPurple900,
    primaryContainer = CravexaPurple700,
    onPrimaryContainer = CravexaPurple100,
    secondary = CravexaOrange300,
    onSecondary = CravexaOrange900,
    secondaryContainer = CravexaOrange800,
    onSecondaryContainer = CravexaOrange100,
    tertiary = CravexaGold500,
    onTertiary = Color.Black,
    tertiaryContainer = CravexaGold600,
    onTertiaryContainer = CravexaGold100,
    background = CravexaBackgroundDark,
    onBackground = CravexaTextPrimaryDark,
    surface = CravexaSurfaceDark,
    onSurface = CravexaTextPrimaryDark,
    surfaceVariant = CravexaSurfaceDarkVar,
    onSurfaceVariant = CravexaTextSecondaryDark,
    outline = CravexaDividerDark,
    error = CravexaError,
    onError = Color.White,
    errorContainer = CravexaErrorContainer,
    onErrorContainer = CravexaError
)

@Composable
fun CravexaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    val customColors = CravexaCustomColors(
        brandPurple = if (darkTheme) CravexaPurple300 else CravexaPurple800,
        brandOrange = if (darkTheme) CravexaOrange400 else CravexaOrange500,
        brandGold = if (darkTheme) CravexaGold500 else CravexaGold600,
        foodTagBackground = if (darkTheme) CravexaPurple700 else CravexaOrange50,
        foodTagText = if (darkTheme) CravexaOrange300 else CravexaOrange800,
        shimmerHighlight = if (darkTheme) Color(0xFF352B42) else Color(0xFFF0EBF5),
        starRating = Color(0xFFFFB300),
        badgeSuccess = CravexaSuccess,
        badgeWarning = CravexaWarning
    )

    CompositionLocalProvider(
        LocalCravexaColors provides customColors,
        LocalCravexaSpacing provides CravexaSpacing(),
        LocalCravexaElevation provides CravexaElevation()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = CravexaTypography,
            shapes = CravexaShapes,
            content = content
        )
    }
}

object CravexaTheme {
    val colors: CravexaCustomColors
        @Composable
        @ReadOnlyComposable
        get() = LocalCravexaColors.current

    val spacing: CravexaSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalCravexaSpacing.current

    val elevation: CravexaElevation
        @Composable
        @ReadOnlyComposable
        get() = LocalCravexaElevation.current
}

