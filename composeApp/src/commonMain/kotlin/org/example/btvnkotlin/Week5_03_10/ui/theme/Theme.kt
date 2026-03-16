package org.example.btvnkotlin.Week5_03_10.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import btvnkotlin.composeapp.generated.resources.*
import org.jetbrains.compose.resources.Font

// 1. COLORS
val md_theme_light_primary = Color(0xFF5D7B6F)
val md_theme_light_onPrimary = Color(0xFFA4C3A2)
val md_theme_light_primaryContainer = Color(0xFFB0D4B8)
val md_theme_light_surface = Color(0xFFEAE7D6)
val md_theme_light_onSurface = Color(0xFFD7F9FA)

// 2. SHAPES (Bo góc)
val WoofShapes = Shapes(
    small = RoundedCornerShape(50.dp),
    medium = RoundedCornerShape(bottomStart = 16.dp, topEnd = 16.dp)
)

// 3. THEME BUNDLE
@Composable
fun WoofTheme(content: @Composable () -> Unit) {
    val AbrilFatface = FontFamily(
        Font(Res.font.abril_fatface_regular)
    )
    val Montserrat = FontFamily(
        Font(Res.font.montserrat_regular),
        Font(Res.font.montserrat_bold, FontWeight.Bold)
    )

    val WoofTypography = Typography(
        displayLarge = TextStyle(
            fontFamily = AbrilFatface,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp
        ),
        displayMedium = TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        labelSmall = TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
    )

    val colors = lightColorScheme(
        primary = md_theme_light_primary,
        onPrimary = md_theme_light_onPrimary,
        primaryContainer = md_theme_light_primaryContainer,
        surface = md_theme_light_surface,
        onSurface = md_theme_light_onSurface
    )

    MaterialTheme(
        colorScheme = colors,
        shapes = WoofShapes,
        typography = WoofTypography, // Áp dụng Typography tùy chỉnh
        content = content
    )
}