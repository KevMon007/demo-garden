package com.example.smartgardendemo.core.constants

object FirebasePaths {
    // ── Nodos de lectura (ESP8266 escribe aquí) ──
    const val DATOS          = "/humedad/datos"
    const val SENSOR_HUMEDAD = "/humedad/datos/sensor_humedad"
    const val RELAY_ESTADO   = "/humedad/datos/relay_estado"

    // ── Nodo de control (app escribe aquí) ──
    const val RELAY_MANUAL   = "/humedad/control/relay_manual"

    // ── Nodo de historial ──
    const val HISTORIAL      = "/humedad/historial"
}