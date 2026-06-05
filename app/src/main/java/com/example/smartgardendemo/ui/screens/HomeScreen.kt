package com.example.smartgardendemo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartgardendemo.R
import com.example.smartgardendemo.core.theme.AppColors
import com.example.smartgardendemo.ui.components.RelayControl
import com.example.smartgardendemo.ui.components.SensorCard
import com.example.smartgardendemo.ui.components.StatusIndicator
import com.example.smartgardendemo.viewmodel.SensorViewModel

@Composable
fun HomeScreen(
    onNavigateToHistorial: () -> Unit = {},
    viewModel: SensorViewModel = viewModel()
) {
    // ── Observar estados desde el ViewModel ───────────────────────────────
    val sensorData  by viewModel.sensorData.collectAsStateWithLifecycle()
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()
    val isLoading   by viewModel.isLoading.collectAsStateWithLifecycle()
    val relayResult by viewModel.relayResult.collectAsStateWithLifecycle()

    // ── Snackbar para mostrar resultado del relé ──────────────────────────
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar snackbar cuando hay un resultado del relé
    LaunchedEffect(relayResult) {
        relayResult?.let { mensaje ->
            snackbarHostState.showSnackbar(
                message  = mensaje,
                duration = SnackbarDuration.Short
            )
            viewModel.clearRelayResult()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(24.dp))

                    // ── Encabezado ────────────────────────────────────────
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
            Text(
                    text       = stringResource(R.string.app_title),
                    fontSize   = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color      = AppColors.DarkBlue
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text     = stringResource(R.string.app_subtitle),
                            fontSize = 14.sp,
                            color    = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Indicador de conexión ─────────────────────────────
                    StatusIndicator(isConnected = isConnected)

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Tarjeta principal del sensor ──────────────────────
                    SensorCard(
                        sensorValor = sensorData.sensorHumedad,
                        relayEstado = sensorData.relayEstado,
                        isConnected = isConnected
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Control manual del relé ───────────────────────────
                    RelayControl(
                        relayManualFb = sensorData.relayManualFb,
                        isConnected   = isConnected,
                        onRelayChange = { encender ->
                            viewModel.setRelayManual(encender)
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Tarjetas de datos individuales ────────────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DataChip(
                            modifier = Modifier.weight(1f),
                    label    = stringResource(R.string.sensor_label),
                    valor    = if (sensorData.sensorHumedad == 0) stringResource(R.string.sensor_humedo) else stringResource(R.string.sensor_seco),
                            color    = if (sensorData.sensorHumedad == 0) AppColors.Humid else AppColors.Dry,
                            icono    = if (sensorData.sensorHumedad == 0) "💧" else "🌵"
                        )
                        DataChip(
                            modifier = Modifier.weight(1f),
                    label    = stringResource(R.string.rele_label),
                    valor    = if (sensorData.relayEstado == 1) stringResource(R.string.rele_encendido) else stringResource(R.string.rele_apagado),
                            color    = if (sensorData.relayEstado == 1) AppColors.RelayOn else AppColors.RelayOff,
                            icono    = if (sensorData.relayEstado == 1) "🔵" else "⚪"
                        )
                        DataChip(
                            modifier = Modifier.weight(1f),
                    label    = stringResource(R.string.modo_label),
                    valor    = if (sensorData.relayManualFb == 1) stringResource(R.string.modo_manual) else stringResource(R.string.modo_auto),
                            color    = if (sensorData.relayManualFb == 1) AppColors.Blue else AppColors.Humid,
                            icono    = if (sensorData.relayManualFb == 1) "🕹️" else "🤖"
                        )
                    }

                    // ── Botón para ir al historial ──────────────────────────
                    Button(
                        onClick = onNavigateToHistorial,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.DarkBlue
                        )
                    ) {
                        Text(stringResource(R.string.btn_historial), color = AppColors.TextOnDark)
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

// ── Componente auxiliar — chip de dato individual ─────────────────────────────
@Composable
private fun DataChip(
    label    : String,
    valor    : String,
    color    : androidx.compose.ui.graphics.Color,
    icono    : String,
    modifier : Modifier = Modifier
) {
    Card(
        modifier  = modifier,
        colors    = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.12f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icono, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text       = valor,
                fontSize   = 13.sp,
                fontWeight = FontWeight.Bold,
                color      = color
            )
            Text(
                text     = label,
                fontSize = 11.sp,
                color    = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}