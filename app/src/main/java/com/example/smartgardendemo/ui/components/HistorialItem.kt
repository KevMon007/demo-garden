package com.example.smartgardendemo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartgardendemo.core.theme.AppColors
import com.example.smartgardendemo.data.model.HistorialEntry

@Composable
fun HistorialItem(
    entry: HistorialEntry,
    modifier: Modifier = Modifier
) {
    val esHumedo = entry.sensorHumedad == 0
    val icono = if (esHumedo) "💧" else "🌵"
    val bgColor = if (esHumedo) AppColors.Humid.copy(alpha = 0.1f) else AppColors.Dry.copy(alpha = 0.1f)
    val estadoTexto = if (esHumedo) "Húmedo" else "Seco"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.fecha,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = icono, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = estadoTexto,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                if (entry.duracionEstadoAnterior > 0) {
                    Text(
                        text = formatDuracion(entry.duracionEstadoAnterior),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
            Text(
                text = if (entry.relayEstado == 1) "Relé encendido 🔵" else "Relé apagado ⚪",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

private fun formatDuracion(segundos: Long): String {
    return if (segundos < 60) {
        "Duración anterior: ${segundos}s"
    } else {
        val minutos = segundos / 60
        val segs = segundos % 60
        "Duración anterior: ${minutos}m ${segs}s"
    }
}
