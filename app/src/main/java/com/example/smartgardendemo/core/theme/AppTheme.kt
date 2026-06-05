package com.example.smartgardendemo.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Colores personalizados ────────────────────────────────────────────────────
object AppColors {
    // Primarios
    val Green        = Color(0xFF4CAF50)   // Sensor húmedo / relé encendido
    val Red          = Color(0xFFE53935)   // Sensor seco / alerta
    val Blue         = Color(0xFF1E88E5)   // Acento principal
    val DarkBlue     = Color(0xFF1F4E79)   // Encabezados

    // Fondos
    val Background   = Color(0xFFF5F7FA)   // Fondo claro
    val Surface      = Color(0xFFFFFFFF)   // Tarjetas
    val DarkBg       = Color(0xFF121212)   // Fondo oscuro
    val DarkSurface  = Color(0xFF1E1E2E)   // Tarjetas oscuras

    // Texto
    val TextPrimary  = Color(0xFF212121)
    val TextSecondary= Color(0xFF757575)
    val TextOnDark   = Color(0xFFFFFFFF)

    // Estados del sensor
    val Humid        = Color(0xFF4CAF50)   // Verde — húmedo
    val Dry          = Color(0xFFFF7043)   // Naranja — seco
    val RelayOn      = Color(0xFF42A5F5)   // Azul — relé encendido
    val RelayOff     = Color(0xFFBDBDBD)   // Gris — relé apagado
}

// ── Esquema de colores claro ──────────────────────────────────────────────────
private val LightColorScheme = lightColorScheme(
    primary          = AppColors.Blue,
    onPrimary        = Color.White,
    secondary        = AppColors.Green,
    onSecondary      = Color.White,
    background       = AppColors.Background,
    onBackground     = AppColors.TextPrimary,
    surface          = AppColors.Surface,
    onSurface        = AppColors.TextPrimary,
    error            = AppColors.Red,
    onError          = Color.White
)

// ── Esquema de colores oscuro ─────────────────────────────────────────────────
private val DarkColorScheme = darkColorScheme(
    primary          = AppColors.Blue,
    onPrimary        = Color.White,
    secondary        = AppColors.Green,
    onSecondary      = Color.White,
    background       = AppColors.DarkBg,
    onBackground     = AppColors.TextOnDark,
    surface          = AppColors.DarkSurface,
    onSurface        = AppColors.TextOnDark,
    error            = AppColors.Red,
    onError          = Color.White
)

// ── Tema principal de la app ──────────────────────────────────────────────────
@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content     = content
    )
}