package com.example.smartgardendemo

import com.example.smartgardendemo.data.model.SensorData
import com.example.smartgardendemo.data.repository.SensorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSensorRepository : SensorRepository() {

    private val _datos = MutableStateFlow(SensorData())
    val datos: MutableStateFlow<SensorData> = _datos

    var ultimoValorRelay: Int? = null
    var relayDebeFallar = false

    override fun observarDatos(): Flow<SensorData> = _datos

    override fun setRelayManual(valor: Int, onResult: (Boolean) -> Unit) {
        ultimoValorRelay = valor
        onResult(!relayDebeFallar)
    }
}
