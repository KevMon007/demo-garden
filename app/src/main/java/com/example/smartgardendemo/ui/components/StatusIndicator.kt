package com.example.smartgardendemo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartgardendemo.R
import com.example.smartgardendemo.core.theme.AppColors

@Composable
fun StatusIndicator(
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {
    // ── Animación de pulso cuando está conectado ───────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue   = 1f,
        targetValue    = 1.3f,
        animationSpec  = infiniteRepeatable(
            animation  = tween(800, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Card(
        modifier  = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(
            containerColor = if (isConnected)
                AppColors.Humid.copy(alpha = 0.1f)
            else
                AppColors.Red.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // ── Círculo de estado con animación ───────────────────────
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .scale(if (isConnected) scale else 1f)
                        .background(
                            color = if (isConnected) AppColors.Humid else AppColors.Red,
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text       = if (isConnected) stringResource(R.string.firebase_conectado) else stringResource(R.string.sin_conexion_firebase),
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color      = if (isConnected) AppColors.Humid else AppColors.Red
                )
            }

            // ── Chip de estado del ESP8266 ────────────────────────────────
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isConnected)
                    AppColors.Humid.copy(alpha = 0.2f)
                else
                    AppColors.Red.copy(alpha = 0.2f)
            ) {
                Text(
                    text     = if (isConnected) stringResource(R.string.esp8266_activo) else stringResource(R.string.esp8266_inactivo),
                    fontSize = 12.sp,
                    color    = if (isConnected) AppColors.Humid else AppColors.Red,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}