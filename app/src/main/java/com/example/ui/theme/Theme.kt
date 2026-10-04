package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = BotanicalMintDark,
    onPrimary = BotanicalDeepNight,
    primaryContainer = BotanicalSageDarkContainer,
    onPrimaryContainer = BotanicalSage,
    secondary = BotanicalMint,
    onSecondary = BotanicalDeepNight,
    secondaryContainer = BotanicalSurfaceVariantDark,
    onSecondaryContainer = BotanicalPaleMint,
    tertiary = BotanicalAmberDark,
    onTertiary = BotanicalDeepNight,
    tertiaryContainer = Color(0xFF452200),
    onTertiaryContainer = BotanicalAmberLight,
    error = BotanicalRedDark,
    errorContainer = BotanicalRedDarkContainer,
    background = BotanicalDeepForestDark,
    onBackground = BotanicalOnDark,
    surface = BotanicalSurfaceDark,
    onSurface = BotanicalOnDark,
    surfaceVariant = BotanicalSurfaceVariantDark,
    onSurfaceVariant = BotanicalMintDark
  )

private val LightColorScheme =
  lightColorScheme(
    primary = BotanicalForestGreen,
    onPrimary = Color.White,
    primaryContainer = BotanicalSage,
    onPrimaryContainer = BotanicalDeepNight,
    secondary = BotanicalLushGreen,
    onSecondary = Color.White,
    secondaryContainer = BotanicalPaleMint,
    onSecondaryContainer = BotanicalForestGreen,
    tertiary = BotanicalAmber,
    onTertiary = Color.White,
    tertiaryContainer = BotanicalAmberLight,
    onTertiaryContainer = Color(0xFF573300),
    error = BotanicalErrorRed,
    errorContainer = BotanicalErrorContainer,
    background = BotanicalBackgroundLight,
    onBackground = BotanicalDeepNight,
    surface = BotanicalSurfaceLight,
    onSurface = BotanicalDeepNight,
    surfaceVariant = BotanicalSurfaceVariantLight,
    onSurfaceVariant = BotanicalOnSurfaceVariantLight
  )

@Composable
fun BotanicalAssistantTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our handcrafted botanical scheme for consistency
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
