package com.example.smartgardendemo.data.model

data class HistorialEntry(
    val id: String = "",
    val sensorHumedad: Int = 0,
    val relayEstado: Int = 0,
    val timestamp: Long = 0L,
    val fecha: String = "",
    val duracionEstadoAnterior: Long = 0L
)
