package com.example.smartgardendemo.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartgardendemo.core.theme.AppColors

@Composable
fun RelayControl(
    relayManualFb: Int,             // 0 = automático | 1 = manual encendido
    onRelayChange: (Boolean) -> Unit, // callback al ViewModel
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {
    // ── Estado optimista local — feedback visual inmediato ────────────────
    var localChecked by remember { mutableStateOf(relayManualFb == 1) }
    // Cuando Firebase/ESP8266 confirma el cambio, sincroniza
    LaunchedEffect(relayManualFb) {
        localChecked = relayManualFb == 1
    }

    // ── Color animado del switch ──────────────────────────────────────────
    val switchColor by animateColorAsState(
        targetValue   = if (localChecked) AppColors.RelayOn else AppColors.RelayOff,
        animationSpec = tween(durationMillis = 400),
        label         = "switchColor"
    )

    Card(
        modifier  = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // ── Info del control ──────────────────────────────────────────
            Column {
                Text(
                    text       = "Control Manual del Relé",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text     = if (localChecked) "Encendido manualmente" else "Controlado por sensor",
                    fontSize = 13.sp,
                    color    = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // ── Switch ────────────────────────────────────────────────────
            Switch(
                checked  = localChecked,
                onCheckedChange = { nuevoValor ->
                    if (isConnected) {
                        localChecked = nuevoValor     // feedback visual inmediato
                        onRelayChange(nuevoValor)
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor   = AppColors.TextOnDark,
                    checkedTrackColor   = switchColor,
                    uncheckedThumbColor = AppColors.TextOnDark,
                    uncheckedTrackColor = switchColor
                )
            )
        }

        // ── Advertencia si no hay conexión ────────────────────────────────
        if (!isConnected) {
            Text(
                text     = "⚠️ Sin conexión — control deshabilitado",
                fontSize = 12.sp,
                color    = AppColors.Red,
                modifier = Modifier.padding(start = 20.dp, bottom = 12.dp)
            )
        }
    }
}