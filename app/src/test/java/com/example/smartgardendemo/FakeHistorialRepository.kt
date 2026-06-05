package com.example.smartgardendemo

import com.example.smartgardendemo.data.model.HistorialEntry
import com.example.smartgardendemo.data.repository.HistorialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeHistorialRepository : HistorialRepository() {

    private val _historial = MutableStateFlow(emptyList<HistorialEntry>())
    val historial: MutableStateFlow<List<HistorialEntry>> = _historial

    var entradasGuardadas = mutableListOf<HistorialEntry>()

    override fun observarHistorial(): Flow<List<HistorialEntry>> = _historial

    override fun guardarEntrada(entry: HistorialEntry) {
        entradasGuardadas.add(entry)
    }
}
