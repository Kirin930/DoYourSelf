package com.example.doyourself.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.doyourself.R
import com.example.doyourself.ui.theme.MainGreen
import com.example.doyourself.ui.theme.LightGray

// Declare your custom font family once.
val MyCustomFontFamily = FontFamily(
    Font(R.font.redhatdisplay_variablefont_wght, FontWeight.Normal),
    Font(R.font.redhatdisplay_bold, FontWeight.Bold),
    Font(R.font.redhatdisplay_extrabold, FontWeight.ExtraBold)
)

// Define your typography using the custom font family.
val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = MyCustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    // We'll use labelLarge for button text styles.
    labelLarge = TextStyle(
        fontFamily = MyCustomFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp
    )
)

// Warm light color scheme using warm oranges and ambers.
private val WarmLightColorScheme = lightColorScheme(
    primary = Color(0xFFEF6C00),    // Deep Orange
    onPrimary = Color.White,
    secondary = Color(0xFFFFA726),  // Orange Accent
    onSecondary = Color.Black,
    tertiary = Color(0xFFFF7043),   // Warm Coral
    onTertiary = Color.White,
    background = Color(0xFFFFF3E0),   // Light Cream
    onBackground = Color.Black,
    surface = Color(0xFFFFECB3),    // Light Amber
    onSurface = Color.Black,
)

// Warm dark color scheme for dark mode.
private val WarmDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB74D),
    onPrimary = Color.Black,
    secondary = Color(0xFFFFA726),
    onSecondary = Color.Black,
    tertiary = Color(0xFFFF7043),
    onTertiary = Color.Black,
    background = Color(0xFF424242),
    onBackground = Color.White,
    surface = Color(0xFF616161),
    onSurface = Color.White,
)

// Light color scheme based on the requested palette.
private val LightColorScheme = lightColorScheme(
    primary = MainGreen,
    onPrimary = LightGray,
    secondary = MainGreen,
    onSecondary = LightGray,
    tertiary = MainGreen,
    onTertiary = LightGray,
    background = LightGray,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
)

// Dark color scheme adjusted for readability.
private val DarkColorScheme = darkColorScheme(
    primary = MainGreen,
    onPrimary = LightGray,
    secondary = MainGreen,
    onSecondary = LightGray,
    tertiary = MainGreen,
    onTertiary = LightGray,
    background = Color(0xFF121212),
    onBackground = LightGray,
    surface = Color(0xFF1E1E1E),
    onSurface = LightGray,
)

@Composable
fun DoYourSelfTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Disabling dynamic color for consistency with our custom palette.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
