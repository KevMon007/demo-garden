package com.example.smartgardendemo

import com.example.smartgardendemo.ui.components.formatDuracion
import org.junit.Assert.assertEquals
import org.junit.Test

class FormatDuracionTest {

    @Test
    fun `segundos menores a 60 muestra solo segundos`() {
        assertEquals("Duración anterior: 0s", formatDuracion(0L))
        assertEquals("Duración anterior: 30s", formatDuracion(30L))
        assertEquals("Duración anterior: 59s", formatDuracion(59L))
    }

    @Test
    fun `segundos igual o mayor a 60 muestra minutos y segundos`() {
        assertEquals("Duración anterior: 1m 0s", formatDuracion(60L))
        assertEquals("Duración anterior: 1m 30s", formatDuracion(90L))
        assertEquals("Duración anterior: 2m 0s", formatDuracion(120L))
        assertEquals("Duración anterior: 60m 0s", formatDuracion(3600L))
    }
}
