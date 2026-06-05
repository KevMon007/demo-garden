package com.example.smartgardendemo

import com.example.smartgardendemo.data.model.SensorData
import com.example.smartgardendemo.viewmodel.SensorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SensorViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `estado inicial tiene valores por defecto`() {
        val fakeSensorRepo = FakeSensorRepository()
        val fakeHistorialRepo = FakeHistorialRepository()
        val viewModel = SensorViewModel(fakeSensorRepo, fakeHistorialRepo)

        assertEquals(SensorData(), viewModel.sensorData.value)
        assertEquals(false, viewModel.isConnected.value)
        assertNull(viewModel.relayResult.value)
        assertTrue(viewModel.isLoading.value)
    }

    @Test
    fun `al recibir datos del sensor se actualiza el estado y conexion`() = runTest(testDispatcher) {
        val fakeSensorRepo = FakeSensorRepository()
        val fakeHistorialRepo = FakeHistorialRepository()
        val viewModel = SensorViewModel(fakeSensorRepo, fakeHistorialRepo)

        fakeSensorRepo.datos.value = SensorData(sensorHumedad = 1, relayEstado = 0, relayManualFb = 0)
        advanceUntilIdle()

        assertEquals(1, viewModel.sensorData.value.sensorHumedad)
        assertEquals(true, viewModel.isConnected.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `setRelayManual exitoso actualiza relayResult con check`() = runTest(testDispatcher) {
        val fakeSensorRepo = FakeSensorRepository()
        val fakeHistorialRepo = FakeHistorialRepository()
        val viewModel = SensorViewModel(fakeSensorRepo, fakeHistorialRepo)

        viewModel.setRelayManual(true)
        advanceUntilIdle()

        assertTrue(viewModel.relayResult.value?.contains("✅") == true)
        assertEquals(1, fakeSensorRepo.ultimoValorRelay)
    }

    @Test
    fun `setRelayManual fallido actualiza relayResult con error`() = runTest(testDispatcher) {
        val fakeSensorRepo = FakeSensorRepository().apply { relayDebeFallar = true }
        val fakeHistorialRepo = FakeHistorialRepository()
        val viewModel = SensorViewModel(fakeSensorRepo, fakeHistorialRepo)

        viewModel.setRelayManual(false)
        advanceUntilIdle()

        assertTrue(viewModel.relayResult.value?.contains("❌") == true)
        assertEquals(0, fakeSensorRepo.ultimoValorRelay)
    }

    @Test
    fun `clearRelayResult limpia el mensaje`() {
        val fakeSensorRepo = FakeSensorRepository()
        val fakeHistorialRepo = FakeHistorialRepository()
        val viewModel = SensorViewModel(fakeSensorRepo, fakeHistorialRepo)

        viewModel.clearRelayResult()

        assertNull(viewModel.relayResult.value)
    }
}
