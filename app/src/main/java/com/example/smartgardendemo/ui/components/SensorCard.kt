package com.example.smartgardendemo.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartgardendemo.core.theme.AppColors

@Composable
fun SensorCard(
    sensorValor: Int,       // 0 = húmedo | 1 = seco
    relayEstado: Int,       // 0 = apagado | 1 = encendido
    isConnected: Boolean,   // true = Firebase conectado
    modifier: Modifier = Modifier
) {
    // ── Color animado según estado del sensor ─────────────────────────────
    val cardColor by animateColorAsState(
        targetValue = when {
            !isConnected    -> AppColors.RelayOff
            sensorValor == 0 -> AppColors.Humid
            else             -> AppColors.Dry
        },
        animationSpec = tween(durationMillis = 600),
        label = "cardColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape  = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Ícono del estado ──────────────────────────────────────────
            Text(
                text = when {
                    !isConnected     -> "📡"
                    sensorValor == 0 -> "💧"
                    else             -> "🌵"
                },
                fontSize = 52.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ── Título del estado ─────────────────────────────────────────
            Text(
                text = when {
                    !isConnected     -> "Sin conexión"
                    sensorValor == 0 -> "Suelo Húmedo"
                    else             -> "Suelo Seco"
                },
                fontSize   = 24.sp,
                fontWeight = FontWeight.Bold,
                color      = AppColors.TextOnDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            // ── Valor crudo del sensor ────────────────────────────────────
            Text(
                text  = "Valor del sensor: $sensorValor",
                fontSize = 14.sp,
                color = AppColors.TextOnDark.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = AppColors.TextOnDark.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(16.dp))

            // ── Estado del relé ───────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text     = if (relayEstado == 1) "🔵" else "⚪",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text      = if (relayEstado == 1) "Relé encendido" else "Relé apagado",
                    fontSize  = 16.sp,
                    color     = AppColors.TextOnDark,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}