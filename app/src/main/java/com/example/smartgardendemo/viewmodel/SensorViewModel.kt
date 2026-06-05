package com.example.smartgardendemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartgardendemo.data.model.HistorialEntry
import com.example.smartgardendemo.data.model.SensorData
import com.example.smartgardendemo.data.repository.HistorialRepository
import com.example.smartgardendemo.data.repository.SensorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class SensorViewModel(
    private val repository: SensorRepository = SensorRepository(),
    private val historialRepository: HistorialRepository = HistorialRepository()
) : ViewModel() {

    private var ultimoSensorValor = -1
    private var ultimoCambioTimestamp = System.currentTimeMillis()

    // ── Estado principal — datos del sensor ──────────────────────────────
    private val _sensorData = MutableStateFlow(SensorData())
    val sensorData: StateFlow<SensorData> = _sensorData.asStateFlow()

    // ── Estado de carga inicial ────────────────────────────────────────────
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // ── Estado de conexión ────────────────────────────────────────────────
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    // ── Estado del resultado al escribir en Firebase ──────────────────────
    private val _relayResult = MutableStateFlow<String?>(null)
    val relayResult: StateFlow<String?> = _relayResult.asStateFlow()

    // ── Iniciar observación al crear el ViewModel ─────────────────────────
    init {
        observarSensor()
    }

    // ── Escuchar cambios en tiempo real desde Firebase ────────────────────
    private fun observarSensor() {
        viewModelScope.launch {
            try {
                repository.observarDatos().collect { data ->
                    _sensorData.value  = data
                    _isConnected.value = true
                    _isLoading.value = false

                    // ── Detectar cambio de humedad y guardar en historial ─
                    if (data.sensorHumedad != ultimoSensorValor && ultimoSensorValor != -1) {
                        val duracion = (System.currentTimeMillis() - ultimoCambioTimestamp) / 1000
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                        val entry = HistorialEntry(
                            sensorHumedad = ultimoSensorValor,
                            relayEstado = data.relayEstado,
                            timestamp = System.currentTimeMillis(),
                            fecha = dateFormat.format(System.currentTimeMillis()),
                            duracionEstadoAnterior = duracion
                        )
                        historialRepository.guardarEntrada(entry)
                        ultimoCambioTimestamp = System.currentTimeMillis()
                    }
                    ultimoSensorValor = data.sensorHumedad
                }
            } catch (e: Exception) {
                _isConnected.value = false
            }
        }
    }

    // ── Enviar comando de relé manual desde la app ────────────────────────
    fun setRelayManual(encender: Boolean) {
        val valor = if (encender) 1 else 0
        viewModelScope.launch {
            repository.setRelayManual(valor) { exitoso ->
                _relayResult.value = if (exitoso) {
                    if (encender) "✅ Relé encendido" else "✅ Relé apagado"
                } else {
                    "❌ Error al controlar el relé"
                }
            }
        }
    }

    // ── Limpiar mensaje de resultado después de mostrarlo ─────────────────
    fun clearRelayResult() {
        _relayResult.value = null
    }
}