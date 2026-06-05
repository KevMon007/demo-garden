package com.example.smartgardendemo.data.model

data class SensorData(
    val sensorHumedad: Int = 0,   // 0 = húmedo | 1 = seco
    val relayEstado: Int = 0,     // 0 = apagado | 1 = encendido
    val relayManualFb: Int = 0    // 0 = automático | 1 = manual
)